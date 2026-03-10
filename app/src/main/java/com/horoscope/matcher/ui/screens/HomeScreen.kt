package com.horoscope.matcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Shown only on first launch – "Are you looking for a Boy or Girl?" */
@Composable
fun HomeScreen(onSelect: (lookingFor: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("💫", fontSize = 64.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text(
            "AstroMatch",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Find your zodiac compatibility",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(48.dp))
        Text(
            "Are you looking for a…",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            GenderButton("👦  Boy", Color(0xFF1976D2)) { onSelect("boy") }
            GenderButton("👧  Girl", Color(0xFFD81B60)) { onSelect("girl") }
        }
    }
}

@Composable
private fun GenderButton(label: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(140.dp, 56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) { Text(label, fontSize = 18.sp) }
}
