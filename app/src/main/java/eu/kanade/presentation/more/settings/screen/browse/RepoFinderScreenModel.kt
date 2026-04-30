package eu.kanade.presentation.more.settings.screen.browse

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.network.NetworkHelper
import eu.kanade.tachiyomi.network.awaitSuccess
import eu.kanade.tachiyomi.network.parseAs
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mihon.domain.extensionrepo.interactor.CreateExtensionRepo
import mihon.domain.extensionrepo.interactor.GetExtensionRepo
import tachiyomi.core.common.util.lang.launchIO
import tachiyomi.core.common.util.lang.withIOContext
import logcat.LogPriority
import tachiyomi.core.common.util.system.logcat
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class RepoFinderScreenModel(
    private val networkHelper: NetworkHelper = Injekt.get(),
    private val json: Json = Injekt.get(),
    private val createExtensionRepo: CreateExtensionRepo = Injekt.get(),
    private val getExtensionRepo: GetExtensionRepo = Injekt.get(),
) : StateScreenModel<RepoFinderState>(RepoFinderState.Loading) {

    companion object {
        private const val REPOS_URL = "https://udink.me/repos.json"
    }

    init {
        fetchRepos()
    }

    fun fetchRepos() {
        mutableState.update { RepoFinderState.Loading }
        screenModelScope.launchIO {
            try {
                val existingRepos = getExtensionRepo.getAll()
                val existingUrls = existingRepos.map { "${it.baseUrl}/index.min.json" }.toSet()

                val remoteRepos = withIOContext {
                    with(json) {
                        networkHelper.client.newCall(GET(REPOS_URL))
                            .awaitSuccess()
                            .parseAs<List<DiscoverableRepo>>()
                    }
                }

                val items = remoteRepos.map { repo ->
                    DiscoverableRepoItem(
                        repo = repo,
                        isAdded = existingUrls.contains(repo.url),
                        isAdding = false,
                    )
                }

                mutableState.update {
                    RepoFinderState.Success(items = items)
                }
            } catch (e: Exception) {
                logcat(LogPriority.ERROR, e) { "Failed to fetch discoverable repos" }
                mutableState.update { RepoFinderState.Error }
            }
        }
    }

    fun addRepo(repoUrl: String) {
        val currentState = state.value
        if (currentState !is RepoFinderState.Success) return

        // Mark as adding
        mutableState.update {
            RepoFinderState.Success(
                items = currentState.items.map { item ->
                    if (item.repo.url == repoUrl) item.copy(isAdding = true) else item
                },
            )
        }

        screenModelScope.launchIO {
            val result = createExtensionRepo.await(repoUrl)
            val success = result is CreateExtensionRepo.Result.Success ||
                result is CreateExtensionRepo.Result.RepoAlreadyExists

            mutableState.update { state ->
                if (state is RepoFinderState.Success) {
                    RepoFinderState.Success(
                        items = state.items.map { item ->
                            if (item.repo.url == repoUrl) {
                                item.copy(
                                    isAdded = success || item.isAdded,
                                    isAdding = false,
                                )
                            } else {
                                item
                            }
                        },
                    )
                } else {
                    state
                }
            }
        }
    }
}

@Serializable
data class DiscoverableRepo(
    val name: String,
    val shortName: String? = null,
    val url: String,
    val type: String = "manga",
    val description: String = "",
    val official: Boolean = false,
    val status: String = "online",
)

@Immutable
data class DiscoverableRepoItem(
    val repo: DiscoverableRepo,
    val isAdded: Boolean,
    val isAdding: Boolean,
)

sealed class RepoFinderState {
    data object Loading : RepoFinderState()
    data object Error : RepoFinderState()
    data class Success(val items: List<DiscoverableRepoItem>) : RepoFinderState()
}
