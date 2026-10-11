package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppStrings
import com.example.model.GeneratedSticker
import com.example.model.Language
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.MutedBrown

@Composable
fun StickerEditorDialog(
    strings: AppStrings,
    currentLanguage: Language,
    sticker: GeneratedSticker?,
    onDismiss: () -> Unit,
    onSaveCustomization: (borderThickness: Int, borderColorHex: Long, caption: String) -> Unit,
    onDownload: (GeneratedSticker) -> Unit
) {
    if (sticker == null) return

    var borderThickness by remember(sticker) { mutableStateOf(sticker.borderThicknessDp.toFloat()) }
    var selectedBorderColor by remember(sticker) { mutableStateOf(sticker.borderColorHex) }
    var captionText by remember(sticker) { mutableStateOf(sticker.caption) }
    var bgPreviewMode by remember { mutableStateOf(0) } // 0: Cream, 1: Transparent, 2: Dark

    val borderColors = listOf(
        0xFFFFFFFF to "White",
        0xFFFEF3C7 to "Warm Butter",
        0xFFFDE047 to "Neon Gold",
        0xFFFDA4AF to "Pastel Pink",
        0xFFBAE6FD to "Cyan Blue",
        0xFF1E1B18 to "Dark Ink"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .testTag("sticker_editor_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.customizeTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkCharcoal
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Sticker Preview Box with Background Mode
                    val previewBgColor = when (bgPreviewMode) {
                        1 -> Color(0xFFE2E8F0) // Checkered simulation
                        2 -> Color(0xFF1E1B18) // Dark
                        else -> Color(0xFFFAF6EE) // Cream
                    }

                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(previewBgColor)
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = sticker.bitmap.asImageBitmap(),
                            contentDescription = sticker.emotion.nameEn,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Background Preview Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = bgPreviewMode == 0,
                            onClick = { bgPreviewMode = 0 },
                            label = { Text(strings.solidCream, fontSize = 11.sp) }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        FilterChip(
                            selected = bgPreviewMode == 1,
                            onClick = { bgPreviewMode = 1 },
                            label = { Text(strings.transparent, fontSize = 11.sp) }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        FilterChip(
                            selected = bgPreviewMode == 2,
                            onClick = { bgPreviewMode = 2 },
                            label = { Text(strings.darkCheckered, fontSize = 11.sp) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Border Thickness Slider
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = strings.borderThickness,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkCharcoal
                            )
                            Text(
                                text = "${borderThickness.toInt()} px",
                                fontSize = 13.sp,
                                color = MutedBrown
                            )
                        }

                        Slider(
                            value = borderThickness,
                            onValueChange = { borderThickness = it },
                            valueRange = 8f..32f,
                            colors = SliderDefaults.colors(
                                thumbColor = DarkCharcoal,
                                activeTrackColor = DarkCharcoal
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Border Color Picker
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = strings.borderColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkCharcoal
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            borderColors.forEach { (colorHex, _) ->
                                val isSelected = selectedBorderColor == colorHex
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(colorHex))
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) DarkCharcoal else Color.LightGray,
                                            shape = CircleShape
                                        )
                                        .clickable { selectedBorderColor = colorHex }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Custom Caption Input
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = strings.addCaption,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkCharcoal
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = captionText,
                            onValueChange = { captionText = it },
                            placeholder = { Text(strings.captionPlaceholder, fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("caption_input_field")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDownload(sticker) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = strings.downloadSingle, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            onSaveCustomization(borderThickness.toInt(), selectedBorderColor, captionText)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkCharcoal),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_customization_button")
                    ) {
                        Text(text = strings.saveChanges, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
