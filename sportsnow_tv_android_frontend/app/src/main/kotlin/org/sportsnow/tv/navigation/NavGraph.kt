package org.sportsnow.tv.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.*
import org.sportsnow.tv.ui.details.DetailsScreen
import org.sportsnow.tv.ui.home.HomeScreen
import org.sportsnow.tv.ui.player.HighlightsPlayerScreen
import org.sportsnow.tv.data.Repository

/**
 * PUBLIC_INTERFACE
 * SportsNavGraph sets up the navigation routes for the TV app:
 * - home: list of matches
 * - details/{matchId}: match details
 * - player/{matchId}/{highlightIndex}: highlights player for a match
 */
@Composable
fun SportsNavGraph(startDestination: String = "home") {
    val navController = rememberNavController()
    val repo = remember { Repository() }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("home") {
            HomeScreen(
                repository = repo,
                onOpenMatch = { matchId ->
                    navController.navigate("details/$matchId")
                }
            )
        }
        composable(
            route = "details/{matchId}",
            arguments = listOf(navArgument("matchId") { type = NavType.StringType })
        ) { backStack ->
            val matchId = backStack.arguments?.getString("matchId").orEmpty()
            DetailsScreen(
                matchId = matchId,
                repository = repo,
                onPlayHighlights = { index ->
                    navController.navigate("player/$matchId/$index")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "player/{matchId}/{highlightIndex}",
            arguments = listOf(
                navArgument("matchId") { type = NavType.StringType },
                navArgument("highlightIndex") { type = NavType.IntType }
            )
        ) { backStack ->
            val matchId = backStack.arguments?.getString("matchId").orEmpty()
            val highlightIndex = backStack.arguments?.getInt("highlightIndex") ?: 0
            HighlightsPlayerScreen(
                matchId = matchId,
                startIndex = highlightIndex,
                repository = repo,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
