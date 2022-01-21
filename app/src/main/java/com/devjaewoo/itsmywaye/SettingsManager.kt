package com.devjaewoo.itsmywaye;

import android.app.Application;
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.devjaewoo.itsmywaye.dao.ItemDAO
import com.devjaewoo.itsmywaye.model.Item

class SettingsManager : Application() {

    companion object {

        private const val PREFERENCE_ALARM_ENABLED = "alarm_enabled"
        private const val PREFERENCE_NOTIFICATION_ENABLED = "notification_enabled"
        private const val PREFERENCE_ALARM_OFFTIME_START = "alarm_offtime_start"
        private const val PREFERENCE_ALARM_OFFTIME_END = "alarm_offtime_end"

        lateinit var ApplicationContext: Context
        lateinit var sharedPreferences: SharedPreferences

        var isAlarmEnabled: Boolean = false
            set(value) {
                if(value != field) sharedPreferences.edit().putBoolean(PREFERENCE_ALARM_ENABLED, value).apply()
                field = value
            }

        var isNotificationEnabled: Boolean = false
            set(value) {
                if(value != field) sharedPreferences.edit().putBoolean(PREFERENCE_NOTIFICATION_ENABLED, value).apply()
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

        lateinit var ItemList: List<Item>
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationContext = applicationContext
        sharedPreferences = getSharedPreferences(PREFERENCE_NAME, 0)

        loadPreferences()
    }

    private fun loadPreferences() {

        isAlarmEnabled = sharedPreferences.getBoolean(PREFERENCE_ALARM_ENABLED, false)
        isNotificationEnabled = sharedPreferences.getBoolean(PREFERENCE_NOTIFICATION_ENABLED, false)
        alarmOffTimeStart = sharedPreferences.getInt(PREFERENCE_ALARM_OFFTIME_START, 0)
        alarmOffTimeEnd = sharedPreferences.getInt(PREFERENCE_ALARM_OFFTIME_END, 0)

        Log.d(TAG, "loadPreferences: \n" +
                "AlarmEnabled: $isAlarmEnabled\n" +
                "NotificationEnabled: $isNotificationEnabled\n" +
                "AlarmOffTimeStart: $alarmOffTimeStart\n" +
                "AlarmOffTimeEnd: $alarmOffTimeEnd")

        val itemDAO = ItemDAO(ApplicationContext)
        ItemList = itemDAO.selectAll()
        Log.d(TAG, "loadPreferences: $ItemList")
    }
}
