package com.example.indexcards.ui.box

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.indexcards.data.LanguageData
import com.example.indexcards.utils.box.BoxState
import com.example.indexcards.utils.card.CardState

@Composable
fun WordField(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = cardUiState.cardDetails.word,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "Word*") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    )
}

@Composable
fun MeaningField(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = cardUiState.cardDetails.meaning,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "Meaning*") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    )
}

@Composable
fun NotesField(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = cardUiState.cardDetails.notes,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "Notes") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    )
}

@Composable
fun NameField(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = boxUiState.boxDetails.name,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "Name*") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
    )
}

@Composable
fun DescriptionField(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = boxUiState.boxDetails.description,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "Description") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    )
}

@Composable
fun TopicField(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = boxUiState.boxDetails.topic,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "Topic*") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropDownMenu(
    modifier: Modifier = Modifier,
    boxUiState: BoxState,
    onValueChange: (String) -> Unit,
) {
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

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { changeExpanded() }
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            readOnly = true,
            value = boxUiState.boxDetails.topic,
            label = { Text(text = "Language*") },
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            modifier = modifier.fillMaxWidth(),
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
                                modifier = Modifier
                                    .size(AssistChipDefaults.IconSize),
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(text = option.value)
                        }
                    },
                    onClick = {
                        onValueChange(option.value)
                        changeExpanded()
                    }
                )
            }
        }
    }
}

@Composable
fun IsLanguageRadioButton(
    modifier: Modifier = Modifier,
    isLanguage: Boolean,
    changeIsLanguage: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .selectable(
                selected = isLanguage,
                role = Role.RadioButton,
                onClick = { changeIsLanguage() }
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
