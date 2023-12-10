package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.indexcards.data.Box

@Composable
fun BoxList(
    modifier: Modifier = Modifier,
    boxList: List<Box>,
) {

    Column(
        /* TODO: This doesn't scroll - why? */
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        if (boxList.isEmpty()) {
            Text(
                text = "Click '+' to create a new box",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.Top,
            ) {
                items(
                    items = boxList, key = { it.boxId }
                ) { item ->
                    BoxListItem(
                        modifier = Modifier,
                        item = item,
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
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
        ,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
            ,
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
    }
}


