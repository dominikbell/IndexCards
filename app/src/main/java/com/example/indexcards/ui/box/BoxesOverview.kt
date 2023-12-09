package com.example.indexcards.ui.box

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.indexcards.data.AppDatabase
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

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BoxesOverviewTopBar {
                modifier
                navigateToBoxScreen()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /* TODO: Dialog to create new box */
                    Toast.makeText(context, "Adding new box ...", Toast.LENGTH_SHORT).show()
                    dialog = true
                },
                modifier = modifier
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /* TODO: Iterate over all boxes here */
        }
    }

    if (dialog) {
        AddBoxDialog(
            boxViewModel = boxViewModel,
            onDismiss = { dialog = false }
        )
    }
}