package com.devjaewoo.itsmywaye

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.util.Log

// 여기서 on off on off 다 정해놓고 그대로 알람 틀다가
// Notification에서 Intent 들어오면 종료
class AlarmService : Service() {

    private var ringtone: Ringtone? = null

    var uri: String = ""
    var volume: Int = 100
    var repeatTimes: Int = 3
    var interval: Int = 5

    lateinit var audioManager: AudioManager
    var savedVolume: Int = 0
    var savedRingerMode: Int = 0

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        //startForeground(1000, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(intent != null) {
            if(intent.action == ACTION_ALARM_ON) {
                uri = intent.getStringExtra(EXTRA_ALARM_URI) ?: ""
                volume = intent.getIntExtra(EXTRA_ALARM_VOLUME, 100)
                repeatTimes = intent.getIntExtra(EXTRA_ALARM_REPEAT, 3)
                interval = intent.getIntExtra(EXTRA_ALARM_INTERVAL, 5)

                Log.d(TAG, "onStartCommand: Alarm Received: uri: $uri, volume: $volume, repeatTimes: $repeatTimes, interval: $interval")

                if (ringtone?.isPlaying != true) {
                    ringtone = RingtoneManager.getRingtone(applicationContext, Uri.parse(uri))
                        ?: RingtoneManager.getRingtone(
                            applicationContext,
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        )

                    playRingtone()
                }
            }
            else {
                val notificationID: Int = intent.getIntExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_DEFAULT_ID)
                val notificationManager = ApplicationManager.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationID)
                stopRingtone()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    private fun playRingtone() {
        Log.d(TAG, "playRingtone: ")
        if(ringtone?.isPlaying == false) {
            changeAudioState(volume)
            ringtone?.play()
        }
    }

    private fun stopRingtone() {
        Log.d(TAG, "stopRingtone: ")
        if(ringtone?.isPlaying == true) {
            ringtone?.stop()
            restoreAudioState()
        }
    }


    //볼륨은 0 ~ 100 사이의 값이 들어와야 함
    private fun changeAudioState(volume: Int) {

        savedRingerMode = audioManager.ringerMode
        savedVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)

        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        audioManager.setStreamVolume(
            AudioManager.STREAM_ALARM,
            (audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) * (volume / 100.0)).toInt(),
            0)
        //AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
    }

    private fun restoreAudioState() {
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, savedVolume, 0)
        audioManager.ringerMode = savedRingerMode
    }
}