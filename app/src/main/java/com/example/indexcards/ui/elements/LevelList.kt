package com.example.indexcards.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.indexcards.utils.box.UiBoxWithCards

@Composable
fun LevelList(
    modifier: Modifier = Modifier,
    boxWithCards: UiBoxWithCards,
    currentLevel: Int,
    selectLevel: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (level in 0 until 5) {
                LevelListItem(
                    level = level,
                    numberOfItems = boxWithCards.cardList.filter { it.level == level }.size,
                    onClick = { selectLevel(level) },
                    selected = (currentLevel == level)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 3.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun LevelListItem(
    modifier: Modifier = Modifier,
    level: Int,
    numberOfItems: Int,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val borderThickness =
        if (selected) {
            3.dp
        } else {
            0.dp
        }
    val borderColor =
        if (selected) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() }
            .border(
                width = borderThickness,
                color = borderColor,
                shape = RoundedCornerShape(3.dp)
            )
            .padding(4.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Level ${level + 1}", fontSize = 18.sp)

        Text(text = "$numberOfItems items", fontSize = 12.sp)
    }
}
