package com.example.indexcards.ui.home.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.utils.home.TutorialState

@Composable
fun TutorialDialog(
    modifier: Modifier = Modifier,
    tutorialState: TutorialState,
    nextStep: () -> Unit,
    stopTutorial: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val title = when (tutorialState) {
        TutorialState.WELCOME -> "Welcome to IndexCards!"
        TutorialState.ADD_BOX_INTRO -> "Adding a new box"
        TutorialState.NEW_BOX -> "Great job!"
        else -> ""
    }


    Surface(
        modifier = modifier
            .width(screenWidth * 4 / 5)
            .wrapContentHeight(),
        shape = AlertDialogDefaults.shape,
        color = AlertDialogDefaults.containerColor,
        tonalElevation = AlertDialogDefaults.TonalElevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Text(
                text = when (tutorialState) {
                    TutorialState.WELCOME -> "This tutorial will show you how to use the app."

                    TutorialState.ADD_BOX_INTRO -> "Click the '+' to add a new box."

                    TutorialState.NEW_BOX -> "Click the box to see its contents."

                    else -> ""
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = stopTutorial
                ) {
                    Text(text = "Stop Tutorial")
                }

                if (tutorialState == TutorialState.WELCOME) {
                    TextButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = nextStep
                    ) {
                        Text(text = "Next")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TutorialDialogWelcomePreview() {
    TutorialDialog(
        tutorialState = TutorialState.WELCOME,
        nextStep = {},
        stopTutorial = {},
    )
}

@Preview
@Composable
fun TutorialDialogAddBoxPreview() {
    TutorialDialog(
        tutorialState = TutorialState.ADD_BOX_INTRO,
        nextStep = {},
        stopTutorial = {},
    )
}