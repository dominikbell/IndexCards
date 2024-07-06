package com.example.indexcards.ui.tag

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.tag.EditTagViewModel
import com.example.indexcards.utils.tag.emptyTag
import com.example.indexcards.utils.tag.toTagDetails

@Composable
fun TagList(
    modifier: Modifier = Modifier,
    tagList: List<Tag>,
    onClick: (Tag) -> Unit = {},
    onLongClick: (Long) -> Unit = {},
    selectedTags: List<Tag>,
    editTagViewModel: EditTagViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ) {
        items(
            items = tagList,
            key = { it.tagId }
        ) { item ->
            TagListItem(
                modifier = modifier.padding(2.dp),
                item = item,
                onClick = { onClick(item) },
                onLongClick = {
                    editTagViewModel.setColor(item.color)
                    editTagViewModel.updateUiState(item.toTagDetails())
                    onLongClick(item.tagId)
                },
                selectedTags = selectedTags
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TagListItem(
    modifier: Modifier = Modifier,
    item: Tag,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    selectedTags: List<Tag>,
) {
    val selected = (selectedTags.contains(item))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(16.dp)
                .padding(end = 3.dp)
        ) {
            if (selected) {
                drawCircle(
                    color = Color(android.graphics.Color.parseColor(item.color)),
                )
            } else {
                drawCircle(
                    color = Color(android.graphics.Color.parseColor(item.color)),
                    style = Stroke(width = 6F)
                )
            }
        }
        Text(text = item.text)
    }
}

@Preview
@Composable
fun TagListItemPreview() {
    TagListItem(
        item = emptyTag.copy(),
        selectedTags = listOf()
    )
}