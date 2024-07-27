package com.example.indexcards.ui.box.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.utils.state.emptyBox

@Composable
fun DeleteBoxDialog(
    modifier: Modifier = Modifier,
    boxToBeDeleted: Box,
    onDismiss: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        text = { Text(text = stringResource(R.string.delete_box_sure)) },
        title = { Text(text = stringResource(R.string.delete_box) + " '${boxToBeDeleted.name}'") },
        dismissButton =
        {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        confirmButton =
        {
            TextButton(
                onClick = onDelete
            ) {
                Text(text = stringResource(R.string.delete))
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteBoxDialogPreview() {
    DeleteBoxDialog(
        boxToBeDeleted = emptyBox.copy(name = "Box1234")
    )
}