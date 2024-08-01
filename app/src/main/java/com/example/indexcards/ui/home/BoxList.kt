package com.example.indexcards.ui.home

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.ui.elements.BoxNameWithFlag
import com.example.indexcards.utils.state.emptyBox

@Composable
fun BoxList(
    modifier: Modifier = Modifier,
    boxList: List<Box>,
    navigateToBoxScreen: (Long) -> Unit = {},
    onDelete: (Box) -> Unit = {},
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
                itemsIndexed(
                    items = boxList
                ) { index, item ->

                    val finalOffset = if (index == boxList.size - 1) {
                        (2.5 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                    } else {
                        0.dp
                    }

                    BoxListItem(
                        modifier = Modifier.padding(bottom = finalOffset),
                        box = item,
                        onClick = { navigateToBoxScreen(item.boxId) },
                        showDelete = { onDelete(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun BoxListItem(
    modifier: Modifier = Modifier,
    box: Box,
    onClick: (Box) -> Unit = {},
    showDelete: (Box) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .clip(CardDefaults.shape)
            .clickable { onClick(box) }
            .fillMaxWidth()
        ,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                BoxNameWithFlag(
                    box = box, doBold = false, isTitle = true
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = box.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = {
                    showDelete(box)
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

@Preview
@Composable
fun BoxListItemPreview() {
    BoxListItem(
        box = emptyBox.copy(name = "Test Box", topic = "Japanese", description = "beschreibung")
    )
}

@Preview(showBackground = true)
@Composable
fun BoxListPreview() {
    BoxList(
        boxList = listOf(
            emptyBox.copy(name = "Box123", description = "descr", topic = "English"),
            emptyBox.copy(
                name = "Another Box",
                topic = "Chinese",
                description = "a longer description with more words"
            ),
        )
    )
}