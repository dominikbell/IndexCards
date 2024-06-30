package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.LanguageData
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
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.box.UiCardsWithTags
import kotlin.math.min

@Composable
fun BoxScreenBody(
    modifier: Modifier = Modifier,
    showCard: () -> Unit,
    showEditCardDialog: () -> Unit,
    showNewTagDialog: () -> Unit,
    showEditTagDialog: () -> Unit,
    levelSelected: Int,
    boxWithTags: UiBoxWithTags = UiBoxWithTags(),
    cardsWithTags: UiCardsWithTags = UiCardsWithTags(),
    filteredCardWithTagList: List<CardWithTags> = listOf(),
    boxScreenViewModel: BoxScreenViewModel,
) {
    val tagWithCards = boxScreenViewModel.tagWithCards.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(R.string.description) + ": ${boxWithTags.box.description}",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(text = stringResource(R.string.nr_card) + ": ${cardsWithTags.cardWithTagList.size}")

        Spacer(modifier = Modifier.size(4.dp))

        LevelList(
            cardWithTagList = cardsWithTags.cardWithTagList,
            currentLevel = levelSelected,
            selectLevel = { boxScreenViewModel.updateSelectedLevel(it) },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            TagList(
                modifier = Modifier.weight(1f),
                tagList = boxWithTags.tagList,
                onClick = {
                    if (tagWithCards.value.tag == it) {
                        boxScreenViewModel.resetTagSortedBy()
                    } else {
                        boxScreenViewModel.setTagSortedBy(it)
                    }
                },
                onLongClick = { showEditTagDialog() },
                selectedTags = listOf(tagWithCards.value.tag)
            )

            VerticalDivider(
                modifier = Modifier
                    .height(ButtonDefaults.MinHeight)
                    .padding(start = 3.dp, end = 3.dp)
            )

            NewTagButton(
                onClick = showNewTagDialog
            )
        }

        if (cardsWithTags.cardWithTagList.isEmpty()) {
            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = stringResource(R.string.click_to_add_card),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            CardList(
                cardWithTagList = filteredCardWithTagList,
                showDialog = showCard,
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


@Composable
fun TrainingScreen(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: () -> Unit,
    cardList: List<CardWithTags>,
    onCardCorrect: (Card) -> Unit,
    onCardIncorrect: (Card) -> Unit,
) {
    var trainedCards by remember { mutableIntStateOf(0) }
    var turnedOver by remember { mutableStateOf(false) }
    val numberOfShadowCards = 4

    fun goToNextCard() {
        turnedOver = false
        trainedCards += 1
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val cardHeight = (0.7 * LocalConfiguration.current.screenHeightDp).dp
        val cardWidth = (0.7 * LocalConfiguration.current.screenWidthDp).dp

        for (k in 1..min(numberOfShadowCards, cardList.size - trainedCards - 1)) {
            Card(
                modifier = modifier
                    .offset(x = (k * 10).dp, y = (-k * 10).dp)
                    .height(cardHeight)
                    .width(cardWidth),
                colors = CardDefaults.cardColors().copy(
                    containerColor = CardDefaults.cardColors().containerColor.copy(alpha = (0.8F - k * 0.15F))
                )
            ) { }
        }

        if (trainedCards < cardList.size) {
            val currentCard = cardList[trainedCards].card

            CardCard(
                modifier = modifier
                    .height(cardHeight)
                    .width(cardWidth),
                currentCard = currentCard,
                cardHeight = cardHeight,
                cardWidth = cardWidth,
                turnedOver = turnedOver,
                turnOver = { turnedOver = !turnedOver },
                goToNextCard = { goToNextCard() },
                onCardCorrect = { onCardCorrect(currentCard) },
                onCardIncorrect = { onCardIncorrect(currentCard) }
            )

        } else {
            AlertDialog(
                onDismissRequest = {
                    navigateToBoxScreen()
                },
                title = { Text(text = "All done!") },
                text = {
                    Text(text = "There are no more cards to train.")
                },
                confirmButton = {
                    TextButton(
                        onClick = { navigateToBoxScreen() }
                    ) {
                        Text(text = "Go Back to box")
                    }
                }
            )
        }
    }
}

@Composable
fun CardCard(
    modifier: Modifier,
    currentCard: Card,
    cardHeight: Dp,
    cardWidth: Dp,
    turnedOver: Boolean,
    turnOver: () -> Unit,
    goToNextCard: () -> Unit,
    onCardCorrect: () -> Unit,
    onCardIncorrect: () -> Unit,
) {
    Card(
        modifier = modifier
            .height(cardHeight)
            .width(cardWidth)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(0.5F),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = currentCard.word,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                )

                if (!turnedOver) {
                    Text(text = "X".repeat(currentCard.meaning.length))
                } else {
                    Column {
                        Text(text = currentCard.meaning)

                        Text(text = currentCard.notes)
                    }
                }
            }

            Row {
                if (!turnedOver) {
                    TextButton(
                        onClick = { turnOver() }
                    ) {
                        Text(text = "Show solution")
                    }
                } else {
                    TextButton(
                        onClick = {
                            goToNextCard()
                            onCardCorrect()
                        }
                    ) {
                        Text(text = "Correct")
                    }
                }

                if (turnedOver) {
                    TextButton(
                        onClick = {
                            goToNextCard()
                            onCardIncorrect()
                        }
                    ) {
                        Text(text = "Incorrect")
                    }
                }
            }
        }
    }
}