package com.example.indexcards.ui.box

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.utils.box.HomeScreenViewModel
import com.example.indexcards.utils.AppViewModelProvider

@Composable
fun DeleteBoxDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    deleteBox: () -> Unit,
    homeScreenViewModel: HomeScreenViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val context = LocalContext.current
    var nameOfToBeDeleted by remember { mutableStateOf("")}

    nameOfToBeDeleted = if (homeScreenViewModel.boxToBeDeleted != null) {
        " " + homeScreenViewModel.boxToBeDeleted!!.name
    } else {
        ""
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton =
        {
            TextButton(
//                onClick = deleteBox
                onClick = {
                    Toast.makeText(context, homeScreenViewModel.boxToBeDeleted?.name, Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton =
        {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        },
        text = { Text(text = "Are you sure you want to delete the box$nameOfToBeDeleted ?") },
        title = { Text(text = "Delete this Box") }
    )
}