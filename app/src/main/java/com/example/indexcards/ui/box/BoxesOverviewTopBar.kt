package com.example.indexcards.ui.box

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxesOverviewTopBar(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: () -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

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
                onDismissRequest = { expanded = false },
                modifier = modifier
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Settings")
                    },
                    onClick = {
                        /* TODO: Make settings screen and navigate there */
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
}