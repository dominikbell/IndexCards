package com.example.indexcards.ui.training

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Card
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.tag.emptyTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: (Long) -> Unit,
    boxId: Long,
    boxScreenViewModel: BoxScreenViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val boxWithCards = boxScreenViewModel.boxWithCards.collectAsState()
    val tagWithCards = boxScreenViewModel.tagWithCards.collectAsState()
    val levelSelected = boxScreenViewModel.levelSelected.collectAsState()

    val cardList =
        if (levelSelected.value == -1) {
            if (tagWithCards.value.tag == emptyTag) {
                boxWithCards.value.cardList
            } else {
                tagWithCards.value.cardList
            }
        } else {
            if (tagWithCards.value.tag == emptyTag) {
                boxWithCards.value.cardList.filter { it.level == levelSelected.value }
            } else {
                tagWithCards.value.cardList.filter { it.level == levelSelected.value }
            }
        }

    var title = "Training"

    if (levelSelected.value != -1) {
        title += " of Level ${levelSelected.value}"
    }

    if (tagWithCards.value.tag != emptyTag) {
        title += " of Tag '${tagWithCards.value.tag.text}'"
    }

    if (title == "Training") {
        title += " all"
    }

    title += " of box ${boxWithCards.value.box.name}"

    BackHandler {
        navigateToBoxScreen(boxId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = title)
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateToBoxScreen(boxId) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Arrow"
                        )
                    }
                }
            )
        }
    ) { padding ->
        TrainingScreenContent(
            modifier = modifier.padding(padding),
            cardList = cardList
        )
    }
}

@Composable
fun TrainingScreenContent(
    modifier: Modifier = Modifier,
    cardList: List<Card>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Card {
            Column(
                modifier = modifier.padding(20.dp)
            ) {
                Text(text = "Hier könnte Ihre Werbung stehen!")
            }
        }
    }
}
