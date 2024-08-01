package com.example.indexcards.ui.box

import android.util.Log
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.indexcards.R
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.utils.state.emptyCard
import kotlinx.coroutines.delay
import kotlin.math.min

@Composable
fun TrainingScreen(
    modifier: Modifier = Modifier,
    cardList: List<CardWithTags>,
    trainingCounts: Boolean,
    trainingDirection: Boolean,
    navigateToBoxScreen: () -> Unit = {},
    onCardCorrect: (Card) -> Unit = {},
    onCardIncorrect: (Card) -> Unit = {},
    setRemindersAfterTraining: () -> Unit = {},
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
        val cardHeight = (0.5 * LocalConfiguration.current.screenHeightDp).dp
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
            val currentCard = cardList[trainedCards]

            CardCard(
                modifier = modifier
                    .height(cardHeight)
                    .width(cardWidth),
                currentCard = currentCard,
                cardHeight = cardHeight,
                cardWidth = cardWidth,
                trainingCounts = trainingCounts,
                trainingDirection = trainingDirection,
                turnedOver = turnedOver,
                turnOver = { turnedOver = !turnedOver },
                goToNextCard = { goToNextCard() },
                onCardCorrect = { onCardCorrect(currentCard.card) },
                onCardIncorrect = { onCardIncorrect(currentCard.card) },
            )

        } else {
            AlertDialog(
                onDismissRequest = { navigateToBoxScreen() },
                title = { Text(text = stringResource(id = R.string.all_done)) },
                text = { Text(text = stringResource(id = R.string.no_more_cards)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            setRemindersAfterTraining()
                            navigateToBoxScreen()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.back_to_box))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun TrainingScreenPreview() {
    TrainingScreen(
        cardList = listOf(
            CardWithTags(
                card = emptyCard.copy(word = "Hallo", meaning = "Hello"),
                tags = listOf()
            ),
            CardWithTags(card = emptyCard, tags = listOf()),
            CardWithTags(card = emptyCard, tags = listOf()),
            CardWithTags(card = emptyCard, tags = listOf()),
        ),
        trainingCounts = true,
        trainingDirection = true,
    )
}

@Composable
fun CardCard(
    modifier: Modifier,
    currentCard: CardWithTags,
    cardHeight: Dp,
    cardWidth: Dp,
    turnedOver: Boolean,
    trainingCounts: Boolean,
    trainingDirection: Boolean,
    turnOver: () -> Unit = {},
    goToNextCard: () -> Unit = {},
    onCardCorrect: () -> Unit = {},
    onCardIncorrect: () -> Unit = {},
) {
    val wordSearched =
        if (trainingDirection) {
            currentCard.card.word
        } else {
            currentCard.card.meaning
        }

    val wordSolution =
        if (trainingDirection) {
            currentCard.card.meaning
        } else {
            currentCard.card.word
        }

    val cardPadding = 30.dp

    Card(
        modifier = modifier
            .clip(CardDefaults.shape)
            .height(cardHeight)
            .width(cardWidth)
            .clickable {
                if (!turnedOver) {
                    turnOver()
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(30.dp),
            ) {
                SelectionContainer(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = wordSearched,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                if (!turnedOver) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = "?",
                            fontSize = 100.sp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SelectionContainer(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = wordSolution,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        }

                        Spacer(modifier = Modifier.size(8.dp))

                        TagList(
                            tagList = currentCard.tags,
                            onClick = {},
                            onLongClick = {},
                            selectedTags = currentCard.tags
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        if (currentCard.card.notes.isNotEmpty()) {
                            Row {
                                Text(
                                    text = stringResource(R.string.notes) + ": ",
                                    fontStyle = FontStyle.Italic
                                )
                                Text(text = currentCard.card.notes)
                            }
                        }
                    }
                }
            }

            if (!turnedOver) {
                Text(
                    modifier = Modifier.padding(bottom = cardPadding),
                    text = stringResource(id = R.string.turn_over)
                )
            } else {
                CardButtons(
                    trainingCounts = trainingCounts,
                    goToNextCard = goToNextCard,
                    onCardCorrect = onCardCorrect,
                    onCardIncorrect = onCardIncorrect,
                )
            }
        }
    }
}

@Composable
fun CardButtons(
    modifier: Modifier = Modifier,
    trainingCounts: Boolean,
    goToNextCard: () -> Unit = {},
    onCardCorrect: () -> Unit = {},
    onCardIncorrect: () -> Unit = {},
) {
    val height = 70.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        HorizontalDivider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .weight(1f)
                    .clickable {
                        goToNextCard()
                        if (trainingCounts) {
                            onCardIncorrect()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.incorrect),
                    fontWeight = FontWeight(550),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )
            }

            VerticalDivider(modifier = Modifier.height(height))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .weight(1f)
                    .clickable {
                        goToNextCard()
                        if (trainingCounts) {
                            onCardCorrect()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.correct),
                    fontWeight = FontWeight(550),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


@Preview
@Composable
fun CardCardPreviewHidden() {
    CardCard(
        modifier = Modifier,
        currentCard =
        CardWithTags(
            card = emptyCard.copy(word = "Hallo", meaning = "Hello"),
            tags = listOf()
        ),
        cardHeight = 400.dp,
        cardWidth = 300.dp,
        turnedOver = false,
        trainingCounts = true,
        trainingDirection = true,
    )
}

@Preview
@Composable
fun CardCardPreviewRevealed() {
    CardCard(
        modifier = Modifier,
        currentCard =
        CardWithTags(
            card = emptyCard.copy(word = "Hallo", meaning = "Hello", notes = "Very important card"),
            tags = listOf()
        ),
        cardHeight = 400.dp,
        cardWidth = 300.dp,
        turnedOver = true,
        trainingCounts = true,
        trainingDirection = true,
    )
}