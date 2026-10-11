package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AVAILABLE_STYLES
import com.example.model.AppStrings
import com.example.model.Language
import com.example.model.StickerStyle
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.MutedBrown

@Composable
fun StyleSelectorSection(
    strings: AppStrings,
    currentLanguage: Language,
    selectedStyle: StickerStyle,
    onStyleSelected: (StickerStyle) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Section Header: "Choose a style"
        Text(
            text = strings.chooseStyle,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = DarkCharcoal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Horizontally scrollable carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AVAILABLE_STYLES.forEach { style ->
                val isSelected = style.id == selectedStyle.id
                val styleName = if (currentLanguage == Language.BANGLA) style.nameBn else style.nameEn

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(105.dp)
                        .clickable { onStyleSelected(style) }
                        .testTag("style_item_${style.id}")
                ) {
                    // Style Card Container
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(88.dp)
                            .shadow(
                                elevation = if (isSelected) 8.dp else 2.dp,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) Color(0xFFFFF7ED) else Color.White
                            )
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color(style.badgeColorHex) else Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        // Emoji / Icon
                        Text(
                            text = style.emoji,
                            fontSize = 38.sp,
                            textAlign = TextAlign.Center
                        )

                        // Selected checkmark indicator badge
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(style.badgeColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Style Name
                    Text(
                        text = styleName,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) DarkCharcoal else MutedBrown,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}
