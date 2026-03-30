package com.example.rickandmorty.screens.allEpisodes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.model.domain.Episode
import com.example.rickandmorty.components.common.LoadingState
import com.example.rickandmorty.components.common.SimpleToolbar
import com.example.rickandmorty.components.episode.EpisodeRowComponent
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.flatMap

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllEpisodesScreen(
    modifier: Modifier = Modifier,
    episodesViewModel: AllEpisodesViewModel = koinViewModel()
) {
    val uiState by episodesViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        episodesViewModel.refreshAllEpisodes()
    }

    when (val state = uiState) {
        AllEpisodesUiState.Error -> {
            // todo
        }

        AllEpisodesUiState.Loading -> LoadingState()
        is AllEpisodesUiState.Success -> {
            Column(
                modifier = modifier
            ) {
                SimpleToolbar(title = "All episodes")
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    state.data.forEach { mapEntry ->
                        stickyHeader(key = mapEntry.key) {
                            Header(
                                seasonName = mapEntry.key,
                                uniqueCharacterCount = mapEntry.value.flatMap {
                                    it.characterIdsInEpisode
                                }.toSet().size
                            )
                        }

                        mapEntry.value.forEach { episode ->
                            item(key = episode.id) { EpisodeRowComponent(episode = episode) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(seasonName: String, uniqueCharacterCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        Text(text = seasonName, color = Color.Black, fontSize = 32.sp)
        Text(
            text = "$uniqueCharacterCount unique characters",
            color = Color.Black,
            fontSize = 22.sp
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(4.dp)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}

sealed interface AllEpisodesUiState {
    object Error : AllEpisodesUiState
    object Loading : AllEpisodesUiState
    data class Success(val data: Map<String, List<Episode>>) : AllEpisodesUiState
}