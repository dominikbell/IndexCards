package com.example.indexcards.ui.box

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
import androidx.room.Room
import com.example.indexcards.data.AppDatabase
import com.example.indexcards.data.Box
import com.example.indexcards.utils.box.AddBoxDialog
import com.example.indexcards.utils.box.BoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxesOverview(
    modifier: Modifier = Modifier,
    boxViewModel: BoxViewModel = viewModel(),
    navigateToBoxScreen: () -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val boxState by boxViewModel.state.collectAsState()

    val db by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cards.db"
        ).build()
    }

    var dialog by remember { mutableStateOf(false) }
    val testList = mutableListOf<Box>()

    for (i in 0..20) {
        testList +=
            Box(i.toLong(), "Norsk $i", "Norwegian", "Learning Norwegian!")
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
        ,
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
                .padding(innerPadding)
            ,
            boxList = testList
        )
    }

    if (dialog) {
        AddBoxDialog(
            boxViewModel = boxViewModel,
            onDismiss = { dialog = false }
        )
    }
}