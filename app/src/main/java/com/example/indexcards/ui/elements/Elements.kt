package com.example.indexcards.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Checkbox
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
import com.example.indexcards.utils.state.BoxState
import com.example.indexcards.utils.state.emptyBox
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

    val style = if (isTitle) {
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
    isLanguage: Boolean,
    isError: Boolean,
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    val label = if (isLanguage) {
        stringResource(R.string.word)
    } else {
        stringResource(R.string.frontside)
    }
    OutlinedTextField(
        value = cardUiState.cardDetails.word,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "$label*") },
        isError = isError,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        enabled = isEnabled,
    )
}

@Composable
fun MeaningField(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    isLanguage: Boolean,
    isError: Boolean,
    isEnabled: Boolean = true,
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
        enabled = isEnabled,
    )
}

@Composable
fun NotesField(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        value = cardUiState.cardDetails.notes,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.notes)) },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        enabled = isEnabled,
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
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = boxUiState.boxDetails.name,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.name) + "*") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        isError = isError,
        enabled = isEnabled,
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
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = boxUiState.boxDetails.description,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.description)) },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        enabled = isEnabled,
    )
}

@Composable
fun TopicField(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    isError: Boolean,
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = boxUiState.boxDetails.topic,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(R.string.topic) + "*") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        isError = isError
    )
}

@Composable
fun RemindersSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    hasNotificationPermission: Boolean = false,
    isEnabled: Boolean = true,
    onCheckedChange: () -> Unit = {},
    requestNotificationPermission: () -> Boolean = { false }
) {
    fun onClick() {
        if (!hasNotificationPermission) {
            val success = requestNotificationPermission()
            if (success) {
                onCheckedChange()
            }
        } else {
            onCheckedChange()
        }
    }

    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.reminders))

        Switch(
            checked = checked,
            onCheckedChange = { onClick() },
            enabled = isEnabled,
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
    isEnabled: Boolean = true,
    changeIsLanguage: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isLanguage,
                role = Role.RadioButton,
                onClick = {
                    if (isEnabled) {
                        changeIsLanguage()
                    }
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isLanguage,
            onCheckedChange = { changeIsLanguage() },
            enabled = isEnabled,
        )
        Text(
            modifier = modifier.padding(start = 6.dp),
            text = stringResource(R.string.is_language)
        )
    }
}
