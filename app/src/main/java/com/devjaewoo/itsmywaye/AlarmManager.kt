package com.devjaewoo.itsmywaye

import android.content.Intent
import com.devjaewoo.itsmywaye.model.Alarm

//알림이 들어왔을 때 알림에서 뭐가 떴는지 확인하고 그게 알림 설정 되어있는지 판단하여 소리 재생
object AlarmManager {



    private fun startAlarm(alarm: Alarm) {
        val alarmIntent: Intent = Intent(ApplicationManager.ApplicationContext, AlarmService::class.java)
    }
}