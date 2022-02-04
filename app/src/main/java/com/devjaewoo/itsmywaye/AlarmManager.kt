package com.devjaewoo.itsmywaye

import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.devjaewoo.itsmywaye.model.Alarm

//알림이 들어왔을 때 알림에서 뭐가 떴는지 확인하고 그게 알림 설정 되어있는지 판단하여 소리 재생
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

    fun startAlarm(message: String) {
        val itemIndex = getItemIndex(message)
        Log.d(TAG, "startAlarm: Item index of $message is $itemIndex")
        if(itemIndex == -1) return

        val item = ApplicationManager.ItemList[itemIndex]
        Log.d(TAG, "startAlarm: Alarm Enabled: ${item.enabled}")
        if(!item.enabled) return

        val alarm = item.alarm ?: Alarm(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString(), 100, 3, 5)

        val alarmIntent = Intent(ApplicationManager.applicationContext, AlarmService::class.java)
        alarmIntent.putExtra(EXTRA_ALARM_URI, alarm.filePath)
        alarmIntent.putExtra(EXTRA_ALARM_VOLUME, alarm.volume)
        alarmIntent.putExtra(EXTRA_ALARM_REPEAT, alarm.repeatTimes)
        alarmIntent.putExtra(EXTRA_ALARM_INTERVAL, alarm.interval)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ApplicationManager.applicationContext.startForegroundService(alarmIntent)
        }
        else {
            ApplicationManager.applicationContext.startService(alarmIntent)
        }
    }
}