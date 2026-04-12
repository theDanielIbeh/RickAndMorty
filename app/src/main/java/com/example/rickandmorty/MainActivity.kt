package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.rickandmorty.feature.characterdetails.navigation.characterDetailsScreen
import com.example.rickandmorty.feature.characterdetails.navigation.navigateToCharacterDetails
import com.example.rickandmorty.feature.characterepisodes.navigation.characterEpisodesScreen
import com.example.rickandmorty.feature.characterepisodes.navigation.navigateToCharacterEpisodes
import com.example.rickandmorty.feature.episodes.navigation.EpisodesRoute
import com.example.rickandmorty.feature.episodes.navigation.episodes
import com.example.rickandmorty.feature.episodes.navigation.navigateToEpisodes
import com.example.rickandmorty.feature.home.navigation.HomeBaseRoute
import com.example.rickandmorty.feature.home.navigation.HomeRoute
import com.example.rickandmorty.feature.home.navigation.homeSection
import com.example.rickandmorty.feature.home.navigation.navigateToHome
import com.example.rickandmorty.feature.search.navigation.SearchRoute
import com.example.rickandmorty.feature.search.navigation.navigateToSearch
import com.example.rickandmorty.feature.search.navigation.search
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import kotlin.reflect.KClass

sealed class NavDestination(val title: String, val route: KClass<*>, val icon: ImageVector) {
    object Home : NavDestination(title = "Home", route = HomeRoute::class, icon = Icons.Filled.Home)
    object Episodes :
        NavDestination(
            title = "Episodes",
            route = EpisodesRoute::class,
            icon = Icons.Filled.PlayArrow
        )

    object Search :
        NavDestination(title = "Search", route = SearchRoute::class, icon = Icons.Filled.Search)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                val navController = rememberNavController()
                val items = listOf(
                    NavDestination.Home, NavDestination.Episodes, NavDestination.Search
                )
                var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color.White
                        ) {
                            items.forEachIndexed { index, screen ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(imageVector = screen.icon, contentDescription = null)
                                    },
                                    label = { Text(screen.title) },
                                    selected = index == selectedIndex,
                                    onClick = {
                                        trace("Navigation: ${items[index]}") {
                                            val navigationOptions = navOptions {
                                                selectedIndex = index
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true
                                                // Restore state when reselecting a previously selected item
                                                restoreState = true
                                            }

                                            when (items[index]) {
                                                NavDestination.Home -> navController.navigateToHome(
                                                    navigationOptions
                                                )

                                                NavDestination.Episodes -> navController.navigateToEpisodes(
                                                    navigationOptions
                                                )

                                                NavDestination.Search -> navController.navigateToSearch(
                                                    navigationOptions
                                                )
                                            }
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = Color.Black,
                                        indicatorColor = Color.Transparent
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = HomeBaseRoute,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        homeSection(
                            onCharacterSelected = navController::navigateToCharacterDetails
                        )
                        episodes()
                        search(
                            onCharacterClicked = navController::navigateToCharacterDetails
                        )
                        characterDetailsScreen(
                            onEpisodeClicked = navController::navigateToCharacterEpisodes,
                            onBackClicked = navController::popBackStack
                        )
                        characterEpisodesScreen(
                            onBackClicked = navController::popBackStack
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RickAndMortyTheme {
        Greeting("Android")
    }
}