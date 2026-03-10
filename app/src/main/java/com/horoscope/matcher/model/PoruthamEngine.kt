package com.horoscope.matcher.model

/**
 * Offline Vedic 10-Porutham matching engine with Dosham checks.
 * Based on traditional Tamil/South Indian astrology rules.
 */

data class PoruthamItem(
    val name: String,
    val tamil: String,
    val matched: Boolean,
    val description: String
)

data class DoshamItem(
    val name: String,
    val present: Boolean,
    val description: String
)

data class MatchResult(
    val poruthams: List<PoruthamItem>,
    val doshams: List<DoshamItem>,
    val matchedCount: Int,
    val totalCount: Int,
    val percentage: Int,
    val verdict: String
)

object PoruthamEngine {

    // Nakshatra index (0-26) for each star
    private fun starIndex(s: Star): Int = s.ordinal
    // Rasi index (0-11)
    private fun rasiIndex(r: Rasi): Int = r.ordinal

    // Gana for each nakshatra: D=Deva, M=Manushya, R=Rakshasa
    private val gana = charArrayOf(
        'D','M','R', // Ashwini, Bharani, Krittika
        'D','D','M', // Rohini, Mrigashira, Ardra
        'D','D','R', // Punarvasu, Pushya, Ashlesha
        'R','M','M', // Magha, Purva Phalguni, Uttara Phalguni
        'D','R','D', // Hasta, Chitra, Swati
        'R','D','R', // Vishakha, Anuradha, Jyeshtha
        'R','M','D', // Moola, Purva Ashadha, Uttara Ashadha
        'D','R','R', // Shravana, Dhanishta, Shatabhisha
        'M','M','D'  // Purva Bhadrapada, Uttara Bhadrapada, Revati
    )

    // Yoni (animal) for each nakshatra pair
    // 0=Horse,1=Elephant,2=Sheep,3=Snake,4=Dog,5=Cat,6=Rat,7=Cow,8=Buffalo,
    // 9=Tiger,10=Deer,11=Monkey,12=Mongoose,13=Lion
    private val yoni = intArrayOf(
        0,0,2,4,4,5,5,2,5, // Ashwini..Ashlesha
        6,6,8,8,0,8,9,10,10, // Magha..Jyeshtha
        4,12,12,12,0,0,13,8,8 // Moola..Revati
    )

    // Yoni enemies (pairs that are incompatible)
    private val yoniEnemies = setOf(
        setOf(0,8), setOf(1,13), setOf(2,12), setOf(4,10),
        setOf(5,6), setOf(3,12), setOf(9,8)
    )

    // Rajju group for each nakshatra (0=Pada/Foot, 1=Hip, 2=Navel, 3=Neck, 4=Head)
    private val rajju = intArrayOf(
        0,1,2,3,4,3,2,1,0,
        0,1,2,3,4,3,2,1,0,
        0,1,2,3,4,3,2,1,0
    )

    // Vedha (obstruction) pairs by nakshatra index
    private val vedhaPairs = setOf(
        setOf(0,17), setOf(1,16), setOf(2,15),
        setOf(3,14), setOf(4,22), setOf(5,21),
        setOf(6,20), setOf(7,19), setOf(8,18),
        setOf(9,26), setOf(10,25), setOf(11,24),
        setOf(12,23)
    )

    // Vasya (attraction) - which rasis are vasya to which
    // Key rasi is attracted to value rasis
    private val vasyaMap = mapOf(
        Rasi.MESHAM to setOf(Rasi.SIMMAM, Rasi.VIRUCHIGAM),
        Rasi.RISHABAM to setOf(Rasi.KADAGAM, Rasi.THULAM),
        Rasi.MITHUNAM to setOf(Rasi.KANNI),
        Rasi.KADAGAM to setOf(Rasi.VIRUCHIGAM, Rasi.DHANUSU),
        Rasi.SIMMAM to setOf(Rasi.THULAM),
        Rasi.KANNI to setOf(Rasi.MEENAM, Rasi.MITHUNAM),
        Rasi.THULAM to setOf(Rasi.MAGARAM, Rasi.KANNI),
        Rasi.VIRUCHIGAM to setOf(Rasi.KADAGAM),
        Rasi.DHANUSU to setOf(Rasi.MEENAM),
        Rasi.MAGARAM to setOf(Rasi.MESHAM, Rasi.KUMBAM),
        Rasi.KUMBAM to setOf(Rasi.MESHAM),
        Rasi.MEENAM to setOf(Rasi.MAGARAM)
    )

    // Rasi lord for each rasi
    private val rasiLord = mapOf(
        Rasi.MESHAM to "Mars", Rasi.RISHABAM to "Venus",
        Rasi.MITHUNAM to "Mercury", Rasi.KADAGAM to "Moon",
        Rasi.SIMMAM to "Sun", Rasi.KANNI to "Mercury",
        Rasi.THULAM to "Venus", Rasi.VIRUCHIGAM to "Mars",
        Rasi.DHANUSU to "Jupiter", Rasi.MAGARAM to "Saturn",
        Rasi.KUMBAM to "Saturn", Rasi.MEENAM to "Jupiter"
    )

    // Planet friendships for Rasiyathipathi
    private val friends = mapOf(
        "Sun" to setOf("Moon","Mars","Jupiter"),
        "Moon" to setOf("Sun","Mercury"),
        "Mars" to setOf("Sun","Moon","Jupiter"),
        "Mercury" to setOf("Sun","Venus"),
        "Jupiter" to setOf("Sun","Moon","Mars"),
        "Venus" to setOf("Mercury","Saturn"),
        "Saturn" to setOf("Mercury","Venus")
    )

    fun calculate(boyStar: Star, boyRasi: Rasi, girlStar: Star, girlRasi: Rasi): MatchResult {
        val bsi = starIndex(boyStar)
        val gsi = starIndex(girlStar)
        val bri = rasiIndex(boyRasi)
        val gri = rasiIndex(girlRasi)

        val poruthams = mutableListOf<PoruthamItem>()

        // 1. Dina Porutham - count from girl's star to boy's star, divide by 9
        val dinaCount = ((bsi - gsi + 27) % 27) + 1
        val dinaRem = dinaCount % 9
        val dinaOk = dinaRem !in setOf(2, 4, 6, 8, 0)
        poruthams.add(PoruthamItem("Dina", "தினம்", dinaOk,
            if (dinaOk) "Good health and prosperity for the couple\nதம்பதிக்கு நல்ல ஆரோக்கியமும் செழிப்பும் உண்டு"
            else "May indicate health challenges — remedies available\nஆரோக்கிய சவால்கள் இருக்கலாம் — பரிகாரம் உள்ளது"))

        // 2. Gana Porutham
        val bg = gana[bsi]
        val gg = gana[gsi]
        val ganaOk = when {
            bg == gg -> true
            bg == 'D' && gg == 'M' -> true
            bg == 'M' && gg == 'D' -> true
            bg == 'D' && gg == 'R' -> false
            bg == 'R' && gg == 'D' -> false
            bg == 'M' && gg == 'R' -> false
            bg == 'R' && gg == 'M' -> true
            else -> false
        }
        val ganaName = mapOf('D' to "Deva", 'M' to "Manushya", 'R' to "Rakshasa")
        poruthams.add(PoruthamItem("Gana", "கணம்", ganaOk,
            if (ganaOk) "Temperament compatibility is good (${ganaName[bg]} & ${ganaName[gg]})\nகுணப் பொருத்தம் நன்றாக உள்ளது (${ganaName[bg]} & ${ganaName[gg]})"
            else "Different temperaments (${ganaName[bg]} & ${ganaName[gg]}) — needs understanding\nவேறுபட்ட குணங்கள் (${ganaName[bg]} & ${ganaName[gg]}) — புரிதல் தேவை"))

        // 3. Mahendra Porutham - boy's star from girl's should be 4,7,10,13,16,19,22,25
        val mahendraCount = ((bsi - gsi + 27) % 27) + 1
        val mahendraOk = mahendraCount in setOf(4,7,10,13,16,19,22,25)
        poruthams.add(PoruthamItem("Mahendra", "மகேந்திரம்", mahendraOk,
            if (mahendraOk) "Prosperity and progeny are favored\nசெல்வமும் சந்ததியும் சிறப்பாக இருக்கும்"
            else "Mahendra not matched — not a major concern\nமகேந்திரம் பொருந்தவில்லை — பெரிய கவலை இல்லை"))

        // 4. Stree Dheerga Porutham - boy's star should be >13 stars away from girl's
        val sdCount = ((bsi - gsi + 27) % 27) + 1
        val sdOk = sdCount > 13
        poruthams.add(PoruthamItem("Stree Dheerga", "ஸ்திரீ தீர்க்கம்", sdOk,
            if (sdOk) "Long and happy married life indicated\nநீண்ட மற்றும் மகிழ்ச்சியான திருமண வாழ்க்கை"
            else "Shorter compatibility range — other poruthams can compensate\nபொருத்தம் குறைவு — மற்ற பொருத்தங்கள் ஈடுசெய்யலாம்"))

        // 5. Yoni Porutham
        val byoni = yoni[bsi]
        val gyoni = yoni[gsi]
        val yoniOk = byoni == gyoni || setOf(byoni, gyoni) !in yoniEnemies
        poruthams.add(PoruthamItem("Yoni", "யோனி", yoniOk,
            if (yoniOk) "Physical and intimate compatibility is good\nஉடல் மற்றும் நெருக்கமான பொருத்தம் நன்றாக உள்ளது"
            else "Yoni mismatch — may need adjustment in relationship\nயோனி பொருத்தமின்மை — உறவில் சரிசெய்தல் தேவை"))

        // 6. Rasi Porutham - boy's rasi from girl's should be favorable
        val rasiDiff = ((bri - gri + 12) % 12) + 1
        val rasiOk = rasiDiff in setOf(2,3,4,5,6,7,12)
        poruthams.add(PoruthamItem("Rasi", "ராசி", rasiOk,
            if (rasiOk) "Rasi positions are favorable for harmony\nராசி நிலைகள் இணக்கத்திற்கு சாதகமாக உள்ளன"
            else "Rasi positions need attention — remedies can help\nராசி நிலைகள் கவனிக்க வேண்டும் — பரிகாரம் உதவும்"))

        // 7. Rasiyathipathi Porutham (Rasi Lord compatibility)
        val bLord = rasiLord[boyRasi]!!
        val gLord = rasiLord[girlRasi]!!
        val lordOk = bLord == gLord ||
                friends[bLord]?.contains(gLord) == true ||
                friends[gLord]?.contains(bLord) == true
        poruthams.add(PoruthamItem("Rasiyathipathi", "ராசியாதிபதி", lordOk,
            if (lordOk) "Rasi lords ($bLord & $gLord) are friendly\nராசி அதிபதிகள் ($bLord & $gLord) நட்பாக உள்ளனர்"
            else "Rasi lords ($bLord & $gLord) are not friendly — needs remedies\nராசி அதிபதிகள் ($bLord & $gLord) நட்பாக இல்லை — பரிகாரம் தேவை"))

        // 8. Vasya Porutham
        val vasyaOk = boyRasi == girlRasi ||
                vasyaMap[boyRasi]?.contains(girlRasi) == true ||
                vasyaMap[girlRasi]?.contains(boyRasi) == true
        poruthams.add(PoruthamItem("Vasya", "வசியம்", vasyaOk,
            if (vasyaOk) "Mutual attraction and magnetic bonding present\nஒருவருக்கொருவர் ஈர்ப்பும் பிணைப்பும் உள்ளது"
            else "Vasya not matched — emotional bonding needs effort\nவசியம் பொருந்தவில்லை — உணர்வுப் பிணைப்புக்கு முயற்சி தேவை"))

        // 9. Rajju Porutham (MOST IMPORTANT - same rajju is BAD)
        val rajjuOk = rajju[bsi] != rajju[gsi]
        poruthams.add(PoruthamItem("Rajju", "ரஜ்ஜு", rajjuOk,
            if (rajjuOk) "Rajju is clear — longevity of marriage is protected\nரஜ்ஜு தெளிவாக உள்ளது — திருமண நீடிப்பு பாதுகாக்கப்படுகிறது"
            else "⚠️ Same Rajju — serious concern for marital longevity\n⚠️ ஒரே ரஜ்ஜு — திருமண நீடிப்புக்கு தீவிர கவலை"))

        // 10. Vedha Porutham (obstruction check)
        val vedhaOk = setOf(bsi, gsi) !in vedhaPairs
        poruthams.add(PoruthamItem("Vedha", "வேதை", vedhaOk,
            if (vedhaOk) "No vedha obstruction — clear path ahead\nவேதை தடை இல்லை — தெளிவான பாதை"
            else "⚠️ Vedha present — may cause obstacles in married life\n⚠️ வேதை உள்ளது — திருமண வாழ்க்கையில் தடைகள் வரலாம்"))

        // Dosham checks
        val doshams = mutableListOf<DoshamItem>()

        // Rajju Dosham
        doshams.add(DoshamItem("Rajju Dosham", !rajjuOk,
            if (!rajjuOk) "Both stars fall in the same Rajju group — considered serious. Pariharam recommended.\nஇரு நட்சத்திரங்களும் ஒரே ரஜ்ஜு குழுவில் உள்ளன — தீவிரமானது. பரிகாரம் பரிந்துரைக்கப்படுகிறது."
            else "No Rajju Dosham detected.\nரஜ்ஜு தோஷம் இல்லை."))

        // Vedha Dosham
        doshams.add(DoshamItem("Vedha Dosham", !vedhaOk,
            if (!vedhaOk) "Stars are in Vedha pair — may cause difficulties. Temple remedies suggested.\nநட்சத்திரங்கள் வேதை ஜோடியில் உள்ளன — சிரமங்கள் வரலாம். கோவில் பரிகாரம் பரிந்துரைக்கப்படுகிறது."
            else "No Vedha Dosham detected.\nவேதை தோஷம் இல்லை."))

        // Gana Dosham (Deva-Rakshasa mismatch)
        val ganaDosham = (bg == 'D' && gg == 'R') || (bg == 'R' && gg == 'D')
        doshams.add(DoshamItem("Gana Dosham", ganaDosham,
            if (ganaDosham) "Deva-Rakshasa gana combination — temperament clash possible. Can be mitigated if other poruthams match.\nதேவ-ராட்சச கண இணைப்பு — குணமுரண்பாடு வரலாம். மற்ற பொருத்தங்கள் இருந்தால் சரிசெய்யலாம்."
            else "No Gana Dosham detected.\nகண தோஷம் இல்லை."))

        // Nadi Dosham (same nadi = bad) — nadi cycles in groups of 3 among nakshatras
        val boyNadi = bsi % 3  // 0=Aadhi, 1=Madhya, 2=Anthya
        val girlNadi = gsi % 3
        val nadiDosham = boyNadi == girlNadi
        val nadiNames = arrayOf("Aadhi (Vata)", "Madhya (Pitta)", "Anthya (Kapha)")
        doshams.add(DoshamItem("Nadi Dosham", nadiDosham,
            if (nadiDosham) "Both have ${nadiNames[boyNadi]} nadi — may affect progeny. Nadi pariharam recommended.\nஇருவருக்கும் ${nadiNames[boyNadi]} நாடி — சந்ததியை பாதிக்கலாம். நாடி பரிகாரம் பரிந்துரைக்கப்படுகிறது."
            else "Different nadis (${nadiNames[boyNadi]} & ${nadiNames[girlNadi]}) — no dosham.\nவெவ்வேறு நாடிகள் (${nadiNames[boyNadi]} & ${nadiNames[girlNadi]}) — தோஷம் இல்லை."))

        val matchedCount = poruthams.count { it.matched }
        val percentage = (matchedCount * 100) / poruthams.size
        val hasSeriousDosham = doshams.any { it.present && (it.name == "Rajju Dosham" || it.name == "Vedha Dosham") }

        val verdict = when {
            hasSeriousDosham -> "⚠️ Serious Dosham Present — Consult an astrologer for remedies"
            matchedCount >= 8 -> "✅ Excellent Match! ($matchedCount/10 poruthams)"
            matchedCount >= 6 -> "👍 Good Match ($matchedCount/10 poruthams)"
            matchedCount >= 4 -> "🤝 Average Match ($matchedCount/10) — Some remedies may help"
            else -> "⚠️ Low Compatibility ($matchedCount/10) — Detailed consultation recommended"
        }

        return MatchResult(poruthams, doshams, matchedCount, poruthams.size, percentage, verdict)
    }
}
