package com.devjaewoo.itsmywaye;

import android.app.Application;
import android.content.Context

class SettingsManager : Application() {

    companion object {
        lateinit var ApplicationContext: Context
    }

    private var enableEpicItem: Boolean = false
    private var enableLegendaryItem: Boolean = false
    private var enableJinMadnic: Boolean = false
    private var enableMokamoka: Boolean = false
    private var enableKeisar: Boolean = false

    override fun onCreate() {
        super.onCreate()
        ApplicationContext = applicationContext
    }

    private fun loadPreferences() {

    }
}
