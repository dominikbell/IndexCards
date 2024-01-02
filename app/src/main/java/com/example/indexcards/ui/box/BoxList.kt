package com.example.indexcards.ui.box

import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Box

@Composable
fun BoxList(
    modifier: Modifier = Modifier,
    navigateToBoxScreen: (Long) -> Unit,
    boxList: List<Box>,
    onDelete: (Long) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        if (boxList.isEmpty()) {
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = stringResource(R.string.click_to_add_box),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
            ) {
                items(
                    items = boxList, key = { it.boxId }
                ) { item ->
                    BoxListItem(
                        modifier = Modifier,
                        item = item,
                        onClick = { navigateToBoxScreen(item.boxId) },
                        showDelete = onDelete
                    )
                }
            }
        }
    }
}

@Composable
fun BoxListItem(
    modifier: Modifier = Modifier,
    item: Box,
    onClick: (Long) -> Unit,
    showDelete: (Long) -> Unit,
) {
    Card(
        modifier = modifier
            .clickable { onClick(item.boxId) }
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = item.name,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = modifier.size(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = {
                    showDelete(item.boxId)
                }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}
