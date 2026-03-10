package com.horoscope.matcher.model

/** 12 Tamil/Vedic Rasi (Moon signs). */
enum class Rasi(val tamil: String) {
    MESHAM("மேஷம்"),
    RISHABAM("ரிஷபம்"),
    MITHUNAM("மிதுனம்"),
    KADAGAM("கடகம்"),
    SIMMAM("சிம்மம்"),
    KANNI("கன்னி"),
    THULAM("துலாம்"),
    VIRUCHIGAM("விருச்சிகம்"),
    DHANUSU("தனுசு"),
    MAGARAM("மகரம்"),
    KUMBAM("கும்பம்"),
    MEENAM("மீனம்");

    val displayName: String get() = name.lowercase().replaceFirstChar { it.uppercase() }
}

/** 27 Nakshatras (Stars). */
enum class Star(val tamil: String) {
    ASHWINI("அஸ்வினி"),
    BHARANI("பரணி"),
    KRITTIKA("கிருத்திகை"),
    ROHINI("ரோகிணி"),
    MRIGASHIRA("மிருகசீரிடம்"),
    ARDRA("திருவாதிரை"),
    PUNARVASU("புனர்பூசம்"),
    PUSHYA("பூசம்"),
    ASHLESHA("ஆயில்யம்"),
    MAGHA("மகம்"),
    PURVA_PHALGUNI("பூரம்"),
    UTTARA_PHALGUNI("உத்திரம்"),
    HASTA("அஸ்தம்"),
    CHITRA("சித்திரை"),
    SWATI("சுவாதி"),
    VISHAKHA("விசாகம்"),
    ANURADHA("அனுஷம்"),
    JYESHTHA("கேட்டை"),
    MOOLA("மூலம்"),
    PURVA_ASHADHA("பூராடம்"),
    UTTARA_ASHADHA("உத்திராடம்"),
    SHRAVANA("திருவோணம்"),
    DHANISHTA("அவிட்டம்"),
    SHATABHISHA("சதயம்"),
    PURVA_BHADRAPADA("பூரட்டாதி"),
    UTTARA_BHADRAPADA("உத்திரட்டாதி"),
    REVATI("ரேவதி");

    val displayName: String get() = name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }
}

/** Person details used for both boy and girl. */
data class PersonDetails(
    val name: String,
    val dob: String,        // "dd MMM yyyy"
    val birthPlace: String,
    val rasi: Rasi,
    val star: Star
)
