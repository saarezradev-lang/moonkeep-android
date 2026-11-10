package com.saarezra.moonkeep.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.saarezra.moonkeep.ui.screens.GameplayScreen
import com.saarezra.moonkeep.ui.screens.HomeScreen
import com.saarezra.moonkeep.ui.screens.LevelSelectScreen
import com.saarezra.moonkeep.ui.screens.ResultScreen
import com.saarezra.moonkeep.ui.screens.ShopScreen

object Routes {
    const val HOME = "home"
    const val LEVELS = "levels"
    const val GAME = "game/{level}"
    const val RESULT = "result/{level}/{score}/{won}"
    const val SHOP = "shop"

    fun game(level: Int) = "game/$level"
    fun result(level: Int, score: Int, won: Boolean) = "result/$level/$score/$won"
}

@Composable
fun MoonKeepApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onPlay = { navController.navigate(Routes.LEVELS) },
                onShop = { navController.navigate(Routes.SHOP) }
            )
        }
        composable(Routes.LEVELS) {
            LevelSelectScreen(
                onLevelSelected = { level -> navController.navigate(Routes.game(level)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.GAME) { backStackEntry ->
            val level = backStackEntry.arguments?.getString("level")?.toIntOrNull() ?: 1
            GameplayScreen(
                level = level,
                onFinished = { score, won ->
                    navController.navigate(Routes.result(level, score, won)) {
                        popUpTo(Routes.LEVELS)
                    }
                }
            )
        }
        composable(Routes.RESULT) { backStackEntry ->
            val args = backStackEntry.arguments
            val level = args?.getString("level")?.toIntOrNull() ?: 1
            val score = args?.getString("score")?.toIntOrNull() ?: 0
            val won = args?.getString("won")?.toBoolean() ?: false
            ResultScreen(
                level = level,
                score = score,
                won = won,
                onNext = {
                    navController.navigate(Routes.game(level + 1)) { popUpTo(Routes.LEVELS) }
                },
                onBackToLevels = {
                    navController.popBackStack(Routes.LEVELS, inclusive = false)
                }
            )
        }
        composable(Routes.SHOP) {
            ShopScreen(onBack = { navController.popBackStack() })
        }
    }
}
