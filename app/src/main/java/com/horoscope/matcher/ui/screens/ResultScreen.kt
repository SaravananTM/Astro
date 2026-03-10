package com.horoscope.matcher.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horoscope.matcher.model.MatchResult
import com.horoscope.matcher.ui.theme.LocalAppColors

@Composable
fun ResultScreen(
    boyName: String,
    girlName: String,
    result: MatchResult,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val appColors = LocalAppColors.current

    // Build share text
    val coupleEmoji = when {
        result.matchedCount >= 8 -> "👩‍❤️‍👨"
        result.matchedCount in 5..7 -> "❤️"
        else -> "💔"
    }
    val shareText = buildString {
        appendLine("💫 AstroMatch - Compatibility Report")
        appendLine("👦 $boyName  $coupleEmoji  👧 $girlName")
        appendLine("Score: ${result.matchedCount}/${result.totalCount}")
        appendLine(result.verdict)
        appendLine()
        appendLine("📋 10 Poruthams:")
        result.poruthams.forEachIndexed { i, p ->
            appendLine("${i+1}. ${if (p.matched) "✅" else "❌"} ${p.name} (${p.tamil})")
        }
        appendLine()
        appendLine("⚠️ Dosham:")
        result.doshams.forEach { d ->
            appendLine("${if (d.present) "⚠️" else "✅"} ${d.name}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar: Back (left) + Share (right)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                context.startActivity(Intent.createChooser(intent, "Share Report"))
            }) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
        }

        Text("💫 Compatibility Report", fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))

        Text("👦 $boyName  $coupleEmoji  👧 $girlName", fontSize = 16.sp)
        Spacer(Modifier.height(16.dp))

        // Score
        Text("${result.matchedCount}/${result.totalCount}", fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            color = if (result.matchedCount >= 6) appColors.scoreGreen else appColors.scoreRed)
        Text("Poruthams Matched", fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))

        // Verdict
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (result.matchedCount >= 6) appColors.verdictGood else appColors.verdictWarn
            )
        ) {
            Text(result.verdict, modifier = Modifier.padding(16.dp).fillMaxWidth(),
                fontSize = 16.sp, fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center)
        }

        Spacer(Modifier.height(20.dp))

        // 10 Poruthams
        Text("📋 10 Poruthams", fontSize = 18.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.height(8.dp))

        result.poruthams.forEachIndexed { index, p ->
            PoruthamRow(index + 1, p.name, p.tamil, p.matched, p.description)
        }

        Spacer(Modifier.height(20.dp))

        Text("⚠️ Dosham Analysis", fontSize = 18.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.height(8.dp))

        result.doshams.forEach { d ->
            DoshamRow(d.name, d.present, d.description)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) { Text("Try Another Match 🔄", fontSize = 16.sp) }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun PoruthamRow(number: Int, name: String, tamil: String, matched: Boolean, desc: String) {
    val appColors = LocalAppColors.current
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (matched) appColors.matchGood else appColors.matchBad
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text("$number.", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.width(28.dp))
            Text(if (matched) "✅" else "❌", fontSize = 18.sp)
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("$name ($tamil)", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Text(desc, fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun DoshamRow(name: String, present: Boolean, desc: String) {
    val appColors = LocalAppColors.current
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (present) appColors.matchBad else appColors.matchGood
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(if (present) "⚠️" else "✅", fontSize = 18.sp)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Text(desc, fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
