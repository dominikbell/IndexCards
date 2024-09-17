package com.example.indexcards.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.indexcards.data.Box
import com.example.indexcards.ui.elements.AddBoxButton
import com.example.indexcards.ui.home.dialogs.TutorialDialog
import com.example.indexcards.utils.home.TutorialState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tutorial(
    modifier: Modifier,
    tutorialState: TutorialState,
    newBox: Box,
    nextStep: () -> Unit = {},
    stopTutorial: () -> Unit = {},
    openAddBoxDialog: () -> Unit = {},
) {
    val topPadding = TopAppBarDefaults.TopAppBarExpandedHeight.value.dp

    when (tutorialState) {
        TutorialState.ADD_BOX_DIALOG -> {}

        else -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.Black.copy(alpha = 0.6F),
                floatingActionButton = {
                    when (tutorialState) {
                        TutorialState.WELCOME -> {}
                        TutorialState.ADD_BOX_INTRO -> {
                            Box(
                                modifier = Modifier
                                    .clip(FloatingActionButtonDefaults.shape)
                                    .background(color = MaterialTheme.colorScheme.primary)
                                    .padding(6.dp)
                            ) {
                                AddBoxButton(
                                    modifier = Modifier,
                                    onClick = {
                                        nextStep()
                                        openAddBoxDialog()
                                    }
                                )
                            }
                        }

                        else -> {}
                    }
                }
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = if (tutorialState == TutorialState.NEW_BOX) topPadding else 0.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (tutorialState == TutorialState.NEW_BOX) {
                        Box(
                            modifier = Modifier
                                .clip(CardDefaults.shape)
                                .background(color = MaterialTheme.colorScheme.primary)
                                .padding(4.dp)
                        ) {
                            BoxListItem(box = newBox, isSelecting = false, isSelected = false)
                        }
                    }

                    Column(
                        modifier = modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        TutorialDialog(
                            tutorialState = tutorialState,
                            nextStep = nextStep,
                            stopTutorial = stopTutorial,
                        )
                    }
                }
            }
        }
    }
}
