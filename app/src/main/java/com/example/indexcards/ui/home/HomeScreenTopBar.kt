package com.example.indexcards.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.indexcards.R
import com.example.indexcards.utils.home.HomeScreenState

@Composable
fun HomeScreenTopBar(
    modifier: Modifier = Modifier,
    homeScreenState: HomeScreenState,
    goToMainScreen: () -> Unit = {},
    goToSettings: () -> Unit = {},
    goToStatistics: () -> Unit = {},
    showAboutApp: () -> Unit = {},
    saveSettings: () -> Unit,
) {
    when (homeScreenState) {
        HomeScreenState.MAIN -> {
            MainScreenTopBar(
                modifier = modifier,
                showAboutApp = showAboutApp,
                goToSettings = goToSettings,
                goToStatistics = goToStatistics
            )
        }

        else -> {
            SettingsStatisticsTopBar(
                homeScreenState = homeScreenState,
                goBack = goToMainScreen,
                saveSettings = saveSettings
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsStatisticsTopBar(
    modifier: Modifier = Modifier,
    homeScreenState: HomeScreenState,
    goBack: () -> Unit,
    saveSettings: () -> Unit,
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

    fun saveAndGoBack() {
        when (homeScreenState) {
            HomeScreenState.SETTINGS -> {
                saveSettings()
                goBack()
            }

            else -> {
                goBack()
            }
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
                onClick = { saveAndGoBack() }
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
        goBack = { },
        saveSettings = { }
    )
}

@Preview
@Composable
fun StatisticsTopBarPreview() {
    SettingsStatisticsTopBar(
        homeScreenState = HomeScreenState.STATISTICS,
        goBack = { },
        saveSettings = { }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar(
    modifier: Modifier,
    showAboutApp: () -> Unit,
    goToSettings: () -> Unit = {},
    goToStatistics: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

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
                onClick = {
                    expanded = true
                }) {
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
                    text = {
                        Text(text = stringResource(R.string.settings))
                    },
                    onClick = {
                        expanded = false
                        goToSettings()
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.statistics))
                    },
                    onClick = {
                        expanded = false
                        goToStatistics()
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.about_app))
                    },
                    onClick = {
                        expanded = false
                        showAboutApp()
                    }
                )
            }
        },
    )
}

@Preview
@Composable
fun MainScreenTopBarPreview() {
    MainScreenTopBar(
        modifier = Modifier,
        showAboutApp = { }
    )
}