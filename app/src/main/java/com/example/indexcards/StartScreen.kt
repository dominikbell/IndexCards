package com.example.indexcards

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    navigateToBoxesScreen: () -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.Top
        ),
        modifier = modifier
            .fillMaxSize()
            .background(Color(R.color.background_color))
    ) {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    text = "IndexCards",
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
                }
            },
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 20.dp,
                alignment = Alignment.CenterVertically
            ),
            modifier = modifier
                .padding(16.dp)
                .weight(1f),
        ) {
            Button(onClick = {
                navigateToBoxesScreen()
            }) {
                Text(text = "To Vocab Boxes")
            }
            Button(onClick = {
                /*TODO*/
                Toast.makeText(context, "This is not implemented yet!", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "See Statistics")
            }
        }
    }
}
