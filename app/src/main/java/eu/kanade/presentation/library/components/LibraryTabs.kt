package eu.kanade.presentation.library.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import eu.kanade.presentation.category.visualName
import tachiyomi.domain.category.model.Category
import tachiyomi.presentation.core.components.material.TabText

@Composable
internal fun LibraryTabs(
    categories: List<Category>,
    pagerState: PagerState,
    getItemCountForCategory: (Category) -> Int?,
    onTabItemClick: (Int) -> Unit,
) {
    val currentPageIndex = pagerState.currentPage.coerceAtMost(categories.lastIndex)
    androidx.compose.foundation.layout.Column(modifier = Modifier.zIndex(2f)) {
        androidx.compose.foundation.lazy.LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            androidx.compose.foundation.lazy.itemsIndexed(categories) { index, category ->
                val selected = currentPageIndex == index
                androidx.compose.material3.FilterChip(
                    selected = selected,
                    onClick = { onTabItemClick(index) },
                    label = {
                        androidx.compose.material3.Text(text = category.visualName)
                    },
                    leadingIcon = if (selected) {
                        { androidx.compose.material3.Icon(androidx.compose.material.icons.Icons.Filled.Check, contentDescription = null) }
                    } else null,
                    colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                        selectedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer,
                        selectedLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        }
    }
}
