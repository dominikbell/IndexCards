package com.example.indexcards

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.indexcards.data.Card
import com.example.indexcards.ui.box.BoxScreen
import com.example.indexcards.ui.home.HomeScreen
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.home.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    homeScreenViewModel: HomeScreenViewModel,
    startBoxId: Long = -1,
    startLevel: Int = -1,
    hasNotificationPermission: Boolean = false,
    hasRecordingPermission: Boolean = false,
    requestNotificationPermission: () -> Boolean = { false },
    requestRecordingPermission: () -> Boolean = { false },
    deleteAllMemos: (List<Card>) -> Unit = {},
    cancelAllNotifications: () -> Unit = {},
    scheduleNotification: (Long, Int, String, Long) -> Unit = { _, _, _, _ -> },
) {
    var currentBoxId by remember { mutableLongStateOf(startBoxId) }

    fun setNewBoxId(newBoxId: Long) {
        currentBoxId = newBoxId
    }

    NavHost(
        navController = navController,
        startDestination =
        if (startBoxId == (-1).toLong()) {
            "homeScreen"
        } else {
            "boxScreen/${startBoxId}/${startLevel}"
        }
    ) {
        composable("homeScreen") {
            HomeScreen(
                navigateToBoxScreen = { boxId ->
                    setNewBoxId(boxId)
                    navController.navigate("boxScreen/${boxId}/${-1}")
                },
                hasNotificationPermission = hasNotificationPermission,
                requestNotificationPermission = requestNotificationPermission,
                cancelAllNotifications = cancelAllNotifications,
                scheduleNotification = scheduleNotification,
                homeScreenViewModel = homeScreenViewModel,
            )
        }
        composable(
            "boxScreen/{boxId}/{level}",
            arguments = listOf(
                navArgument("boxId") {
                    type = NavType.LongType
                    defaultValue = startBoxId
                },
                navArgument("level") {
                    type = NavType.IntType
                    defaultValue = startLevel
                }
            )
        ) {
            val boxId = it.arguments?.getLong("boxId") ?: -1
            val level = it.arguments?.getInt("level") ?: -1
            BoxScreen(
                navigateToBoxesOverview = {
                    navController.navigate("homeScreen")
                },
                boxId = boxId,
                startLevel = level,
                boxScreenViewModel = viewModel(
                    factory = ViewModelProvider(context = LocalContext.current).factory
                ) as BoxScreenViewModel,
                hasNotificationPermission = hasNotificationPermission,
                hasRecordingPermission = hasRecordingPermission,
                requestNotificationPermission = { requestNotificationPermission() },
                requestRecordingPermission = { requestRecordingPermission() },
                scheduleNotification = { lvl, name, time -> scheduleNotification(boxId, lvl, name, time) },
            )
        }
    }
}