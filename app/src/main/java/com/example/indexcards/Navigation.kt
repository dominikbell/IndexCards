package com.example.indexcards

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                {}
//                navigateToBoxScreen = { navController.navigate("boxScreen") }
            )
        }
//        composable("boxScreen") {
//            BoxScreen(
//                navigateToBoxesOverview = {navController.navigate("boxesOverview")}
//            )
//        }
    }
}