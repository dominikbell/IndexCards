package com.example.indexcards.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        modifier = modifier.fillMaxWidth()
    ) {

        Divider(modifier = Modifier
            .height(3.dp)
            .fillMaxWidth())

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

        Divider(modifier = Modifier
            .height(3.dp)
            .fillMaxWidth())
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
    val backgroundColor =
        if (selected) {
            Color.LightGray
        } else {
            Color.White
        }

    Column(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Level $level", fontSize = 18.sp)

        Text(text = "$numberOfItems items", fontSize = 12.sp)
    }
}
