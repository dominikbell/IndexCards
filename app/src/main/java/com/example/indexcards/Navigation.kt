package com.example.indexcards

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = "boxesOverview"
    ) {
        composable("boxesOverview") {
            BoxesOverview(
                navigateToBoxScreen = { navController.navigate("boxScreen") }
            )
        }
        composable("boxScreen") {
            BoxScreen(
                navigateToBoxesOverview = {navController.navigate("boxesOverview")}
            )
        }
    }
}