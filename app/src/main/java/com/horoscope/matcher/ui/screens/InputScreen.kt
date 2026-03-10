package com.horoscope.matcher.ui.screens

import androidx.compose.runtime.Composable
import com.horoscope.matcher.model.Rasi
import com.horoscope.matcher.model.Star
import com.horoscope.matcher.ui.components.PersonForm

@Composable
fun InputScreen(
    partnerSex: String,
    onSubmit: (name: String, dob: String, birthPlace: String, rasi: Rasi, star: Star) -> Unit,
    onBack: () -> Unit
) {
    val emoji = if (partnerSex == "boy") "👦" else "👧"
    val label = if (partnerSex == "boy") "Boy" else "Girl"
    PersonForm(
        title = "$emoji  Enter $label's Details",
        buttonLabel = "Check Compatibility ❤️",
        onSubmit = onSubmit,
        onBack = onBack
    )
}
