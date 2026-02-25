package com.example.indexcards.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    confirmButton: @Composable () -> Unit = {},
    dismissButton: @Composable () -> Unit = {},
    title: String = "",
    titleIcon: ImageVector? = null,
    onIconClick: () -> Unit = {},
    text: @Composable () -> Unit = {},
    tutorial: Boolean = false,
    tutorialText: @Composable () -> Unit = {},
    tutorialConfirmButton: @Composable () -> Unit = {},
    tutorialDismissButton: @Composable () -> Unit = {},
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Column {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = shape,
                tonalElevation = AlertDialogDefaults.TonalElevation,
                color = containerColor,
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .wrapContentHeight()
                ) {
                    if (title.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .wrapContentHeight()
                                .padding(bottom = 20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1F),
                                    text = title,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                titleIcon?.let {
                                    IconButton(
                                        onClick = onIconClick
                                    ) {
                                        Icon(
                                            imageVector = titleIcon,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.Start)
                    ) {
                        text()
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        dismissButton()
                        Spacer(modifier = Modifier.width(12.dp))
                        confirmButton()
                        if (confirmButton != {}) {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                }
            }

            if (tutorial) {
                Surface(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = shape,
                    tonalElevation = AlertDialogDefaults.TonalElevation,
                    color = containerColor,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .wrapContentHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                        ) { tutorialText() }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            tutorialDismissButton()
                            tutorialConfirmButton()
                        }
                    }
                }
            }
        }
    }
}