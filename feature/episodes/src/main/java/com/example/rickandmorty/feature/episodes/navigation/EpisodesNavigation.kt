package com.example.rickandmorty.feature.episodes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.rickandmorty.feature.episodes.AllEpisodesScreen
import kotlinx.serialization.Serializable

@Serializable data object EpisodesRoute

fun NavController.navigateToEpisodes(
    navOptions: NavOptions? = null
) {
    navigate(EpisodesRoute, navOptions)
}

fun NavGraphBuilder.episodes() {
    composable<EpisodesRoute> { AllEpisodesScreen() }
}