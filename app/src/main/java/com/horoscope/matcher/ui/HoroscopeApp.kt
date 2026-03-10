package com.horoscope.matcher.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.horoscope.matcher.data.MatchHistory
import com.horoscope.matcher.data.MatchRecord
import com.horoscope.matcher.data.UserPrefs
import com.horoscope.matcher.model.MatchResult
import com.horoscope.matcher.model.PersonDetails
import com.horoscope.matcher.model.PoruthamEngine
import com.horoscope.matcher.ui.screens.*
import org.json.JSONArray
import org.json.JSONObject

private enum class Screen {
    HOME, PROFILE_SETUP, DASHBOARD, INPUT_PARTNER, RESULT,
    PERSONAL_DETAILS, MATCH_HISTORY, MATCH_DETAIL, ABOUT
}

@Composable
fun HoroscopeApp() {
    val context = LocalContext.current
    val prefs = remember { UserPrefs(context) }
    val history = remember { MatchHistory(context) }

    var screen by remember {
        mutableStateOf(if (prefs.isProfileSaved) Screen.DASHBOARD else Screen.HOME)
    }
    var pendingUserSex by remember { mutableStateOf(prefs.userSex ?: "") }
    var partnerDetails by remember { mutableStateOf<PersonDetails?>(null) }
    var matchResult by remember { mutableStateOf<MatchResult?>(null) }
    var selectedRecord by remember { mutableStateOf<MatchRecord?>(null) }

    // Back button handling — go to previous logical screen
    BackHandler(enabled = screen != Screen.HOME && screen != Screen.DASHBOARD) {
        screen = when (screen) {
            Screen.PROFILE_SETUP -> Screen.HOME
            Screen.INPUT_PARTNER -> Screen.DASHBOARD
            Screen.RESULT -> Screen.DASHBOARD
            Screen.PERSONAL_DETAILS -> Screen.DASHBOARD
            Screen.MATCH_HISTORY -> Screen.DASHBOARD
            Screen.MATCH_DETAIL -> Screen.MATCH_HISTORY
            Screen.ABOUT -> Screen.DASHBOARD
            else -> Screen.DASHBOARD
        }
    }

    when (screen) {
        Screen.HOME -> HomeScreen(
            onSelect = { lookingFor ->
                pendingUserSex = if (lookingFor == "boy") "girl" else "boy"
                screen = Screen.PROFILE_SETUP
            }
        )

        Screen.PROFILE_SETUP -> ProfileSetupScreen(
            userSex = pendingUserSex,
            onSave = { name, dob, place, rasi, star ->
                prefs.userSex = pendingUserSex
                prefs.userName = name
                prefs.userDob = dob
                prefs.userBirthPlace = place
                prefs.userRasi = rasi
                prefs.userStar = star
                screen = Screen.DASHBOARD
            }
        )

        Screen.DASHBOARD -> DashboardScreen(
            userSex = prefs.userSex!!,
            userName = prefs.userName!!,
            userDob = prefs.userDob!!,
            birthPlace = prefs.userBirthPlace!!,
            rasi = prefs.userRasi!!,
            star = prefs.userStar!!,
            recentMatch = history.getLatest(),
            onCheckMatch = { screen = Screen.INPUT_PARTNER },
            onResetProfile = {
                prefs.clear()
                pendingUserSex = ""
                screen = Screen.HOME
            },
            onPersonalDetails = { screen = Screen.PERSONAL_DETAILS },
            onMatchHistory = { screen = Screen.MATCH_HISTORY },
            onAbout = { screen = Screen.ABOUT },
            onViewRecentMatch = {
                val latest = history.getLatest()
                if (latest != null) {
                    selectedRecord = latest
                    screen = Screen.MATCH_DETAIL
                }
            }
        )

        Screen.INPUT_PARTNER -> InputScreen(
            partnerSex = prefs.lookingFor!!,
            onSubmit = { name, dob, place, rasi, star ->
                val partner = PersonDetails(name, dob, place, rasi, star)
                partnerDetails = partner
                val user = prefs.toPersonDetails()
                val boy = if (prefs.userSex == "boy") user else partner
                val girl = if (prefs.userSex == "girl") user else partner

                val result = PoruthamEngine.calculate(boy.star, boy.rasi, girl.star, girl.rasi)
                matchResult = result

                val analysis = buildString {
                    appendLine(result.verdict)
                    appendLine()
                    result.poruthams.forEach { p ->
                        appendLine("${if (p.matched) "✅" else "❌"} ${p.name} (${p.tamil}): ${p.description}")
                    }
                    appendLine()
                    result.doshams.forEach { d ->
                        appendLine("${if (d.present) "⚠️" else "✅"} ${d.name}: ${d.description}")
                    }
                }

                history.save(MatchRecord(
                    boyName = boy.name, girlName = girl.name,
                    boyRasi = boy.rasi.displayName, boyStar = boy.star.displayName,
                    girlRasi = girl.rasi.displayName, girlStar = girl.star.displayName,
                    analysis = analysis, timestamp = System.currentTimeMillis(),
                    matchedCount = result.matchedCount,
                    totalCount = result.totalCount,
                    verdict = result.verdict,
                    poruthamsJson = JSONArray().apply {
                        result.poruthams.forEach { p ->
                            put(JSONObject().apply {
                                put("name", p.name); put("tamil", p.tamil)
                                put("matched", p.matched); put("desc", p.description)
                            })
                        }
                    }.toString(),
                    doshamsJson = JSONArray().apply {
                        result.doshams.forEach { d ->
                            put(JSONObject().apply {
                                put("name", d.name); put("present", d.present)
                                put("desc", d.description)
                            })
                        }
                    }.toString()
                ))
                screen = Screen.RESULT
            },
            onBack = { screen = Screen.DASHBOARD }
        )

        Screen.RESULT -> {
            val bName = if (prefs.userSex == "boy") prefs.userName!! else partnerDetails!!.name
            val gName = if (prefs.userSex == "girl") prefs.userName!! else partnerDetails!!.name
            ResultScreen(
                boyName = bName, girlName = gName,
                result = matchResult!!,
                onBack = { screen = Screen.DASHBOARD }
            )
        }

        Screen.PERSONAL_DETAILS -> PersonalDetailScreen(
            prefs = prefs,
            onBack = { screen = Screen.DASHBOARD }
        )

        Screen.MATCH_HISTORY -> MatchHistoryScreen(
            records = history.getAll(),
            onViewDetail = { record ->
                selectedRecord = record
                screen = Screen.MATCH_DETAIL
            },
            onBack = { screen = Screen.DASHBOARD }
        )

        Screen.MATCH_DETAIL -> {
            val rec = selectedRecord!!
            val result = rec.toMatchResult()
            if (result.poruthams.isNotEmpty()) {
                ResultScreen(
                    boyName = rec.boyName, girlName = rec.girlName,
                    result = result,
                    onBack = { screen = Screen.MATCH_HISTORY }
                )
            } else {
                // Fallback for old records without porutham data
                MatchDetailScreen(
                    record = rec,
                    onBack = { screen = Screen.MATCH_HISTORY }
                )
            }
        }

        Screen.ABOUT -> AboutScreen(
            onBack = { screen = Screen.DASHBOARD }
        )
    }
}
