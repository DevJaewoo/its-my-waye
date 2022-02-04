package com.devjaewoo.itsmywaye

import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.devjaewoo.itsmywaye.dao.ItemDAO
import com.devjaewoo.itsmywaye.model.Alarm

class AlarmService : Service() {

    var audioStateManager: AudioStateManager? = null
    var uri: String = ""
    var volume: Int = 100
    var repeatTimes: Int = 3
    var interval: Int = 5

    lateinit var ringtone: Ringtone

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        uri = intent?.getStringExtra(EXTRA_ALARM_URI) ?: ""
        volume = intent?.getIntExtra(EXTRA_ALARM_URI, 100) ?: 100
        repeatTimes = intent?.getIntExtra(EXTRA_ALARM_URI, 3) ?: 3
        interval = intent?.getIntExtra(EXTRA_ALARM_URI, 5) ?: 5

        ringtone = RingtoneManager.getRingtone(applicationContext, Uri.parse(uri))
            ?: RingtoneManager.getRingtone(applicationContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

        playRingtone()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun playRingtone() {
        ringtone.play()
    }

    private fun stopRingtone() {
        ringtone.stop()
    }
}