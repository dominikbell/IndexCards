package com.example.indexcards.ui.tag

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.tag.EditTagViewModel
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.launch

@Composable
fun TagDialog(
    modifier: Modifier = Modifier,
    hideDialog: () -> Unit,
    editTagViewModel: EditTagViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    )
) {
    val tagUiState = editTagViewModel.tagUiState
    val controller = rememberColorPickerController()

    fun onDismiss() {
        hideDialog()
        editTagViewModel.resetUiState()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Add a tag") },
        text = {
            Column(
                modifier = modifier.padding(10.dp)
            ) {
                /* TODO: changing color resets name field - Why? */
                OutlinedTextField(
                    value = editTagViewModel.tagUiState.tagDetails.text,
                    label = {
                        Text(text = "New Tag")
                    },
                    onValueChange = { editTagViewModel.updateUiState(tagUiState.tagDetails.copy(text = it)) }
                )

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
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        editTagViewModel.updateUiState(tagUiState.tagDetails.copy(color = colorEnvelope.hexCode))
                    }
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    editTagViewModel.viewModelScope.launch {
                        editTagViewModel.saveTag()
                    }
                    onDismiss()
                }) {
                Text(text = "Save")
            }
        },

        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        }
    )
}