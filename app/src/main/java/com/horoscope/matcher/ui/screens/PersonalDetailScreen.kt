package com.horoscope.matcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horoscope.matcher.data.UserPrefs

@Composable
fun PersonalDetailScreen(prefs: UserPrefs, onBack: () -> Unit) {
    val emoji = if (prefs.userSex == "boy") "👦" else "👧"
    val cardColor = if (prefs.userSex == "boy") Color(0xFFE3F2FD) else Color(0xFFFCE4EC)

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        TextButton(onClick = onBack) { Text("← Back") }
        Spacer(Modifier.height(16.dp))
        Text("📋 Personal Details", fontSize = 24.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, fontSize = 40.sp)
                    Spacer(Modifier.width(16.dp))
                    Text(prefs.userName ?: "", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(16.dp))
                InfoRow("Gender", if (prefs.userSex == "boy") "Male" else "Female")
                InfoRow("Date of Birth", prefs.userDob ?: "")
                InfoRow("Birth Place", prefs.userBirthPlace ?: "")
                InfoRow("Rasi", "${prefs.userRasi?.displayName} (${prefs.userRasi?.tamil})")
                InfoRow("Star", "${prefs.userStar?.displayName} (${prefs.userStar?.tamil})")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium,
            modifier = Modifier.width(100.dp))
        Text(":  $value", fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
