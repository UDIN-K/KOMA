package eu.kanade.presentation.library.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.updates.UpdatesItem
import tachiyomi.domain.manga.model.MangaCover
import eu.kanade.presentation.manga.components.MangaCover as MangaCoverComponent

val LocalLibraryUpdates = staticCompositionLocalOf<List<UpdatesItem>> { emptyList() }
val LocalLibraryUpdatesOnClickAll = staticCompositionLocalOf<() -> Unit> { {} }

@Composable
fun LibraryUpdatesCarousel(
    updates: List<UpdatesItem>,
    onClickAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tachiyomi.presentation.core.i18n.stringResource(tachiyomi.i18n.MR.strings.label_recent_updates),
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(onClick = onClickAll) {
                Text(text = tachiyomi.presentation.core.i18n.stringResource(tachiyomi.i18n.MR.strings.label_more))
            }
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(updates) { item ->
                val manga = item.update
                val navigator = cafe.adriel.voyager.navigator.LocalNavigator.current
                Column(
                    modifier = Modifier.width(100.dp).clickable { 
                        navigator?.push(eu.kanade.tachiyomi.ui.manga.MangaScreen(manga.mangaId)) 
                    }
                ) {
                    MangaCoverComponent.Book(
                        modifier = Modifier.fillMaxWidth(),
                        data = manga.coverData
                    )
                    Text(
                        text = manga.mangaTitle,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
