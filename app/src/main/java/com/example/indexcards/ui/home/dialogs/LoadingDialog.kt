package com.example.indexcards.ui.home.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R

@Composable
fun LoadingDialog(
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.loading))
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp)
                )

                Text(
                    text = stringResource(id = R.string.wait),
                    textAlign = TextAlign.Start
                )
            }
        },
        onDismissRequest = {},
        confirmButton = {},
    )
}

@Preview
@Composable
fun LoadingDialogPreview() {
    LoadingDialog()
}