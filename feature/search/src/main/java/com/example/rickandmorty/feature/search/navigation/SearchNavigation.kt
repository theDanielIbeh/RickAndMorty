package com.example.rickandmorty.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.rickandmorty.feature.search.SearchScreen
import kotlinx.serialization.Serializable

@Serializable data object SearchRoute

fun NavGraphBuilder.search(
    onCharacterClicked: (Int) -> Unit
) {
    composable<SearchRoute> {
        SearchScreen(
            onCharacterClicked = onCharacterClicked
        )
    }
}

fun NavController.navigateToSearch(navOptions: NavOptions? = null) = navigate(SearchRoute, navOptions)