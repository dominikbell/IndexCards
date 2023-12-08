package com.example.indexcards

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.room.Room

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxesOverview(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: () -> Unit,
) {
    val context = LocalContext.current

    val db by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cards.db"
        ).build()
    }

    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.Top
        ),
        modifier = modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    text = "Your Boxes",
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                )
            },
            actions = {
                IconButton(
                    onClick = {
                        expanded = true
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Settings")
                        },
                        onClick = {
                            /* TODO: Navigate to settings screen */
                            expanded = false
                            Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
                        })

                    DropdownMenuItem(
                        text = {
                            Text(text = "Statistics")
                        },
                        onClick = {
                            /* TODO: Show statistics */
                            expanded = false
                            Toast.makeText(
                                context,
                                "This feature is not implemented yet!",
                                Toast.LENGTH_SHORT
                            ).show()
                        })

                    DropdownMenuItem(
                        text = {
                            Text(text = "About this App")
                        },
                        onClick = {
                            /* TODO: Show information card */
                            expanded = false
                            Toast.makeText(
                                context,
                                "This feature is not implemented yet!",
                                Toast.LENGTH_SHORT
                            ).show()
                        })

                    DropdownMenuItem(
                        text = {
                            Text(text = "Navigate to box screen")
                        },
                        onClick = {
                            expanded = false
                            navigateToBoxScreen()
                        })
                }
            },
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /* TODO: Iterate over all boxes here */
        }
        FloatingActionButton(
            onClick = {
                /* TODO: Dialog to create new box */
                Toast.makeText(context, "Adding new box ...", Toast.LENGTH_SHORT).show()
            }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}