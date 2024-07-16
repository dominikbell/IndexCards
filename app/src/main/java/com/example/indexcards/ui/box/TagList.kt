package com.example.indexcards.ui.box

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.tag.emptyTag

@Composable
fun TagList(
    modifier: Modifier = Modifier,
    tagList: List<Tag>,
    selectedTags: List<Tag>,
    onClick: (Tag) -> Unit = {},
    onLongClick: (Tag) -> Unit = {},
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
                onLongClick = { onLongClick(item) },
                selectedTags = selectedTags
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TagListPreview() {
    TagList(
        tagList = listOf(
            emptyTag.copy(tagId = 1, text = "Tag1"),
            emptyTag.copy(tagId = 2, text = "nächster Tag"),
            emptyTag.copy(tagId = 3, text = "übermorgen"),
        ),
        selectedTags = listOf(emptyTag.copy(tagId = 3, text = "übermorgen")),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TagListItem(
    modifier: Modifier = Modifier,
    item: Tag,
    selectedTags: List<Tag>,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
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

@Preview(showBackground = true)
@Composable
fun TagListItemPreview() {
    TagListItem(
        item = emptyTag.copy(),
        selectedTags = listOf()
    )
}