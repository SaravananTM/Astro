package com.horoscope.matcher.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horoscope.matcher.model.Rasi
import com.horoscope.matcher.model.Star
import com.horoscope.matcher.model.TamilNaduPlaces
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonForm(
    title: String,
    buttonLabel: String,
    onSubmit: (name: String, dob: String, birthPlace: String, rasi: Rasi, star: Star) -> Unit,
    onBack: (() -> Unit)? = null
) {
    var name by remember { mutableStateOf("") }
    var dobDisplay by remember { mutableStateOf("") }
    var birthTime by remember { mutableStateOf("") }
    var birthPlace by remember { mutableStateOf("") }
    var selectedRasi by remember { mutableStateOf<Rasi?>(null) }
    var selectedStar by remember { mutableStateOf<Star?>(null) }
    var rasiExpanded by remember { mutableStateOf(false) }
    var starExpanded by remember { mutableStateOf(false) }
    var placeExpanded by remember { mutableStateOf(false) }

    val ctx = LocalContext.current
    val fmt = DateTimeFormatter.ofPattern("dd MMM yyyy")

    val isValid = name.isNotBlank() && dobDisplay.isNotBlank()
            && birthPlace.isNotBlank() && selectedRasi != null && selectedStar != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(24.dp))

        // Name
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Name") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        // DOB picker
        OutlinedButton(
            onClick = {
                val now = LocalDate.now()
                DatePickerDialog(ctx, { _, y, m, d ->
                    dobDisplay = LocalDate.of(y, m + 1, d).format(fmt)
                }, now.year - 25, 0, 1).show()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (dobDisplay.isNotBlank()) "🎂  $dobDisplay" else "📅  Pick Date of Birth",
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.height(12.dp))

        // Birth Time picker
        OutlinedButton(
            onClick = {
                TimePickerDialog(ctx, { _, h, m ->
                    val amPm = if (h < 12) "AM" else "PM"
                    val hour12 = if (h == 0) 12 else if (h > 12) h - 12 else h
                    birthTime = String.format("%02d:%02d %s", hour12, m, amPm)
                }, 6, 0, false).show()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (birthTime.isNotBlank()) "🕐  $birthTime" else "🕐  Pick Birth Time",
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.height(12.dp))

        // Birth Place dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = birthPlace,
                onValueChange = {},
                readOnly = true,
                label = { Text("Birth Place (Tamil Nadu)") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(placeExpanded)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Surface(
                modifier = Modifier.matchParentSize(),
                color = Color.Transparent,
                onClick = { placeExpanded = true }
            ) {}
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                DropdownMenu(
                    expanded = placeExpanded,
                    onDismissRequest = { placeExpanded = false }
                ) {
                    TamilNaduPlaces.cities.forEach { place ->
                        DropdownMenuItem(
                            text = { Text(place) },
                            onClick = { birthPlace = place; placeExpanded = false }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        // Rasi dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedRasi?.let { "${it.displayName}  (${it.tamil})" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Rasi (Moon Sign)") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(rasiExpanded)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Surface(
                modifier = Modifier.matchParentSize(),
                color = Color.Transparent,
                onClick = { rasiExpanded = true }
            ) {}
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                DropdownMenu(
                    expanded = rasiExpanded,
                    onDismissRequest = { rasiExpanded = false }
                ) {
                    Rasi.entries.forEach { rasi ->
                        DropdownMenuItem(
                            text = { Text("${rasi.displayName}  (${rasi.tamil})") },
                            onClick = { selectedRasi = rasi; rasiExpanded = false }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        // Star dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedStar?.let { "${it.displayName}  (${it.tamil})" } ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Star (Nakshatra)") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(starExpanded)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Surface(
                modifier = Modifier.matchParentSize(),
                color = Color.Transparent,
                onClick = { starExpanded = true }
            ) {}
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                DropdownMenu(
                    expanded = starExpanded,
                    onDismissRequest = { starExpanded = false }
                ) {
                    Star.entries.forEach { star ->
                        DropdownMenuItem(
                            text = { Text("${star.displayName}  (${star.tamil})") },
                            onClick = { selectedStar = star; starExpanded = false }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = {
                val dobWithTime = if (birthTime.isNotBlank()) "$dobDisplay, $birthTime" else dobDisplay
                onSubmit(name.trim(), dobWithTime, birthPlace.trim(), selectedRasi!!, selectedStar!!)
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) { Text(buttonLabel, fontSize = 18.sp) }

        if (onBack != null) {
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onBack) { Text("← Back") }
        }
    }
}
