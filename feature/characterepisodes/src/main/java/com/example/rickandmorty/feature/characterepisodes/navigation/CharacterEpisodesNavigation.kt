package com.example.rickandmorty.feature.characterepisodes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rickandmorty.feature.characterepisodes.CharacterEpisodesScreen
import kotlinx.serialization.Serializable


@Serializable
data class CharacterEpisodesRoute(
    // The ID of the topic which will be initially selected at this destination
    val characterId: Int? = null,
)

fun NavGraphBuilder.characterEpisodesScreen(
    onBackClicked: () -> Unit
) {
    composable<CharacterEpisodesRoute> {entry ->
        val characterId = entry.toRoute<CharacterEpisodesRoute>().characterId ?: -1
        CharacterEpisodesScreen(
            characterId = characterId,
            onBackClicked = onBackClicked
        )
    }
}

fun NavController.navigateToCharacterEpisodes(
    characterId: Int? = null,
    navOptions: NavOptions? = null,
) {
    navigate(route = CharacterEpisodesRoute(characterId), navOptions)
}
