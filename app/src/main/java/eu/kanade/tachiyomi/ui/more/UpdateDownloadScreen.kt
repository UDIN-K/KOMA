package eu.kanade.tachiyomi.ui.more

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.presentation.more.UpdateDownloadScreen
import eu.kanade.presentation.more.UpdateDownloadState
import eu.kanade.presentation.util.Screen
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.network.NetworkHelper
import eu.kanade.tachiyomi.network.ProgressListener
import eu.kanade.tachiyomi.network.await
import eu.kanade.tachiyomi.network.newCachelessCallWithProgress
import eu.kanade.tachiyomi.util.storage.getUriCompat
import eu.kanade.tachiyomi.util.storage.saveTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tachiyomi.core.common.util.lang.launchIO
import tachiyomi.core.common.util.lang.withUIContext
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.io.File

class UpdateDownloadScreen(
    private val versionName: String,
    private val downloadLink: String,
) : Screen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel {
            UpdateDownloadScreenModel(
                downloadLink = downloadLink,
                versionName = versionName,
            )
        }

        val state by screenModel.state.collectAsState()
        val progress by screenModel.progress.collectAsState()
        val errorMessage by screenModel.errorMessage.collectAsState()

        // Start download automatically when screen opens
        LaunchedEffect(Unit) {
            screenModel.startDownload(context)
        }

        // When download is done, trigger install
        LaunchedEffect(state) {
            if (state == UpdateDownloadState.DONE) {
                screenModel.installApk(context)
            }
        }

        UpdateDownloadScreen(
            versionName = versionName,
            downloadProgress = progress,
            state = state,
            errorMessage = errorMessage,
            onRetryClick = {
                screenModel.startDownload(context)
            },
            onCancelClick = {
                screenModel.cancelDownload()
                navigator.pop()
            },
        )
    }
}

class UpdateDownloadScreenModel(
    private val downloadLink: String,
    private val versionName: String,
    private val network: NetworkHelper = Injekt.get(),
) : ScreenModel {

    private val _state = MutableStateFlow(UpdateDownloadState.DOWNLOADING)
    val state: StateFlow<UpdateDownloadState> = _state.asStateFlow()

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var apkFile: File? = null
    private var isCancelled = false

    fun startDownload(context: android.content.Context) {
        _state.value = UpdateDownloadState.DOWNLOADING
        _progress.value = 0
        _errorMessage.value = null
        isCancelled = false

        screenModelScope.launchIO {
            try {
                downloadApk(context)
            } catch (e: Exception) {
                if (!isCancelled) {
                    _errorMessage.value = e.message
                    _state.value = UpdateDownloadState.ERROR
                }
            }
        }
    }

    private suspend fun downloadApk(context: android.content.Context) {
        val progressListener = object : ProgressListener {
            var savedProgress = 0
            var lastTick = 0L

            override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                val currentProgress = (100 * (bytesRead.toFloat() / contentLength)).toInt()
                val currentTime = System.currentTimeMillis()
                if (currentProgress > savedProgress && currentTime - 100 > lastTick) {
                    savedProgress = currentProgress
                    lastTick = currentTime
                    _progress.value = currentProgress
                }
            }
        }

        val response = network.client.newCachelessCallWithProgress(GET(downloadLink), progressListener)
            .await()

        if (isCancelled) return

        val file = File(context.externalCacheDir, "update.apk")
        apkFile = file

        if (response.isSuccessful) {
            response.body.source().saveTo(file)
            _progress.value = 100
            // Brief pause before moving to install state
            delay(500)
            _state.value = UpdateDownloadState.DONE
        } else {
            response.close()
            throw Exception("Download failed: ${response.code}")
        }
    }

    fun installApk(context: android.content.Context) {
        val file = apkFile ?: return
        _state.value = UpdateDownloadState.INSTALLING

        screenModelScope.launchIO {
            try {
                withUIContext {
                    val uri = file.getUriCompat(context)
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "application/vnd.android.package-archive")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _state.value = UpdateDownloadState.ERROR
            }
        }
    }

    fun cancelDownload() {
        isCancelled = true
    }
}
