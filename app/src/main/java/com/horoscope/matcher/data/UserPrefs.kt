package com.horoscope.matcher.data

import android.content.Context
import android.content.SharedPreferences
import com.horoscope.matcher.model.PersonDetails
import com.horoscope.matcher.model.Rasi
import com.horoscope.matcher.model.Star

class UserPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("horoscope_user", Context.MODE_PRIVATE)

    var userSex: String?
        get() = prefs.getString("user_sex", null)
        set(v) = prefs.edit().putString("user_sex", v).apply()

    var userName: String?
        get() = prefs.getString("user_name", null)
        set(v) = prefs.edit().putString("user_name", v).apply()

    var userDob: String?
        get() = prefs.getString("user_dob", null)
        set(v) = prefs.edit().putString("user_dob", v).apply()

    var userBirthPlace: String?
        get() = prefs.getString("user_birth_place", null)
        set(v) = prefs.edit().putString("user_birth_place", v).apply()

    var userRasi: Rasi?
        get() = prefs.getString("user_rasi", null)?.let { Rasi.valueOf(it) }
        set(v) = prefs.edit().putString("user_rasi", v?.name).apply()

    var userStar: Star?
        get() = prefs.getString("user_star", null)?.let { Star.valueOf(it) }
        set(v) = prefs.edit().putString("user_star", v?.name).apply()

    val isProfileSaved: Boolean
        get() = userSex != null && userName != null && userDob != null
                && userBirthPlace != null && userRasi != null && userStar != null

    val lookingFor: String?
        get() = if (userSex == "boy") "girl" else if (userSex == "girl") "boy" else null

    fun toPersonDetails(): PersonDetails = PersonDetails(
        name = userName!!,
        dob = userDob!!,
        birthPlace = userBirthPlace!!,
        rasi = userRasi!!,
        star = userStar!!
    )

    fun clear() = prefs.edit().clear().apply()
}
