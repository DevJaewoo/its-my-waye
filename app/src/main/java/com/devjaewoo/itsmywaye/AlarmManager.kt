package com.devjaewoo.itsmywaye

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.devjaewoo.itsmywaye.model.Alarm
import java.util.*

object AlarmManager {

    /**
     * 알림 메시지 확인 후 받고자 하는 알림 안에 있는지 확인
     * 있을 경우: Index 반환
     * 없을 경우: -1 반환
     */
    private fun getItemIndex(message: String): Int {
        val formattedMessage = message.blankRemovedString
        val itemList = ApplicationManager.ItemList

        for(i in itemList.indices) {
            if(formattedMessage.contains(itemList[i].name.blankRemovedString)) {
                Log.d(TAG, "getAlarmIndex: Message $message contains item ${itemList[i].name}")
                return i
            }
        }

        return -1
    }

    //현재 알람 금지 시간대인지 판별
    fun isAlarmEnabled(hour: Int): Boolean {
        if(!ApplicationManager.isAlarmEnabled) return false

        val alarmOffTimeStart = ApplicationManager.alarmOffTimeStart
        var alarmOffTimeEnd = ApplicationManager.alarmOffTimeEnd
        var currentTime = hour

        if(alarmOffTimeStart > alarmOffTimeEnd) alarmOffTimeEnd += 24
        if(alarmOffTimeStart > currentTime) currentTime += 24

        return currentTime !in alarmOffTimeStart until alarmOffTimeEnd
    }

    fun startAlarm(message: String) {

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if(!isAlarmEnabled(currentHour)) {
            Log.d(TAG, "startAlarm: Alarm is disabled at $currentHour.")
            return
        }

        val itemIndex = getItemIndex(message)
        Log.d(TAG, "startAlarm: Item index of $message is $itemIndex")
        if(itemIndex == -1) return

        val item = ApplicationManager.ItemList[itemIndex]
        Log.d(TAG, "startAlarm: Alarm Enabled: ${item.enabled}")
        if(!item.enabled) return

        val alarm = item.alarm ?: Alarm(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString(), 100,
            vibrate = false,
            fullscreen = false,
            offTimeStart = 0,
            offTimeEnd = 0
        )

        val alarmIntent = Intent(ApplicationManager.applicationContext, AlarmService::class.java).apply {
            action = ACTION_ALARM_ON
            putExtra(EXTRA_ALARM_URI, alarm.filePath)
            putExtra(EXTRA_ALARM_VOLUME, alarm.volume)
            putExtra(EXTRA_ALARM_REPEAT, alarm.vibrate)
            putExtra(EXTRA_ALARM_INTERVAL, alarm.fullscreen)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ApplicationManager.applicationContext.startService(alarmIntent)
        }
        else {
            ApplicationManager.applicationContext.startService(alarmIntent)
        }

        createNotification(item.name)
    }

    private fun createNotification(content: String) {
        val intent = Intent(ApplicationManager.applicationContext, AlarmService::class.java).apply {
            action = ACTION_ALARM_OFF
            putExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_DEFAULT_ID)
        }

        val pendingIntent: PendingIntent = PendingIntent.getService(
            ApplicationManager.applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)

        val notificationManager = ApplicationManager.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(ApplicationManager.applicationContext, NOTIFICATION_DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
            .setContentTitle("마이웨이")
            .setContentText(content)
            .addAction(R.drawable.ic_baseline_access_alarm_24, "알람 끄기", pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .build()

        Log.d(TAG, "createNotification: $APPLICATION_NAME/$content")
        notificationManager.notify(NOTIFICATION_DEFAULT_ID, notification)
    }
}