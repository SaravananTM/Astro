package com.horoscope.matcher.data

import android.content.Context
import android.content.SharedPreferences
import com.horoscope.matcher.model.DoshamItem
import com.horoscope.matcher.model.MatchResult
import com.horoscope.matcher.model.PoruthamItem
import org.json.JSONArray
import org.json.JSONObject

data class MatchRecord(
    val boyName: String,
    val girlName: String,
    val boyRasi: String,
    val boyStar: String,
    val girlRasi: String,
    val girlStar: String,
    val analysis: String,
    val timestamp: Long,
    val matchedCount: Int = 0,
    val totalCount: Int = 10,
    val verdict: String = "",
    val poruthamsJson: String = "",
    val doshamsJson: String = ""
) {
    fun toMatchResult(): MatchResult {
        val poruthams = mutableListOf<PoruthamItem>()
        val doshams = mutableListOf<DoshamItem>()

        if (poruthamsJson.isNotEmpty()) {
            val arr = JSONArray(poruthamsJson)
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                poruthams.add(PoruthamItem(
                    name = o.getString("name"),
                    tamil = o.getString("tamil"),
                    matched = o.getBoolean("matched"),
                    description = o.getString("desc")
                ))
            }
        }
        if (doshamsJson.isNotEmpty()) {
            val arr = JSONArray(doshamsJson)
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                doshams.add(DoshamItem(
                    name = o.getString("name"),
                    present = o.getBoolean("present"),
                    description = o.getString("desc")
                ))
            }
        }

        return MatchResult(
            poruthams = poruthams,
            doshams = doshams,
            matchedCount = matchedCount,
            totalCount = totalCount,
            percentage = if (totalCount > 0) (matchedCount * 100) / totalCount else 0,
            verdict = verdict
        )
    }
}


class MatchHistory(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("match_history", Context.MODE_PRIVATE)

    fun save(record: MatchRecord) {
        val arr = loadJson()
        arr.put(JSONObject().apply {
            put("boyName", record.boyName)
            put("girlName", record.girlName)
            put("boyRasi", record.boyRasi)
            put("boyStar", record.boyStar)
            put("girlRasi", record.girlRasi)
            put("girlStar", record.girlStar)
            put("analysis", record.analysis)
            put("timestamp", record.timestamp)
            put("matchedCount", record.matchedCount)
            put("totalCount", record.totalCount)
            put("verdict", record.verdict)
            put("poruthamsJson", record.poruthamsJson)
            put("doshamsJson", record.doshamsJson)
        })
        prefs.edit().putString("records", arr.toString()).apply()
    }

    fun getAll(): List<MatchRecord> {
        val arr = loadJson()
        val list = mutableListOf<MatchRecord>()
        for (i in (arr.length() - 1) downTo 0) {
            val o = arr.getJSONObject(i)
            list.add(MatchRecord(
                boyName = o.getString("boyName"),
                girlName = o.getString("girlName"),
                boyRasi = o.getString("boyRasi"),
                boyStar = o.getString("boyStar"),
                girlRasi = o.getString("girlRasi"),
                girlStar = o.getString("girlStar"),
                analysis = o.getString("analysis"),
                timestamp = o.getLong("timestamp"),
                matchedCount = o.optInt("matchedCount", 0),
                totalCount = o.optInt("totalCount", 10),
                verdict = o.optString("verdict", ""),
                poruthamsJson = o.optString("poruthamsJson", ""),
                doshamsJson = o.optString("doshamsJson", "")
            ))
        }
        return list
    }

    fun getLatest(): MatchRecord? = getAll().firstOrNull()

    private fun loadJson(): JSONArray {
        val raw = prefs.getString("records", null) ?: return JSONArray()
        return try { JSONArray(raw) } catch (_: Exception) { JSONArray() }
    }
}
