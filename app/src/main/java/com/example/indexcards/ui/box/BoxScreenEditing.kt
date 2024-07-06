package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.R
import com.example.indexcards.data.isLanguage
import com.example.indexcards.ui.elements.DescriptionField
import com.example.indexcards.ui.elements.LanguageDropDownMenu
import com.example.indexcards.ui.elements.NameField
import com.example.indexcards.ui.elements.RemindersSwitch
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.ui.elements.TopicField
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.box.toBox

@Composable
fun BoxScreenEditing(
    modifier: Modifier = Modifier,
    boxScreenViewModel: BoxScreenViewModel,
    onSave: () -> Unit,
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Unit = {}
) {
    val boxUiState = boxScreenViewModel.boxUiState
    val isLanguage = boxUiState.boxDetails.toBox().isLanguage()
    val isEnabled = boxUiState.boxDetails.reminders

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NameField(
            modifier = Modifier.fillMaxWidth(),
            boxUiState = boxUiState,
            onValueChange = { boxScreenViewModel.updateUiState(boxUiState.boxDetails.copy(name = it)) }
        )
        if (isLanguage) {
            LanguageDropDownMenu(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                onValueChange = {
                    boxScreenViewModel.updateUiState(boxUiState.boxDetails.copy(topic = it))
                }
            )
        } else {
            TopicField(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                onValueChange = {
                    boxScreenViewModel.updateUiState(boxUiState.boxDetails.copy(topic = it))
                }
            )
        }

        DescriptionField(
            modifier = Modifier.fillMaxWidth(),
            boxUiState = boxUiState,
            onValueChange = {
                boxScreenViewModel.updateUiState(boxUiState.boxDetails.copy(description = it))
            }
        )

        RequiredFieldsText()

        Spacer(modifier = Modifier.size(8.dp))

        RemindersSwitch(
            modifier = modifier,
            enabled = (isEnabled && hasNotificationPermission),
            onCheckedChange = {
                boxScreenViewModel.updateUiState(boxUiState.boxDetails.copy(reminders = !isEnabled))
            },
            hasNotificationPermission = hasNotificationPermission,
            requestNotificationPermission = { requestNotificationPermission() }
        )

        Button(
            onClick = {
                /* TODO: Only save valid entries -> BoxState.isValid */
                onSave()
            }
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@Preview
@Composable
fun BoxScreenEditingPreview() {
    BoxScreenEditing(
        boxScreenViewModel = viewModel(
            factory = ViewModelProvider(context = LocalContext.current).factory
        ),
        onSave = { }
    )
}