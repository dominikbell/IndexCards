package com.example.indexcards.ui.tag

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.tag.EditTagViewModel
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
    var backgroundColor by remember { mutableStateOf(String()) }

    val selected = (selectedTags.contains(item))

    backgroundColor =
        if (selected) {
            item.color
        } else {
            /* TODO: change to neutral background */
            "#00000000"
        }

    Text(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(
                color = Color(android.graphics.Color.parseColor(backgroundColor))
            )
            .border(
                width = 2.dp,
                color = Color(android.graphics.Color.parseColor(item.color)),
                shape = RoundedCornerShape(4.dp)
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(4.dp),
        text = item.text
    )
}