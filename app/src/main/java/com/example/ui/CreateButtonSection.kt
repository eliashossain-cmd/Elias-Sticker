package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppStrings
import com.example.model.GenerationState
import com.example.ui.theme.DarkCharcoal

@Composable
fun CreateButtonSection(
    strings: AppStrings,
    isPhotoSelected: Boolean,
    generationState: GenerationState,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading = generationState is GenerationState.Loading

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            val progress = (generationState as GenerationState.Loading).progress
            val message = generationState.statusMessage

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(8.dp),
                    color = DarkCharcoal,
                    trackColor = Color(0xFFE5E7EB)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = message,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkCharcoal
                )
            }
        } else {
            Button(
                onClick = onCreateClick,
                enabled = isPhotoSelected,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkCharcoal,
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFEFEBE4),
                    disabledContentColor = Color(0xFFA8A29E)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isPhotoSelected) 4.dp else 0.dp
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(52.dp)
                    .testTag("create_stickers_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = strings.createStickers,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
