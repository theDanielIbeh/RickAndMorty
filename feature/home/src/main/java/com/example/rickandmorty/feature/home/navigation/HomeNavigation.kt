package com.example.rickandmorty.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.rickandmorty.feature.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) = navigate(
    route =
        HomeRoute, navOptions = navOptions
)

fun NavGraphBuilder.homeScreen(
    onCharacterSelected: (Int) -> Unit
) {
    composable<HomeRoute> {
        HomeScreen(onCharacterSelected = onCharacterSelected)
    }
}