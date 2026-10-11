package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppStrings
import com.example.model.GeneratedSticker
import com.example.model.GenerationState
import com.example.model.Language
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.MutedBrown

@Composable
fun StickerGallerySection(
    strings: AppStrings,
    currentLanguage: Language,
    generationState: GenerationState,
    onStickerClick: (GeneratedSticker) -> Unit,
    onDownloadSingle: (GeneratedSticker) -> Unit,
    onRegenerateSingle: (GeneratedSticker) -> Unit,
    onViewStickerSheet: () -> Unit,
    onDownloadAll: () -> Unit,
    onDownloadZip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        when (generationState) {
            is GenerationState.Idle -> {
                // Empty State (matching screenshot 1!)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = strings.emptyTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkCharcoal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = strings.emptySubtitle,
                        fontSize = 13.sp,
                        color = MutedBrown,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is GenerationState.Loading -> {
                // Handled in CreateButtonSection
            }

            is GenerationState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = generationState.errorMessage,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is GenerationState.Success -> {
                val stickers = generationState.stickers

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 2-column Sticker Grid (matching screenshot 2!)
                    val rows = (stickers.size + 1) / 2
                    for (rowIndex in 0 until rows) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            val leftIndex = rowIndex * 2
                            val rightIndex = leftIndex + 1

                            // Left Sticker Card
                            if (leftIndex < stickers.size) {
                                StickerCard(
                                    sticker = stickers[leftIndex],
                                    currentLanguage = currentLanguage,
                                    onCardClick = { onStickerClick(stickers[leftIndex]) },
                                    onDownload = { onDownloadSingle(stickers[leftIndex]) },
                                    onRegenerate = { onRegenerateSingle(stickers[leftIndex]) },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }

                            // Right Sticker Card
                            if (rightIndex < stickers.size) {
                                StickerCard(
                                    sticker = stickers[rightIndex],
                                    currentLanguage = currentLanguage,
                                    onCardClick = { onStickerClick(stickers[rightIndex]) },
                                    onDownload = { onDownloadSingle(stickers[rightIndex]) },
                                    onRegenerate = { onRegenerateSingle(stickers[rightIndex]) },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Bottom Action Buttons (matching screenshot 2!)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // View Sticker Sheet
                        OutlinedButton(
                            onClick = onViewStickerSheet,
                            shape = RoundedCornerShape(24.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(DarkCharcoal),
                                width = 1.5.dp
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = DarkCharcoal,
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("view_sticker_sheet_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = strings.viewStickerSheet,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Download all
                        Button(
                            onClick = onDownloadAll,
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkCharcoal,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("download_all_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = strings.downloadAll,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Download ZIP Pack
                    OutlinedButton(
                        onClick = onDownloadZip,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DarkCharcoal
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(42.dp)
                            .testTag("download_zip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Archive,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = strings.downloadZip,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StickerCard(
    sticker: GeneratedSticker,
    currentLanguage: Language,
    onCardClick: () -> Unit,
    onDownload: () -> Unit,
    onRegenerate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emotionTitle = if (currentLanguage == Language.BANGLA) sticker.emotion.nameBn else sticker.emotion.nameEn

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onCardClick() }
            .testTag("sticker_card_${sticker.emotion.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sticker Preview (Transparent background)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF9F7F4)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = sticker.bitmap.asImageBitmap(),
                    contentDescription = emotionTitle,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Action row under each sticker: [Download] | [Regenerate] (matching screenshot 2!)
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF3EFE9))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDownload,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Download ${sticker.emotion.nameEn}",
                        tint = DarkCharcoal,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(14.dp)
                        .background(Color(0xFFD6CFC7))
                )

                IconButton(
                    onClick = onRegenerate,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Regenerate ${sticker.emotion.nameEn}",
                        tint = DarkCharcoal,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Emotion Label
            Text(
                text = emotionTitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkCharcoal,
                textAlign = TextAlign.Center
            )
        }
    }
}
