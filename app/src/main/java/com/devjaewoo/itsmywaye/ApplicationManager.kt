package com.devjaewoo.itsmywaye

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Build
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                if(get(Calendar.MINUTE) > 55) set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY + 1))
                set(Calendar.MINUTE, 55)
                set(Calendar.SECOND, 0)
                Log.d(TAG, "onCreate: ${get(Calendar.MINUTE)}")
            }

            val intent = Intent(ApplicationManager.applicationContext, AlarmService::class.java).apply {
                action = ACTION_ALARM_OFF
                putExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_DEFAULT_ID)
            }

            val pendingIntent: PendingIntent = PendingIntent.getService(
                applicationContext,
                1,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            alarmManager?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_HOUR,
                pendingIntent
            )
        }
    }

    private fun loadPreferences() {

        isAlarmEnabled = sharedPreferences.getBoolean(PREFERENCE_ALARM_ENABLED, true)
        isAllNotificationAllowed = sharedPreferences.getBoolean(PREFERENCE_NOTIFICATION_ALLOW_ALL, false)
        isDiscordNotificationAllowed = sharedPreferences.getBoolean(PREFERENCE_NOTIFICATION_ALLOW_DISCORD, true)
        isKakaotalkNotificationAllowed = sharedPreferences.getBoolean(PREFERENCE_NOTIFICATION_ALLOW_KAKAOTALK, false)

        Log.d(TAG, "loadPreferences: \n" +
                "AlarmEnabled: $isAlarmEnabled\n" +
                "AllNotificationAllowed: $isAllNotificationAllowed\n" +
                "DiscordNotificationAllowed: $isDiscordNotificationAllowed\n" +
                "KakaotalkNotificationAllowed: $isKakaotalkNotificationAllowed\n")

        ItemNameList = applicationContext.resources.getStringArray(R.array.itemList).toList()

        val itemDAO = ItemDAO(applicationContext)
        ItemList = itemDAO.selectAll()
        Log.d(TAG, "loadPreferences: $ItemList")
    }
}
