package com.example.indexcards

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    navigateToBoxesScreen: () -> Unit
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.Top
        ),
        modifier = modifier
            .fillMaxSize()
            .background(Color(R.color.white))
//            .background(Color(R.color.background_color))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "IndexCards",
                    color = Color(R.color.black),
                    modifier = modifier
//                        .background(Color(R.color.dark_highlight))
//                        .fillMaxSize()
                )
            },
            actions = {
                IconButton(onClick = {
                    /* TODO */
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }
            },

            )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
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
            }) {
                Text(text = "See Statistics")
            }
        }
        Row {
            Button(onClick = {
                /*TODO*/
            }) {
                Text(text = "Quit App")
            }
            Spacer(
                modifier = modifier
                    .size(50.dp)
            )
            Button(onClick = {
                Toast.makeText(context, "This is not implemented yet!", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "About this App")
            }
        }
        Spacer(
            modifier = modifier
                .weight(0.1f)
        )
    }
}
