package com.example.indexcards

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.indexcards.ui.box.BoxEditScreen
import com.example.indexcards.ui.box.BoxScreen
import com.example.indexcards.ui.home.HomeScreen

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = "homeScreen"
    ) {
        composable("homeScreen") {
            HomeScreen(
                navigateToBoxScreen = { navController.navigate("boxScreen/${it}") }
            )
        }
        composable(
            "boxScreen/{boxId}",
            arguments = listOf(
                navArgument("boxId") {
                    type = NavType.LongType
                }
            )
        ) {
            val boxId = it.arguments?.getLong("boxId") ?: 0
            BoxScreen(
                navigateToBoxesOverview = { navController.navigate("homeScreen") },
                navigateToBoxEditScreen = { navController.navigate("boxEditScreen") },
                boxId = boxId,
            )
        }
        composable("boxEditScreen") {
            BoxEditScreen(
                navigateToBoxScreen = { navController.navigate("boxScreen") }
            )
        }
    }
}