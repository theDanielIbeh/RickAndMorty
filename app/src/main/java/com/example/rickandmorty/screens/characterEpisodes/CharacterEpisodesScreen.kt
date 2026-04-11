package com.example.rickandmorty.screens.characterEpisodes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rickandmorty.core.network.model.domain.Character
import com.example.rickandmorty.core.network.model.domain.Episode
import com.example.rickandmorty.components.common.CharacterImage
import com.example.rickandmorty.components.common.CharacterNameComponent
import com.example.rickandmorty.components.common.DataPoint
import com.example.rickandmorty.components.common.DataPointComponent
import com.example.rickandmorty.components.common.LoadingState
import com.example.rickandmorty.components.common.SimpleToolbar
import com.example.rickandmorty.components.episode.EpisodeRowComponent
import org.koin.androidx.compose.koinViewModel

sealed interface ScreenState {
    object Loading : ScreenState
    data class Success(val character: Character, val episodes: List<Episode>) : ScreenState
    data class Error(val message: String) : ScreenState
}

@Composable
fun CharacterEpisodesScreen(
    characterId: Int,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterEpisodesViewModel = koinViewModel()
) {
    val screenState by viewModel.stateFlow.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchCharacter(characterId)
    })

    Column {
        when (val state = screenState) {
            ScreenState.Loading -> LoadingState()
            is ScreenState.Error -> {
                Text(
                    text = state.message,
                    color = White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp
                )
            }

            is ScreenState.Success -> {
                MainScreen(
                    character = state.character,
                    episodes = state.episodes,
                    onBackClicked = onBackClicked,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun MainScreen(
    character: Character, episodes: List<Episode>, onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val episodeBySeasonMap = episodes.groupBy { it.seasonNumber }

    Column(
        modifier = modifier
    ) {
        SimpleToolbar(title = "Character episodes", onBackAction = onBackClicked)
        LazyColumn(contentPadding = PaddingValues(all = 16.dp)) {
            item { CharacterNameComponent(name = character.name) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                LazyRow {
                    episodeBySeasonMap.forEach { mapEntry ->
                        val title = "Season ${mapEntry.key}"
                        val description = "${mapEntry.value.size} ep"
                        item {
                            DataPointComponent(dataPoint = DataPoint(title, description))
                            Spacer(modifier = Modifier.width(32.dp))
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { CharacterImage(imageUrl = character.imageUrl) }
            item { Spacer(modifier = Modifier.height(32.dp)) }

            episodeBySeasonMap.forEach { mapEntry ->
                stickyHeader { SeasonHeader(seasonNumber = mapEntry.key) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(mapEntry.value) { episode ->
                    EpisodeRowComponent(episode = episode)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SeasonHeader(seasonNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Text(
            text = "Season $seasonNumber",
            color = Black,
            fontSize = 32.sp,
            lineHeight = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 8.dp)
        )
    }
}