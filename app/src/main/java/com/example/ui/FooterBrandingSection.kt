package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppStrings
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.MutedBrown

@Composable
fun FooterBrandingSection(
    strings: AppStrings,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = strings.footerText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkCharcoal
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Elias Stickers v1.0.0 • Turn Your Photos into Amazing Stickers",
            fontSize = 11.sp,
            color = MutedBrown
        )
    }
}
