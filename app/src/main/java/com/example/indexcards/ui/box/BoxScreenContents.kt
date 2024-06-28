package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.LanguageData
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.card.CardList
import com.example.indexcards.ui.elements.LevelList
import com.example.indexcards.ui.home.DescriptionField
import com.example.indexcards.ui.home.LanguageDropDownMenu
import com.example.indexcards.ui.home.NameField
import com.example.indexcards.ui.home.NewTagButton
import com.example.indexcards.ui.home.RequiredFieldsText
import com.example.indexcards.ui.home.TopicField
import com.example.indexcards.ui.tag.TagList
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.tag.emptyTag

@Composable
fun BoxScreenBody(
    modifier: Modifier = Modifier,
    showCard: () -> Unit,
    showEditCardDialog: () -> Unit,
    showCardDelete: () -> Unit,
    showNewTagDialog: () -> Unit,
    showEditTagDialog: () -> Unit,
    boxScreenViewModel: BoxScreenViewModel,
) {
    val tagSortedBy: State<Tag> = boxScreenViewModel.tagSortedBy.collectAsState()
    val boxWithTags = boxScreenViewModel.boxWithTags.collectAsState()
    val boxWithCards = boxScreenViewModel.boxWithCards.collectAsState()
    val tagWithCards = boxScreenViewModel.tagWithCards.collectAsState()
    val levelSelected = boxScreenViewModel.levelSelected.collectAsState()
    val cardsWithTags = boxScreenViewModel.cardsWithTags.collectAsState()

    val cardWithTagList =
        if (levelSelected.value == -1) {
            if (tagSortedBy.value == emptyTag) {
                cardsWithTags.value.cardWithTagList
            } else {
                cardsWithTags.value.cardWithTagList.filter { it.tags.contains(tagSortedBy.value) }
            }
        } else {
            if (tagSortedBy.value == emptyTag) {
                cardsWithTags.value.cardWithTagList.filter { it.card.level == levelSelected.value }
            } else {
                cardsWithTags.value.cardWithTagList.filter {
                    it.card.level == levelSelected.value && it.tags.contains(tagSortedBy.value)
                }
            }
        }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(R.string.description) + ": ${boxWithCards.value.box.description}",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(text = stringResource(R.string.nr_card) + ": ${boxWithCards.value.cardList.size}")

        Spacer(modifier = Modifier.size(4.dp))

        LevelList(
            boxWithCards = boxWithCards.value,
            currentLevel = levelSelected.value,
            selectLevel = { boxScreenViewModel.updateSelectedLevel(it) },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            TagList(
                modifier = Modifier.weight(1f),
                tagList = boxWithTags.value.tagList,
                onClick = {
                    if (tagWithCards.value.tag == emptyTag) {
                        boxScreenViewModel.setTagSortedBy(it)
                    } else {
                        if (tagWithCards.value.tag == it) {
                            boxScreenViewModel.resetTagSortedBy()
                        } else {
                            boxScreenViewModel.setTagSortedBy(it)
                        }
                    }
                },
                onLongClick = { showEditTagDialog() },
                selectedTags = listOf(tagWithCards.value.tag)
            )

            NewTagButton(
                onClick = showNewTagDialog
            )
        }

        Spacer(modifier = Modifier.size(4.dp))

        if (boxWithCards.value.cardList.isEmpty()) {
            Text(
                text = stringResource(R.string.click_to_add_card),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            CardList(
                cardWithTagList = cardWithTagList,
                showDialog = showCard,
                showDelete = { showCardDelete() },
                showEditDialog = { showEditCardDialog() }
            )


        }
    }
}

@Composable
fun BoxScreenEditing(
    modifier: Modifier = Modifier,
    boxScreenViewModel: BoxScreenViewModel,
    onSave: () -> Unit,
) {
    val boxUiState = boxScreenViewModel.boxUiState
    val isLanguage = (boxUiState.boxDetails.topic in LanguageData.language.values)

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
                onValueChange = { boxScreenViewModel.updateUiState(boxUiState.boxDetails.copy(topic = it)) }
            )
        } else {
            TopicField(
                modifier = Modifier.fillMaxWidth(),
                boxUiState = boxUiState,
                onValueChange = { boxScreenViewModel.updateUiState(boxUiState.boxDetails.copy(topic = it)) }
            )
        }

        DescriptionField(
            modifier = Modifier.fillMaxWidth(),
            boxUiState = boxUiState,
            onValueChange = {
                boxScreenViewModel.updateUiState(
                    boxUiState.boxDetails.copy(
                        description = it
                    )
                )
            }
        )

        RequiredFieldsText()

        Spacer(modifier = Modifier.size(8.dp))

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