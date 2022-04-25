package com.devjaewoo.itsmywaye

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.*
import android.net.Uri
import android.os.IBinder
import android.util.Log

// 여기서 on off on off 다 정해놓고 그대로 알람 틀다가
// Notification에서 Intent 들어오면 종료
class AlarmService : Service() {

    private var ringtone: Ringtone? = null
    private var mediaPlayer: MediaPlayer? = null

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

                if (mediaPlayer == null || !(mediaPlayer!!.isPlaying)) {
                    if(RingtoneManager.getRingtone(applicationContext, Uri.parse(uri)) == null) {
                        Log.w(TAG, "onStartCommand: Cannot find ringtone at $uri. Setting uri to default alarm ringtone.")
                        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()
                    }

                    mediaPlayer = MediaPlayer.create(this, Uri.parse(uri)).apply {
                        stop()
                        isLooping = true
                        setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()) //알람 볼륨으로 소리 켬
                        prepare()
                    }

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
        if(mediaPlayer?.isPlaying == false) {
            changeAudioState(volume)
            mediaPlayer?.start()
        }
        else {
            Log.d(TAG, "playRingtone: Alarm is already playing")
        }
    }

    private fun stopRingtone() {
        Log.d(TAG, "stopRingtone: ")
        if(mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            restoreAudioState()
        }
        else {
            Log.d(TAG, "stopRingtone: Alarm is not playing")
        }
    }


    //볼륨은 0 ~ 100 사이의 값이 들어와야 함
    private fun changeAudioState(volume: Int) {

        savedRingerMode = audioManager.ringerMode
        savedVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)

        Log.d(TAG, "changeAudioState: SavedVolume: $savedVolume MaxVolume: ${audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)} CurrentVolume: ${(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) * (volume / 100.0)).toInt()}")

        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        audioManager.setStreamVolume(
            AudioManager.STREAM_NOTIFICATION,
            (audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) * (volume / 100.0)).toInt(),
            0)
    }

    private fun restoreAudioState() {
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, savedVolume, 0)
        audioManager.ringerMode = savedRingerMode
    }
}