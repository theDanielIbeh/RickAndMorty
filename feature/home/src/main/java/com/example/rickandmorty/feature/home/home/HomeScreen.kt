package com.example.rickandmorty.feature.home.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.rickandmorty.core.designsystem.components.common.LoadingState
import com.example.rickandmorty.core.designsystem.components.common.SimpleToolbar
import com.example.rickandmorty.core.ui.character.CharacterGridItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onCharacterSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()

    when (characters.loadState.refresh) {
        is LoadState.Loading -> LoadingState()
        else -> {
            Column(modifier = modifier) {
                SimpleToolbar(title = "All characters")
                LazyVerticalGrid(
                    contentPadding = PaddingValues(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    columns = GridCells.Fixed(2),
                    content = {
                        items(
                            count = characters.itemCount,
                            key = characters.itemKey { it.id }
                        ) { index ->
                            val character = characters[index]
                            if (character != null) {
                                CharacterGridItem(
                                    modifier = Modifier,
                                    character = character
                                ) {
                                    onCharacterSelected(character.id)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}