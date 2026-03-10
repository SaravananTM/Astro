package com.horoscope.matcher.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horoscope.matcher.data.MatchRecord
import com.horoscope.matcher.model.Rasi
import com.horoscope.matcher.model.Star
import com.horoscope.matcher.ui.theme.LocalAppColors
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userSex: String,
    userName: String,
    userDob: String,
    birthPlace: String,
    rasi: Rasi,
    star: Star,
    recentMatch: MatchRecord?,
    onCheckMatch: () -> Unit,
    onResetProfile: () -> Unit,
    onPersonalDetails: () -> Unit,
    onMatchHistory: () -> Unit,
    onAbout: () -> Unit,
    onViewRecentMatch: () -> Unit
) {
    val lookingFor = if (userSex == "boy") "Girl" else "Boy"
    val emoji = if (userSex == "boy") "👦" else "👧"
    val appColors = LocalAppColors.current
    val cardColor = if (userSex == "boy") appColors.boyCard else appColors.girlCard
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                Spacer(Modifier.height(24.dp))
                Text("  💫 AstroMatch", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(Modifier.height(24.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text("📋  Personal Details") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onPersonalDetails() },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("📜  Match History") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onMatchHistory() },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("ℹ️  About") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onAbout() },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Spacer(Modifier.weight(1f))
                NavigationDrawerItem(
                    label = { Text("🔄  Reset Profile", fontSize = 13.sp) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onResetProfile() },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                Spacer(Modifier.weight(1f))
                Text("💫 AstroMatch", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(48.dp))
            }
            Spacer(Modifier.height(24.dp))

            // Profile card
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor)) {
                Row(modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, fontSize = 44.sp)
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(userName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        DetailRow("🎂", userDob)
                        DetailRow("📍", birthPlace)
                        DetailRow("🪐", "${rasi.displayName} (${rasi.tamil})")
                        DetailRow("⭐", "${star.displayName} (${star.tamil})")
                    }
                }
            }
            Spacer(Modifier.height(28.dp))

            // Check match button
            Button(onClick = onCheckMatch, modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) { Text("Check Match with a $lookingFor ❤️", fontSize = 18.sp) }

            // Recent match card
            if (recentMatch != null) {
                Spacer(Modifier.height(28.dp))
                Text("🕐 Recent Match", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start))
                Spacer(Modifier.height(12.dp))

                val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                val date = sdf.format(Date(recentMatch.timestamp))
                val coupleEmoji = when {
                    recentMatch.matchedCount >= 8 -> "👩‍❤️‍👨"
                    recentMatch.matchedCount in 5..7 -> "❤️"
                    else -> "💔"
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(onClick = onViewRecentMatch),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = appColors.recentMatch
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👦", fontSize = 20.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(recentMatch.boyName, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Text("  $coupleEmoji  ", fontSize = 16.sp)
                            Text("👧", fontSize = 20.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(recentMatch.girlName, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                        Spacer(Modifier.height(8.dp))
                        Row {
                            Text("${recentMatch.boyRasi}/${recentMatch.boyStar}", fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("  ↔  ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${recentMatch.girlRasi}/${recentMatch.girlStar}", fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${recentMatch.matchedCount}/${recentMatch.totalCount} Poruthams",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (recentMatch.matchedCount >= 6) appColors.scoreGreen else appColors.scoreRed
                            )
                            Text(date, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text("Tap to view full report →", fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(icon: String, text: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(icon, fontSize = 14.sp)
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
