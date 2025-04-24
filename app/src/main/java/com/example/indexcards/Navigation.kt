package com.example.indexcards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
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
    importBox: () -> Unit = {},
    saveFile: (ByteArray, String) -> Unit = { _, _ -> },
    deleteAllMemos: (List<Card>) -> Unit = {},
    cancelAllNotifications: () -> Unit = {},
    cancelNotification: (Long, Int) -> Unit = { _, _ -> },
    scheduleNotification: (Long, Int, String, Long, Long) -> Unit = { _, _, _, _, _ -> },
) {
    var currentBoxId by remember { mutableLongStateOf(startBoxId) }
    var tutorial by remember { mutableStateOf(false) }

    fun setNewBoxId(newBoxId: Long) {
        currentBoxId = newBoxId
    }

    fun setTutorial(newTut: Boolean) {
        tutorial = newTut
    }

    NavHost(
        navController = navController,
        startDestination =
        if (startBoxId == (-1).toLong()) {
            "homeScreen"
        } else {
            "boxScreen/${startBoxId}/${startLevel}/false"
        }
    ) {
        composable("homeScreen") {
            HomeScreen(
                navigateToBoxScreen = { boxId, tut ->
                    setNewBoxId(boxId)
                    setTutorial(tut)
                    navController.navigate("boxScreen/${boxId}/${-1}/${tut}")
                },
                hasNotificationPermission = hasNotificationPermission,
                requestNotificationPermission = requestNotificationPermission,
                deleteAllMemos = deleteAllMemos,
                cancelAllNotifications = cancelAllNotifications,
                scheduleNotification = scheduleNotification,
                homeScreenViewModel = homeScreenViewModel,
                importBox = importBox,
            )
        }
        composable(
            "boxScreen/{boxId}/{level}/{tutorial}",
            arguments = listOf(
                navArgument("boxId") {
                    type = NavType.LongType
                    defaultValue = startBoxId
                },
                navArgument("level") {
                    type = NavType.IntType
                    defaultValue = startLevel
                },
                navArgument("tutorial") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            val boxId = it.arguments?.getLong("boxId") ?: (-1).toLong()
            val level = it.arguments?.getInt("level") ?: -1
            val tut = it.arguments?.getBoolean("tutorial") == true
            BoxScreen(
                navigateToBoxesOverview = {
                    navController.navigate("homeScreen")
                },
                boxId = boxId,
                startLevel = level,
                tutorialGiven = tut,
                boxScreenViewModel = viewModel(
                    factory = ViewModelProvider(context = LocalContext.current).factory
                ) as BoxScreenViewModel,
                hasNotificationPermission = hasNotificationPermission,
                hasRecordingPermission = hasRecordingPermission,
                requestNotificationPermission = { requestNotificationPermission() },
                requestRecordingPermission = { requestRecordingPermission() },
                deleteAllMemos = deleteAllMemos,
                cancelNotification = { lvl -> cancelNotification(boxId, lvl) },
                scheduleNotification = { lvl, name, trigger, repeat ->
                    scheduleNotification(boxId, lvl, name, trigger, repeat)
                },
                saveFile = saveFile,
            )
        }
    }
}