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
import com.horoscope.matcher.ui.theme.LocalAppColors

@Composable
fun HomeScreen(onSelect: (lookingFor: String) -> Unit) {
    val appColors = LocalAppColors.current

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🪔", fontSize = 56.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("🕉️", fontSize = 36.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text(
            "ஜோதிட பொருத்தம்",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "AstroMatch",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = appColors.gold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Vedic 10-Porutham Matching",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(48.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "யாருக்கு பொருத்தம் பார்க்க வேண்டும்?",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Who are you matching for?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GenderButton("👦  Boy", Color(0xFF1565C0)) { onSelect("boy") }
                    GenderButton("👧  Girl", Color(0xFFC62828)) { onSelect("girl") }
                }
            }
        }
    }
}

@Composable
private fun GenderButton(label: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(140.dp, 52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) { Text(label, fontSize = 17.sp) }
}
