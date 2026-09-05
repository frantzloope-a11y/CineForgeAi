package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricIndigo

@Composable
fun AspectRatioSelector(
    selectedRatio: String,
    onRatioSelected: (String) -> Unit,
    ratios: List<String> = listOf("9:16", "16:9", "1:1"),
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Aspect Ratio",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(ratios) { ratio ->
                val isSelected = ratio == selectedRatio

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) ElectricIndigo.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) CyanAccent else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onRatioSelected(ratio) }
                        .testTag("ratio_chip_$ratio")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        // Ratio visual icon
                        Box(
                            modifier = Modifier
                                .then(
                                    when (ratio) {
                                        "9:16", "3:4", "2:3", "4:5", "1:2" -> Modifier.size(width = 12.dp, height = 20.dp)
                                        "16:9", "4:3", "3:2", "5:4", "2:1" -> Modifier.size(width = 20.dp, height = 12.dp)
                                        else -> Modifier.size(16.dp)
                                    }
                                )
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (isSelected) CyanAccent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = ratio,
                                color = if (isSelected) CyanAccent else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            val subtitle = when (ratio) {
                                "9:16" -> "Shorts/Reels"
                                "16:9" -> "YouTube/Film"
                                "1:1" -> "Square Feed"
                                else -> null
                            }
                            if (subtitle != null) {
                                Text(
                                    text = subtitle,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
