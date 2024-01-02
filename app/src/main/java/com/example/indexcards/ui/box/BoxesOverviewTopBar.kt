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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.indexcards.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxesOverviewTopBar(
    modifier: Modifier = Modifier,
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
                text = stringResource(R.string.your_boxes),
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
                    contentDescription = "Menu"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = modifier
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.settings))
                    },
                    onClick = {
                        /* TODO: Make settings screen and navigate there */
                        expanded = false
                        Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT).show()
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.statistics))
                    },
                    onClick = {
                        /* TODO: Show statistics */
                        expanded = false
                        Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT).show()
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.about_app))
                    },
                    onClick = {
                        /* TODO: Show information card */
                        expanded = false
                        Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        },
    )
}