package com.example.indexcards.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.box.BoxList
import com.example.indexcards.ui.box.BoxesOverviewTopBar
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.box.AddBoxDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToBoxScreen: () -> Unit,
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val homeScreenUiState by homeScreenViewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    var dialog by remember { mutableStateOf(false) }
//    val testList = mutableListOf<Box>()
//
//    for (i in 0..5) {
//        testList +=
//            Box(
//                i.toLong(),
//                "Norsk $i",
//                "Norwegian",
//                "Learning Norwegian!",
//                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
//            )
//    }


    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BoxesOverviewTopBar {
                navigateToBoxScreen()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    dialog = true
                },
                modifier = modifier
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        BoxList(
            modifier = modifier
                .padding(innerPadding),
            boxList = homeScreenUiState.boxList
        )
    }

    if (dialog) {
        AddBoxDialog(
            onDismiss = { dialog = false }
        )
    }
}