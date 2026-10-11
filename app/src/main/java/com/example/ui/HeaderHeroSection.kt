package com.example.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppStrings
import com.example.model.Language
import com.example.ui.theme.*

@Composable
fun HeaderHeroSection(
    strings: AppStrings,
    currentLanguage: Language,
    onToggleLanguage: () -> Unit,
    onOpenApiKeyDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Try loading elias-profile.jpg from assets
    val eliasBitmap = remember {
        try {
            context.assets.open("elias-profile.jpg").use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (_: Exception) {
            null
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF9F3EA),
                        Color(0xFFEFE4D4)
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top action bar: Elias Avatar on left, Language + Key toggle on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Official Elias Profile Avatar & Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("elias_profile_header")
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(6.dp, CircleShape)
                            .clip(CircleShape)
                            .border(2.5.dp, Color.White, CircleShape)
                            .background(Color(0xFF292524))
                    ) {
                        if (eliasBitmap != null) {
                            Image(
                                bitmap = eliasBitmap.asImageBitmap(),
                                contentDescription = "Elias Official Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.elias_profile_default),
                                contentDescription = "Elias Official Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = strings.creatorName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkCharcoal
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldAccent)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AI Studio Pro",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MutedBrown
                            )
                        }
                    }
                }

                // Right: Language Selector and Settings
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // API Key Settings
                    IconButton(
                        onClick = onOpenApiKeyDialog,
                        modifier = Modifier
                            .size(38.dp)
                            .shadow(2.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .testTag("api_key_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Settings",
                            tint = AmberPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Language toggle chip
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .clickable { onToggleLanguage() }
                            .testTag("language_toggle_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = DarkCharcoal,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLanguage == Language.ENGLISH) "English" else "বাংলা",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkCharcoal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Hero Branding with Stickers
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Background decorative sticker badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left decorative badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFE4E6),
                        shadowElevation = 3.dp,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text(
                            text = "✨ POP!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = CoralAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Right decorative badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEF3C7),
                        shadowElevation = 3.dp,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "❤️ LOVE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = AmberPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Center Title
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = strings.appTitle,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 1.5.sp,
                        color = DarkCharcoal,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = strings.appTagline,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MutedBrown,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
