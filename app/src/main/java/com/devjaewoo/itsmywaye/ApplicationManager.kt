package com.devjaewoo.itsmywaye

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.devjaewoo.itsmywaye.dao.ItemDAO
import com.devjaewoo.itsmywaye.model.Item

class ApplicationManager : Application() {

    companion object {

        lateinit var applicationContext: Context
        lateinit var sharedPreferences: SharedPreferences

        var isAlarmEnabled: Boolean = false
            set(value) {
                if(value != field) sharedPreferences.edit().putBoolean(PREFERENCE_ALARM_ENABLED, value).apply()
                field = value
            }

        var alarmOffTimeStart: Int = 0
            set(value) {
                if(value != field) sharedPreferences.edit().putInt(PREFERENCE_ALARM_OFFTIME_START, value).apply()
                field = value
            }

        var alarmOffTimeEnd: Int = 0
            set(value) {
                if(value != field) sharedPreferences.edit().putInt(PREFERENCE_ALARM_OFFTIME_END, value).apply()
                field = value
            }

        var isAllNotificationAllowed: Boolean = false
            set(value) {
                if(value != field) sharedPreferences.edit().putBoolean(PREFERENCE_NOTIFICATION_ALLOW_ALL, value).apply()
                field = value
            }

        var isDiscordNotificationAllowed: Boolean = false
            set(value) {
                if(value != field) sharedPreferences.edit().putBoolean(
                    PREFERENCE_NOTIFICATION_ALLOW_DISCORD, value).apply()
                field = value
            }

        var isKakaotalkNotificationAllowed: Boolean = false
            set(value) {
                if(value != field) sharedPreferences.edit().putBoolean(
                    PREFERENCE_NOTIFICATION_ALLOW_KAKAOTALK, value).apply()
                field = value
            }

        //var listAllowedPackageName: ArrayList<String> = ArrayList(arrayListOf("com.discord", "com.kakao.talk"))

        lateinit var ItemNameList: List<String>
        lateinit var ItemList: List<Item>
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationManager.applicationContext = applicationContext
        sharedPreferences = getSharedPreferences(PREFERENCE_NAME, 0)

        loadPreferences()
    }

    private fun loadPreferences() {

        isAlarmEnabled = sharedPreferences.getBoolean(PREFERENCE_ALARM_ENABLED, true)
        alarmOffTimeStart = sharedPreferences.getInt(PREFERENCE_ALARM_OFFTIME_START, 0)
        alarmOffTimeEnd = sharedPreferences.getInt(PREFERENCE_ALARM_OFFTIME_END, 0)
        isAllNotificationAllowed = sharedPreferences.getBoolean(PREFERENCE_NOTIFICATION_ALLOW_ALL, false)
        isDiscordNotificationAllowed = sharedPreferences.getBoolean(PREFERENCE_NOTIFICATION_ALLOW_DISCORD, true)
        isKakaotalkNotificationAllowed = sharedPreferences.getBoolean(PREFERENCE_NOTIFICATION_ALLOW_KAKAOTALK, false)

        Log.d(TAG, "loadPreferences: \n" +
                "AlarmEnabled: $isAlarmEnabled\n" +
                "AlarmOffTimeStart: $alarmOffTimeStart\n" +
                "AlarmOffTimeEnd: $alarmOffTimeEnd\n" +
                "AllNotificationAllowed: $isAllNotificationAllowed\n" +
                "DiscordNotificationAllowed: $isDiscordNotificationAllowed\n" +
                "KakaotalkNotificationAllowed: $isKakaotalkNotificationAllowed\n")

        ItemNameList = applicationContext.resources.getStringArray(R.array.itemList).toList()

        val itemDAO = ItemDAO(applicationContext)
        ItemList = itemDAO.selectAll()
        Log.d(TAG, "loadPreferences: $ItemList")
    }
}
