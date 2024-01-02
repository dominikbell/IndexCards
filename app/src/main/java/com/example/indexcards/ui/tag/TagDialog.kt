package com.example.indexcards.ui.tag

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.home.RequiredFieldsText
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.tag.EditTagViewModel
import com.example.indexcards.utils.tag.toColor
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.launch

@Composable
fun TagDialog(
    modifier: Modifier = Modifier,
    newTag: Boolean,
    onDismiss: () -> Unit,
    editTagViewModel: EditTagViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    )
) {
    val tagUiState = editTagViewModel.tagUiState
    val controller = rememberColorPickerController()
    val titleText = if (newTag) {
        "Add a tag"
    } else {
        "Edit tag"
    }

    val initialColor = editTagViewModel.colorUiState.toColor()

    fun newOnDismiss() {
        onDismiss()
        editTagViewModel.resetUiState()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { newOnDismiss() },
        title = { Text(text = titleText) },
        text = {
            Column(
                modifier = modifier.padding(10.dp)
            ) {
                OutlinedTextField(
                    value = tagUiState.tagDetails.text,
                    label = { Text(text = "Tag Name*") },
                    onValueChange = {
                        editTagViewModel.updateUiState(
                            tagUiState.tagDetails.copy(text = it)
                        )
                    }
                )

                RequiredFieldsText(plural = false)

                AlphaTile(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    controller = controller,
                )

                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    controller = controller,
                    /* TODO: initial color is still white .. */
                    initialColor = initialColor,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        /* Hot fix for bug that resets text field */
                        editTagViewModel.setColor('#' + colorEnvelope.hexCode)
                    },
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    if (newTag) {
                        editTagViewModel.viewModelScope.launch {
                            editTagViewModel.saveTag()
                        }
                    } else {
                        editTagViewModel.viewModelScope.launch {
                            editTagViewModel.updateTag()
                        }
                    }
                    newOnDismiss()
                }) {
                Text(text = "Save")
            }
        },

        dismissButton = {
            Row {
                TextButton(onClick = { newOnDismiss() }) {
                    Text(text = "Cancel")
                }
                if (!newTag) {
                    TextButton(onClick = {
                        newOnDismiss()
                        editTagViewModel.viewModelScope.launch {
                            editTagViewModel.deleteTag()
                        }
                    }) {
                        Text(text = "Delete")
                    }
                }
            }
        }
    )
}