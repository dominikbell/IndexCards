package com.example.indexcards.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.data.LanguageData
import com.example.indexcards.utils.state.BoxState
import com.example.indexcards.utils.state.emptyBox
import com.example.indexcards.utils.state.toBoxDetails
import com.example.indexcards.utils.state.toBoxState
import com.example.indexcards.utils.state.CardState
import com.example.indexcards.utils.state.getImageId

@Composable
fun BoxNameWithFlag(
    box: Box,
    doBold: Boolean,
    isTitle: Boolean
) {
    val context = LocalContext.current
    val fontWeight = if (doBold) {
        FontWeight.Bold
    } else {
        FontWeight.Normal
    }

    val style = if(isTitle) {
        MaterialTheme.typography.titleLarge
    } else {
        LocalTextStyle.current
    }

    val imageId = box.getImageId(context)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = box.name,
            fontWeight = fontWeight,
            style = style
        )

        if (imageId != -1) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(AssistChipDefaults.IconSize * 1.4F),
            )
        }
    }
}

@Composable
fun NewTagButton(
    short: Boolean = false,
    onClick: () -> Unit = {},
) {
    val text = if (short) {
        stringResource(R.string.new_tag_short)
    } else {
        stringResource(R.string.new_tag)
    }
    Text(
        modifier = Modifier.clickable { onClick() },
        text = text
    )
}

@Composable
fun WordField(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    isError: Boolean,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        value = cardUiState.cardDetails.word,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.word) + "*") },
        isError = isError,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    )
}

@Composable
fun MeaningField(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    isLanguage: Boolean,
    isError: Boolean,
    onValueChange: (String) -> Unit = {},
) {
    val label =
        if (isLanguage) {
            stringResource(R.string.meaning)
        } else {
            stringResource(R.string.backside)
        }

    OutlinedTextField(
        value = cardUiState.cardDetails.meaning,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "$label*") },
        isError = isError,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    )
}

@Composable
fun NotesField(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        value = cardUiState.cardDetails.notes,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.notes)) },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    )
}

@Composable
fun RequiredFieldsText(
    modifier: Modifier = Modifier,
    plural: Boolean = true,
) {
    val text = if (plural) {
        stringResource(R.string.required_fields)
    } else {
        stringResource(R.string.required_field)
    }

    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Left
    )
}

@Composable
fun NameField(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    isError: Boolean,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = boxUiState.boxDetails.name,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.name) + "*") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        isError = isError
    )
}

@Preview(showBackground = true)
@Composable
fun NameFieldPreview() {
    NameField(
        boxUiState = emptyBox.copy(name = "Herbert").toBoxState(),
        isError = false
    )
}

@Composable
fun DescriptionField(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = boxUiState.boxDetails.description,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.description)) },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    )
}

@Composable
fun TopicField(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    isError: Boolean,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = boxUiState.boxDetails.topic,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.topic) + "*") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        isError = isError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropDownMenu(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    expanded: Boolean,
    isError: Boolean,
    changeExpanded: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
) {
    val context = LocalContext.current

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { changeExpanded() }
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            readOnly = true,
            value = boxUiState.boxDetails.topic,
            label = { Text(text = stringResource(R.string.language) + "*") },
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            isError = isError
        )
        ExposedDropdownMenu(
            modifier = modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { changeExpanded() }) {
            LanguageData.language.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = getImageId(context, option.key)),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(AssistChipDefaults.IconSize),
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(text = option.value.first)
                        }
                    },
                    onClick = {
                        onValueChange(option.value.first)
                        changeExpanded()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageDropDownMenuPreview() {
    LanguageDropDownMenu(
        expanded = true,
        isError = false,
        boxUiState = BoxState(boxDetails = emptyBox.copy(topic = "English").toBoxDetails()),
    )
}

@Composable
fun RemindersSwitch(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    hasNotificationPermission: Boolean = false,
    onCheckedChange: () -> Unit = {},
    requestNotificationPermission: () -> Boolean = { false }
) {
    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.reminders))

        Switch(
            checked = enabled,
            onCheckedChange = {
                if (!hasNotificationPermission) {
                    val success = requestNotificationPermission()
                    if (success) {
                        onCheckedChange()
                    }
                } else {
                    onCheckedChange()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RemindersSwitchPreview() {
    RemindersSwitch()
}

@Composable
fun IsLanguageCheckBox(
    modifier: Modifier = Modifier,
    isLanguage: Boolean,
    changeIsLanguage: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isLanguage,
                role = Role.RadioButton,
                onClick = { changeIsLanguage() }
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isLanguage,
            onCheckedChange = { changeIsLanguage() }
        )
        Text(
            modifier = modifier.padding(start = 6.dp),
            text = stringResource(R.string.is_language)
        )
    }
}
