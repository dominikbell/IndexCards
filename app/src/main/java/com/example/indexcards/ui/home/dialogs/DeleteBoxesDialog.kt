package com.example.indexcards.ui.home.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.utils.state.emptyBox


@Composable
fun DeleteBoxesDialog(
    modifier: Modifier = Modifier,
    boxesToBeDeleted: List<Box>,
    onDismiss: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.delete_boxes)) },
        text = {
            Column(modifier = modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.delete_boxes_names))
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    for (box in boxesToBeDeleted) {
                        Text(
                            text = "\u2022 " + box.name,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
                    text = stringResource(id = R.string.action_cannot_be_undone),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(text = stringResource(R.string.delete_boxes_sure))
            }
        },
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
fun DeleteBoxesDialogPreview() {
    DeleteBoxesDialog(
        boxesToBeDeleted = listOf(
            emptyBox.copy(name = "Box1234"),
            emptyBox.copy(name = "Another one")
        )
    )
}