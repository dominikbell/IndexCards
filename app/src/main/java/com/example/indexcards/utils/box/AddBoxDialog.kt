package com.example.indexcards.utils.box

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.LanguageData
import com.example.indexcards.utils.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBoxDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    addBoxViewModel: AddBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    )
) {
    val addBoxUiState = addBoxViewModel.boxUiState

    fun newOnDismiss() {
        onDismiss()
        addBoxViewModel.updateUiState(
            BoxDetails(
                id = 0,
                name = "",
                topic = "",
                description = "",
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()

    var isLanguage by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    fun changeExpanded() {
        expanded = !expanded
    }

    @SuppressLint("DiscouragedApi")
    fun getImageId(nameBase: String): Int {
        return context.resources.getIdentifier(
            "flag$nameBase",
            "drawable",
            context.packageName
        )
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { },
        title = {
            Text(
                text = "Add a new box",
            )
        },
        text = {
            Column(
                modifier = modifier
            ) {
                OutlinedTextField(
                    value = addBoxUiState.boxDetails.name,
                    onValueChange = {
                        addBoxViewModel.updateUiState(addBoxUiState.boxDetails.copy(name = it))
                    },
                    label = { Text(text = "Name*") },
                )

                if (!isLanguage) {
                    OutlinedTextField(
                        value = addBoxUiState.boxDetails.topic,
                        onValueChange = {
                            addBoxViewModel.updateUiState(addBoxUiState.boxDetails.copy(topic = it))
                        },
                        label = { Text(text = "Topic*") },
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { changeExpanded() }
                    ) {
                        OutlinedTextField(
                            modifier = modifier.menuAnchor(),
                            readOnly = true,
                            value = addBoxUiState.boxDetails.topic,
                            label = { Text(text = "Language*") },
                            onValueChange = {
                                addBoxViewModel.updateUiState(addBoxUiState.boxDetails.copy(topic = it))
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { changeExpanded() }) {
                            LanguageData.language.entries.forEach() { option ->

                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = modifier
                                                .fillMaxWidth()
                                        ) {
                                            Image(
                                                painter = painterResource(
                                                    id = getImageId(option.key)
                                                ),
                                                contentDescription = null,
                                                modifier = modifier
                                                    .size(AssistChipDefaults.IconSize),
                                            )
                                            Spacer(modifier = modifier.size(6.dp))
                                            Text(text = option.value)

                                        }
                                    },
                                    onClick = {
                                        addBoxViewModel.updateUiState(
                                            addBoxUiState.boxDetails.copy(
                                                topic = option.value
                                            )
                                        )
                                        changeExpanded()
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    modifier = modifier,
                    value = addBoxUiState.boxDetails.description,
                    onValueChange = {
                        addBoxViewModel.updateUiState(addBoxUiState.boxDetails.copy(description = it))
                    },
                    label = { Text(text = "Description") },
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .selectable(
                            selected = isLanguage,
                            role = Role.RadioButton,
                            onClick = {
                                isLanguage = !isLanguage
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isLanguage,
                        onClick = null
                    )
                    Text(
                        modifier = modifier
                            .padding(6.dp),
                        text = "Adding a language box"
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                coroutineScope.launch {
                    addBoxViewModel.saveItem()
                    newOnDismiss()
                }
            }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                newOnDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    )
}