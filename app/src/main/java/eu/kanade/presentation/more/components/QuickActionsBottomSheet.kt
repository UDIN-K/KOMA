package eu.kanade.presentation.more.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideUpVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.automirrored.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.DataUsage
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.GetApp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import eu.kanade.presentation.more.DownloadQueueState
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.ui.more.DownloadQueueState as MoreDownloadQueueState
import tachiyomi.core.common.Constants
import tachiyomi.i18n.MR
import tachiyomi.i18n.sy.SYMR
import tachiyomi.presentation.core.i18n.stringResource

/**
 * Quick Actions Panel - Swipe up from bottom to reveal
 * Shows frequently used actions for the current tab
 */
@Composable
fun QuickActionsBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    // Quick actions callbacks
    onClickSettings: () -> Unit,
    onClickStats: () -> Unit,
    onClickCategories: () -> Unit,
    onClickDownloads: () -> Unit,
    onClickDataAndStorage: () -> Unit,
    onClickAbout: () -> Unit,
    onClickBatchAdd: () -> Unit,
    onClickHelp: () -> Unit,
    // State
    downloadQueueState: MoreDownloadQueueState,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showFullPanel by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            isExpanded = true
            // Delay showing full panel for smooth animation
            kotlinx.coroutines.delay(50)
            showFullPanel = true
        } else {
            showFullPanel = false
            kotlinx.coroutines.delay(150)
            isExpanded = false
        }
    }

    if (isExpanded) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = { onDismiss() },
                        )
                    },
            ) {
                // Background scrim
                AnimatedVisibility(
                    visible = showFullPanel,
                    enter = fadeIn(animationSpec = tween(150)),
                    exit = fadeOut(animationSpec = tween(100)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f)),
                    )
                }

                // Bottom sheet
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = 0.8f,
                                stiffness = 300f,
                            ),
                        ),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                    ),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shadowElevation = 16.dp,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                    ) {
                        // Drag handle
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)),
                            )
                        }

                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(MR.strings.label_more),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )

                            Text(
                                text = "Swipe down to close",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )

                        // Quick Actions Grid
                        AnimatedVisibility(
                            visible = showFullPanel,
                            enter = slideUpVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(200),
                            ) + fadeIn(animationSpec = tween(150)),
                            exit = fadeOut(animationSpec = tween(100)),
                        ) {
                            QuickActionsGrid(
                                downloadQueueState = downloadQueueState,
                                onClickSettings = onClickSettings,
                                onClickStats = onClickStats,
                                onClickCategories = onClickCategories,
                                onClickDownloads = onClickDownloads,
                                onClickDataAndStorage = onClickDataAndStorage,
                                onClickAbout = onClickAbout,
                                onClickBatchAdd = onClickBatchAdd,
                                onClickHelp = onClickHelp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionsGrid(
    downloadQueueState: MoreDownloadQueueState,
    onClickSettings: () -> Unit,
    onClickStats: () -> Unit,
    onClickCategories: () -> Unit,
    onClickDownloads: () -> Unit,
    onClickDataAndStorage: () -> Unit,
    onClickAbout: () -> Unit,
    onClickBatchAdd: () -> Unit,
    onClickHelp: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        // Row 1: Settings, Downloads, Categories
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            QuickActionItem(
                icon = Icons.Outlined.Settings,
                label = stringResource(MR.strings.label_settings),
                onClick = onClickSettings,
            )

            val downloadSubtitle = when (downloadQueueState) {
                MoreDownloadQueueState.Stopped -> "Idle"
                is MoreDownloadQueueState.Paused -> "${downloadQueueState.pending} pending"
                is MoreDownloadQueueState.Downloading -> "${downloadQueueState.pending} active"
            }

            QuickActionItem(
                icon = Icons.Outlined.GetApp,
                label = "Downloads",
                subtitle = downloadSubtitle,
                onClick = onClickDownloads,
            )

            QuickActionItem(
                icon = Icons.AutoMirrored.Outlined.Label,
                label = stringResource(MR.strings.categories),
                onClick = onClickCategories,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 2: Stats, Data, Batch Add
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            QuickActionItem(
                icon = Icons.Outlined.QueryStats,
                label = stringResource(MR.strings.label_stats),
                onClick = onClickStats,
            )

            QuickActionItem(
                icon = Icons.Outlined.Storage,
                label = stringResource(MR.strings.label_data_storage),
                onClick = onClickDataAndStorage,
            )

            QuickActionItem(
                icon = Icons.AutoMirrored.Outlined.PlaylistAdd,
                label = stringResource(SYMR.strings.eh_batch_add),
                onClick = onClickBatchAdd,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 3: About, Help
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            QuickActionItem(
                icon = Icons.Outlined.Info,
                label = stringResource(MR.strings.pref_category_about),
                onClick = onClickAbout,
            )

            QuickActionItem(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                label = stringResource(MR.strings.label_help),
                onClick = { uriHandler.openUri(Constants.URL_HELP) },
            )

            // Empty placeholder for symmetry
            Box(modifier = Modifier.width(80.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Quick Actions - More Tab",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(
                MaterialTheme.colorScheme.surfaceContainerHighest,
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )

        if (subtitle != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
    }
}