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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
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
        TutorialState.WELCOME -> stringResource(id = R.string.welcome_title)
        TutorialState.ADD_BOX_INTRO -> stringResource(id = R.string.add_box_intro_title)
        TutorialState.NEW_BOX -> stringResource(id = R.string.new_box_title)
        TutorialState.ADD_CARD_INTRO -> stringResource(id = R.string.add_card_intro_title)
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
                    TutorialState.WELCOME -> stringResource(id = R.string.welcome_text)

                    TutorialState.ADD_BOX_INTRO -> stringResource(id = R.string.add_box_intro_title)

                    TutorialState.NEW_BOX -> stringResource(id = R.string.new_box_text)

                    TutorialState.ADD_CARD_INTRO -> stringResource(id = R.string.add_card_intro_text)

                    else -> ""
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = stopTutorial
                ) {
                    Text(text = stringResource(id = R.string.end_tutorial))
                }

                if (tutorialState == TutorialState.WELCOME) {
                    TextButton(
                        modifier = Modifier.weight(1F),
                        onClick = nextStep
                    ) {
                        Text(text = stringResource(id = R.string.next))
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