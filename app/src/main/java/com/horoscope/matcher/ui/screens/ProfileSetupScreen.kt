package com.horoscope.matcher.ui.screens

import androidx.compose.runtime.Composable
import com.horoscope.matcher.model.Rasi
import com.horoscope.matcher.model.Star
import com.horoscope.matcher.ui.components.PersonForm

@Composable
fun ProfileSetupScreen(
    userSex: String,
    onSave: (name: String, dob: String, birthPlace: String, rasi: Rasi, star: Star) -> Unit
) {
    val emoji = if (userSex == "boy") "👦" else "👧"
    PersonForm(
        title = "$emoji  Tell us about yourself",
        buttonLabel = "Save & Continue ➡️",
        onSubmit = onSave
    )
}
