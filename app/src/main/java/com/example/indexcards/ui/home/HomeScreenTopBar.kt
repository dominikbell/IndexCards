package com.example.indexcards.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.indexcards.R
import com.example.indexcards.utils.box.boxScreenSorting
import com.example.indexcards.utils.home.HomeScreenSorting
import com.example.indexcards.utils.home.HomeScreenState
import com.example.indexcards.utils.home.homeScreenSorting

@Composable
fun HomeScreenTopBar(
    modifier: Modifier = Modifier,
    homeScreenState: HomeScreenState,
    isSelecting: Boolean,
    goToMainScreen: () -> Unit = {},
    goToSettings: () -> Unit = {},
    goToStatistics: () -> Unit = {},
    showAboutApp: () -> Unit = {},
    importBox: () -> Unit = {},
    onSortBy: (HomeScreenSorting) -> Unit = {},
    stopSelecting: () -> Unit = {},
) {
    when (homeScreenState) {
        HomeScreenState.MAIN -> {
            MainScreenTopBar(
                modifier = modifier,
                isSelecting = isSelecting,
                showAboutApp = showAboutApp,
                goToSettings = goToSettings,
                goToStatistics = goToStatistics,
                importBox = importBox,
                onSortBy = onSortBy,
                stopSelecting = stopSelecting
            )
        }

        else -> {
            SettingsStatisticsTopBar(
                homeScreenState = homeScreenState,
                goBack = goToMainScreen,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsStatisticsTopBar(
    modifier: Modifier = Modifier,
    homeScreenState: HomeScreenState,
    goBack: () -> Unit = {},
) {
    val title = when (homeScreenState) {
        HomeScreenState.SETTINGS -> {
            stringResource(id = R.string.settings)
        }

        HomeScreenState.STATISTICS -> {
            stringResource(id = R.string.statistics)
        }

        else -> {
            "This is a bug"
        }
    }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { goBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back"
                )
            }
        }
    )
}

@Preview
@Composable
fun SettingsTopBarPreview() {
    SettingsStatisticsTopBar(
        homeScreenState = HomeScreenState.SETTINGS,
    )
}

@Preview
@Composable
fun StatisticsTopBarPreview() {
    SettingsStatisticsTopBar(
        homeScreenState = HomeScreenState.STATISTICS,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar(
    modifier: Modifier,
    isSelecting: Boolean,
    showAboutApp: () -> Unit = {},
    goToSettings: () -> Unit = {},
    goToStatistics: () -> Unit = {},
    importBox: () -> Unit = {},
    onSortBy: (HomeScreenSorting) -> Unit = {},
    stopSelecting: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }

    if (isSelecting) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    text = stringResource(id = R.string.editing),
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(
                    onClick = { stopSelecting() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        )
    } else {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    text = stringResource(R.string.your_boxes),
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                )
            },
            actions = {
                IconButton(
                    onClick = { expanded = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = modifier
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.settings)) },
                        onClick = {
                            expanded = false
                            goToSettings()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.statistics)) },
                        onClick = {
                            expanded = false
                            goToStatistics()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = stringResource(id = R.string.sort_by))

                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    modifier = Modifier.rotate(-90f),
                                    contentDescription = "sort by"
                                )
                            }
                        },
                        onClick = {
                            expanded = false
                            sortExpanded = true
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.import_box)) },
                        onClick = {
                            expanded = false
                            importBox()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.about_app)) },
                        onClick = {
                            expanded = false
                            showAboutApp()
                        }
                    )
                }

                DropdownMenu(
                    expanded = sortExpanded,
                    onDismissRequest = { sortExpanded = false }
                ) {
                    homeScreenSorting.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = stringResource(option.second)) },
                            onClick = {
                                sortExpanded = false
                                onSortBy(option.first)
                            }
                        )
                    }
                }
            },
        )
    }
}

@Preview
@Composable
fun MainScreenTopBarPreview() {
    MainScreenTopBar(
        modifier = Modifier,
        isSelecting = false,
    )
}

@Preview
@Composable
fun MainScreenTopBarSelectingPreview() {
    MainScreenTopBar(
        modifier = Modifier,
        isSelecting = true,
    )
}