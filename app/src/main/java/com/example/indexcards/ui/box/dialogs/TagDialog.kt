package com.example.indexcards.ui.box.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.utils.tag.TagDetails
import com.example.indexcards.utils.tag.TagState
import com.example.indexcards.utils.tag.emptyTag
import com.example.indexcards.utils.tag.toTagDetails
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun TagDialog(
    modifier: Modifier = Modifier,
    newTag: Boolean,
    tagUiState: TagState,
    initialColor: Color,
    updateUiState: (TagDetails) -> Unit = {},
    setColor: (String) -> Unit = {},
    onDismiss: () -> Unit = {},
    saveTag: () -> Unit = {},
    updateTag: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    val controller = rememberColorPickerController()
    val titleText = if (newTag) {
        stringResource(R.string.new_tag)
    } else {
        stringResource(R.string.edit_tag)
    }

    var validText by remember { mutableStateOf(true) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        title = { Text(text = titleText) },
        text = {
            Column(
                modifier = modifier.padding(10.dp)
            ) {
                OutlinedTextField(
                    value = tagUiState.tagDetails.text,
                    label = { Text(text = stringResource(R.string.tag_name) + "*") },
                    isError = !validText,
                    onValueChange = {
                        validText = true
                        updateUiState(tagUiState.tagDetails.copy(text = it))
                    },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
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
                        setColor('#' + colorEnvelope.hexCode)
                    },
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    if (tagUiState.isValid) {
                        if (newTag) {
                            saveTag()
                        } else {
                            updateTag()
                        }
                    } else {
                        if (!tagUiState.validText) {
                            validText = false
                        }
                    }
                }) {
                Text(text = stringResource(R.string.save))
            }
        },

        dismissButton = {
            Row {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = stringResource(R.string.cancel))
                }
                if (!newTag) {
                    TextButton(onClick = {
                        onDelete()
                    }) {
                        Text(text = stringResource(R.string.delete))
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun NewTagDialogPreview() {
    TagDialog(
        newTag = true,
        initialColor = Color.Red,
        tagUiState = TagState(emptyTag.copy(text = "Tag123").toTagDetails())
    )
}

@Preview
@Composable
fun EditTagDialogPreview() {
    TagDialog(
        newTag = false,
        initialColor = Color.Red,
        tagUiState = TagState(emptyTag.copy(text = "Tag123").toTagDetails())
    )
}