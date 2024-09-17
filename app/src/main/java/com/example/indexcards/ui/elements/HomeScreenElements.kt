package com.example.indexcards.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddBoxButton(
    modifier: Modifier,
    onClick: () -> Unit = {},
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add")
    }
}