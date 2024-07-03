package com.example.indexcards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.indexcards.ui.box.BoxScreen
import com.example.indexcards.ui.home.HomeScreen
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.box.HomeScreenViewModel

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    homeScreenViewModel: HomeScreenViewModel,
    startDestination: String = "homeScreen",
    startBoxId: Long = -1,
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Unit = {}
) {
    var currentBoxId by remember { mutableLongStateOf(startBoxId) }

    fun setNewBoxId(newBoxId: Long) {
        currentBoxId = newBoxId
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("homeScreen") {
            HomeScreen(
                navigateToBoxScreen = { boxId ->
                    setNewBoxId(boxId)
                    navController.navigate("boxScreen/${boxId}")
                },
                homeScreenViewModel = homeScreenViewModel,
                hasNotificationPermission = hasNotificationPermission,
                requestNotificationPermission = { requestNotificationPermission() }
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
                navigateToBoxesOverview = {
                    navController.navigate("homeScreen")
                },
                boxId = boxId,
                boxScreenViewModel = viewModel(
                    factory = ViewModelProvider(context = LocalContext.current).factory
                ) as BoxScreenViewModel,
                hasNotificationPermission = hasNotificationPermission,
                requestNotificationPermission = { requestNotificationPermission() }
            )
        }
    }
}