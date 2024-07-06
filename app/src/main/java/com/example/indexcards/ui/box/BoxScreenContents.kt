package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.R
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.ui.card.CardList
import com.example.indexcards.ui.elements.LevelList
import com.example.indexcards.ui.home.NewTagButton
import com.example.indexcards.ui.tag.TagList
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.box.UiCardsWithTags

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
    val tagWithCards by boxScreenViewModel.tagWithCards.collectAsState()

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
                    if (tagWithCards.tag == it) {
                        boxScreenViewModel.resetTagSortedBy()
                    } else {
                        boxScreenViewModel.setTagSortedBy(it)
                    }
                },
                onLongClick = { showEditTagDialog() },
                selectedTags = listOf(tagWithCards.tag)
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

@Preview
@Composable
fun BoxScreenBodyPreview() {
    BoxScreenBody(
        showCard = { },
        showEditCardDialog = { },
        showNewTagDialog = { },
        showEditTagDialog = { },
        levelSelected = -1,
        boxScreenViewModel = viewModel(
            factory = ViewModelProvider(context = LocalContext.current).factory
        )
    )
}