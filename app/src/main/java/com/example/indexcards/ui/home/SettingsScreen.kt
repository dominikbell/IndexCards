package com.example.indexcards.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.HomeScreenViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
) {
    val uiUserName = homeScreenViewModel.uiPreferences

    Column(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Receive Reminders"
            )
            Switch(
                checked = true,
                onCheckedChange = {}
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "User Name"
            )
            TextField(
                value = uiUserName.userName,
                onValueChange = { it: String -> homeScreenViewModel.updateUiUserName(it) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        modifier = Modifier,
        homeScreenViewModel = viewModel(
            factory = ViewModelProvider(context = LocalContext.current).factory
            )
    )
}