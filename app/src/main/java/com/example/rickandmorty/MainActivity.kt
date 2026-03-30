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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickandmorty.screens.allEpisodes.AllEpisodesScreen
import com.example.rickandmorty.screens.characterDetails.CharacterDetailsScreen
import com.example.rickandmorty.screens.characterEpisodes.CharacterEpisodesScreen
import com.example.rickandmorty.screens.home.HomeScreen
import com.example.rickandmorty.ui.theme.RickAndMortyTheme

sealed class NavDestination(val title: String, val route: String, val icon: ImageVector) {
    object Home : NavDestination(title = "Home", route = "home", icon = Icons.Filled.Home)
    object Episodes :
        NavDestination(title = "Episodes", route = "episodes", icon = Icons.Filled.PlayArrow)

    object Search : NavDestination(title = "Search", route = "search", icon = Icons.Filled.Search)
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
                                        selectedIndex = index
                                        navController.navigate(screen.route) {
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
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                onCharacterSelected = { navController.navigate("characterDetails/${it}") },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable("episodes") {
                            AllEpisodesScreen(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(
                            "characterDetails/{characterId}",
                            arguments = listOf(navArgument("characterId") {
                                type = NavType.IntType
                            })
                        ) {backStackEntry ->
                            val characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
                            CharacterDetailsScreen(
                                characterId = characterId,
                                onBackClicked = navController::popBackStack,
                                onEpisodeClicked = { navController.navigate("characterEpisodes/${characterId}") },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(
                            "characterEpisodes/{characterId}",
                            arguments = listOf(navArgument("characterId") {
                                type = NavType.IntType
                            })
                        ) {
                            val characterId = it.arguments?.getInt("characterId") ?: -1
                            CharacterEpisodesScreen(
                                characterId = characterId,
                                onBackClicked = navController::popBackStack,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
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