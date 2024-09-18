package com.example.indexcards.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.indexcards.ui.home.dialogs.TutorialDialog
import com.example.indexcards.utils.home.TutorialState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tutorial(
    modifier: Modifier,
    tutorialState: TutorialState,
    nextStep: () -> Unit = {},
    stopTutorial: () -> Unit = {},
) {
    if (tutorialState == TutorialState.ERROR) {
        AlertDialog(
            onDismissRequest = stopTutorial,
            confirmButton = {
                TextButton(
                    onClick = stopTutorial
                ) {
                    Text(text = "Stop Tutorial")
                }
            }
        )
    } else {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val screenHeight = configuration.screenHeightDp.dp
        val statusBarHeight = 24.dp
        val barHeight = TopAppBarDefaults.TopAppBarExpandedHeight.value.dp
        val buttonSize = 84.dp

        when (tutorialState) {
            TutorialState.WELCOME -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .clickable(interactionSource = null, indication = null, onClick = {})
                        .background(color = Color.Black.copy(alpha = 0.6F)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TutorialDialog(
                        tutorialState = tutorialState,
                        nextStep = nextStep,
                        stopTutorial = stopTutorial,
                    )
                }
            }

            TutorialState.ADD_BOX_INTRO -> {
                Column(
                    modifier = modifier.fillMaxSize(),
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(screenHeight - statusBarHeight - buttonSize)
                            .clickable(interactionSource = null, indication = null, onClick = {})
                            .background(color = Color.Black.copy(alpha = 0.6F)),
                    ) {}
                    Column(
                        modifier = modifier
                            .width(screenWidth - buttonSize)
                            .height(buttonSize)
                            .clickable(interactionSource = null, indication = null, onClick = {})
                            .background(color = Color.Black.copy(alpha = 0.6F)),
                    ) {}
                }
                    Column(
                        modifier = modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TutorialDialog(
                            tutorialState = tutorialState,
                            nextStep = nextStep,
                            stopTutorial = stopTutorial,
                        )
                    }
            }

            TutorialState.ADD_BOX_DIALOG -> {
            }

            else -> {
            }
        }
    }
}
