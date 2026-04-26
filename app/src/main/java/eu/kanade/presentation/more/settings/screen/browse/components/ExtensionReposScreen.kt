@file:JvmName("ExtensionReposScreenKt")

package eu.kanade.presentation.more.settings.screen.browse.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import eu.kanade.presentation.components.AppBar
import eu.kanade.presentation.more.settings.screen.browse.RepoScreenState
import mihon.domain.extensionrepo.model.ExtensionRepo
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.components.material.topSmallPaddingValues
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.screens.EmptyScreen
import tachiyomi.presentation.core.util.plus

@Composable
fun ExtensionReposScreen(
    state: RepoScreenState.Success,
    onClickCreate: () -> Unit,
    onOpenWebsite: (ExtensionRepo) -> Unit,
    onClickDelete: (String) -> Unit,
    onClickRefresh: () -> Unit,
    onClickFinder: () -> Unit,
    onClickTroubleshoot: () -> Unit,
    navigateUp: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    var isFabExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { scrollBehavior ->
            AppBar(
                navigateUp = navigateUp,
                title = stringResource(MR.strings.label_extension_repos),
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = onClickRefresh) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = stringResource(resource = MR.strings.action_webview_refresh),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            MultiActionFab(
                isExpanded = isFabExpanded,
                onToggle = { isFabExpanded = !isFabExpanded },
                onClickFinder = {
                    isFabExpanded = false
                    onClickFinder()
                },
                onClickCreate = {
                    isFabExpanded = false
                    onClickCreate()
                },
                onClickTroubleshoot = {
                    isFabExpanded = false
                    onClickTroubleshoot()
                },
            )
        },
    ) { paddingValues ->
        // Scrim when FAB is expanded
        if (isFabExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { isFabExpanded = false },
            )
        }

        if (state.isEmpty) {
            EmptyScreen(
                MR.strings.information_empty_repos,
                modifier = Modifier.padding(paddingValues),
            )
            return@Scaffold
        }

        ExtensionReposContent(
            repos = state.repos,
            lazyListState = lazyListState,
            paddingValues = paddingValues + topSmallPaddingValues +
                PaddingValues(horizontal = MaterialTheme.padding.medium),
            onOpenWebsite = onOpenWebsite,
            onClickDelete = onClickDelete,
        )
    }
}

@Composable
private fun MultiActionFab(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onClickFinder: () -> Unit,
    onClickCreate: () -> Unit,
    onClickTroubleshoot: () -> Unit,
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(300),
    )

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Mini FAB items
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(tween(200)) + slideInVertically(tween(250)) { it / 2 } + scaleIn(tween(250)),
            exit = fadeOut(tween(150)) + slideOutVertically(tween(200)) { it / 2 } + scaleOut(tween(200)),
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MiniFabItem(
                    icon = Icons.Outlined.Build,
                    label = stringResource(MR.strings.action_troubleshoot),
                    onClick = onClickTroubleshoot,
                )
                MiniFabItem(
                    icon = Icons.Outlined.Add,
                    label = stringResource(MR.strings.action_add_repo_manual),
                    onClick = onClickCreate,
                )
                MiniFabItem(
                    icon = Icons.Outlined.Explore,
                    label = stringResource(MR.strings.label_repo_finder),
                    onClick = onClickFinder,
                )
            }
        }

        // Main FAB
        FloatingActionButton(
            onClick = onToggle,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Outlined.Close else Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.rotate(rotation),
            )
        }
    }
}

@Composable
private fun MiniFabItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        // Label chip
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = 2.dp,
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Mini FAB
        SmallFloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
