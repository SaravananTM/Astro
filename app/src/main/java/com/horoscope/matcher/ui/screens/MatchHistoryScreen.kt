package com.horoscope.matcher.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horoscope.matcher.data.MatchRecord
import com.horoscope.matcher.ui.theme.LocalAppColors
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MatchHistoryScreen(
    records: List<MatchRecord>,
    onViewDetail: (MatchRecord) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        TextButton(onClick = onBack) { Text("← Back") }
        Spacer(Modifier.height(16.dp))
        Text("📜 Match History", fontSize = 24.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))

        if (records.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No matches yet.\nCheck your first match!", fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(records) { record ->
                    MatchCard(record) { onViewDetail(record) }
                }
            }
        }
    }
}

@Composable
private fun MatchCard(record: MatchRecord, onClick: () -> Unit) {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val date = sdf.format(Date(record.timestamp))
    val coupleEmoji = when {
        record.matchedCount >= 8 -> "👩‍❤️‍👨"
        record.matchedCount in 5..7 -> "❤️"
        else -> "💔"
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("👦", fontSize = 18.sp)
                Spacer(Modifier.width(4.dp))
                Text(record.boyName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("  $coupleEmoji  ", fontSize = 16.sp)
                Text("👧", fontSize = 18.sp)
                Spacer(Modifier.width(4.dp))
                Text(record.girlName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Text("${record.boyRasi} / ${record.boyStar}  ↔  ${record.girlRasi} / ${record.girlStar}",
                fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${record.matchedCount}/${record.totalCount} Poruthams",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (record.matchedCount >= 6) LocalAppColors.current.scoreGreen
                            else LocalAppColors.current.scoreRed
                )
                Text(date, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
