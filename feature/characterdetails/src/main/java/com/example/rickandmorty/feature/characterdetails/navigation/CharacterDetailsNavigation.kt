package com.example.rickandmorty.feature.characterdetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.rickandmorty.feature.characterdetails.CharacterDetailsScreen
import kotlinx.serialization.Serializable


@Serializable
data class CharacterDetailsRoute(
    // The ID of the topic which will be initially selected at this destination
    val characterId: Int? = null,
)

fun NavGraphBuilder.characterDetailsScreen(
    onEpisodeClicked: (Int) -> Unit,
    onBackClicked: () -> Unit
) {
    composable<CharacterDetailsRoute> { entry ->
        val characterId = entry.toRoute<CharacterDetailsRoute>().characterId ?: -1
        CharacterDetailsScreen(
            characterId = characterId,
            onEpisodeClicked = onEpisodeClicked,
            onBackClicked = onBackClicked
        )
    }
}

fun NavController.navigateToCharacterDetails(
    characterId: Int? = null,
    navOptions: NavOptions? = null,
) {
    navigate(route = CharacterDetailsRoute(characterId), navOptions)
}
