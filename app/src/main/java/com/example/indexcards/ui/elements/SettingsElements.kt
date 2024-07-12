package com.example.indexcards.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.CHOICES_FOR_REMINDER_INTERVALS
import com.example.indexcards.utils.home.toWord
import java.util.Locale

@Composable
fun PeriodSelect(
    modifier: Modifier = Modifier,
    selectedPeriod: String,
    selectedAmount: Int,
    onClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier.width(80.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        CHOICES_FOR_REMINDER_INTERVALS.forEach { interval ->
            val selected = (interval == selectedPeriod)

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
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(3.dp))
                    .clickable { onClick(interval) }
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(
                        width = borderThickness,
                        color = borderColor,
                        shape = RoundedCornerShape(3.dp)
                    )
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = interval.toWord(selectedAmount)
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
            }
        }
    }
}

@Preview
@Composable
fun SingularPeriodSelectPreview() {
    PeriodSelect(
        selectedAmount = 1,
        selectedPeriod = "w"
    )
}

@Preview
@Composable
fun PluralPeriodSelectPreview() {
    PeriodSelect(
        selectedAmount = 2,
        selectedPeriod = "d"
    )
}