package com.horoscope.matcher.api

import com.horoscope.matcher.model.PersonDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object GeminiApi {

    private const val MODEL = "gemini-2.0-flash"
    private fun endpoint() =
        "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent?key=${ApiConfig.API_KEY}"

    suspend fun checkCompatibility(boy: PersonDetails, girl: PersonDetails): String {
        return withContext(Dispatchers.IO) {
            val prompt = buildPrompt(boy, girl)
            callGemini(prompt)
        }
    }

    private fun buildPrompt(boy: PersonDetails, girl: PersonDetails): String {
        return """
You are an expert Vedic astrologer. Analyze the horoscope compatibility (porutham) between this boy and girl based on their Rasi and Nakshatra.

Boy Details:
- Name: ${boy.name}
- Date of Birth: ${boy.dob}
- Birth Place: ${boy.birthPlace}
- Rasi: ${boy.rasi.displayName}
- Star (Nakshatra): ${boy.star.displayName}

Girl Details:
- Name: ${girl.name}
- Date of Birth: ${girl.dob}
- Birth Place: ${girl.birthPlace}
- Rasi: ${girl.rasi.displayName}
- Star (Nakshatra): ${girl.star.displayName}

Provide a detailed compatibility analysis covering:
1. Overall compatibility percentage
2. Dina Porutham
3. Gana Porutham
4. Mahendra Porutham
5. Stree Dheerga Porutham
6. Yoni Porutham
7. Rasi Porutham
8. Rasiyathipathi Porutham
9. Vasya Porutham
10. Rajju Porutham
11. Vedha Porutham

End with a clear verdict and recommendation.
Format the response nicely with emojis for readability.
        """.trimIndent()
    }

    private fun callGemini(prompt: String): String {
        val body = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
        }

        val conn = (URL(endpoint()).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
            connectTimeout = 30_000
            readTimeout = 60_000
        }

        try {
            conn.outputStream.use { it.write(body.toString().toByteArray()) }

            if (conn.responseCode != 200) {
                val err = conn.errorStream?.bufferedReader()?.readText() ?: "Unknown error"
                return "⚠️ API Error (${conn.responseCode}): $err"
            }

            val response = BufferedReader(InputStreamReader(conn.inputStream)).readText()
            val json = JSONObject(response)
            return json
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
        } catch (e: Exception) {
            return "⚠️ Network error: ${e.message}"
        } finally {
            conn.disconnect()
        }
    }
}
