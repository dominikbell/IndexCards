package com.example.indexcards.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R

@Composable
fun AboutAppDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier,
        title = { Text(text = stringResource(id = R.string.about_app)) },
        text = {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 6.dp),
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(text = stringResource(id = R.string.about_app_text))
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = { },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}

@Preview
@Composable
fun AboutAppDialogPreview() {
    AboutAppDialog(
        modifier = Modifier,
        onDismiss = { }
    )
}