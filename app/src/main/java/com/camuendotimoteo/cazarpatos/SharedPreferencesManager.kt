package com.camuendotimoteo.cazarpatos

import android.app.Activity
import android.content.Context

class SharedPreferencesManager (val activity: Activity): FileHandler {

    override fun saveInformation(datosAGrabar: Pair<String, String>) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(LOGIN_KEY, datosAGrabar.first)
        editor.putString(PASSWORD_KEY, datosAGrabar.second)
        editor.apply()
    }

    override fun readInformation(): Pair<String, String> {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val email = sharedPref.getString(LOGIN_KEY,"").toString()
        val password = sharedPref.getString(PASSWORD_KEY,"").toString()
        return (email to password)
    }


}