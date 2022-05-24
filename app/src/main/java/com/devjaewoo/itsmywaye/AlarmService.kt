package com.devjaewoo.itsmywaye

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.*
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.devjaewoo.itsmywaye.model.Alarm

// 여기서 on off on off 다 정해놓고 그대로 알람 틀다가
// Notification에서 Intent 들어오면 종료
class AlarmService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    private lateinit var alarm: Alarm

    lateinit var audioManager: AudioManager
    private var savedVolume: Int = 0
    private var savedRingerMode: Int = 0

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(intent != null) {
            if(intent.action == ACTION_ALARM_ON) {
                alarm = Alarm().apply {
                    filePath = intent.getStringExtra(EXTRA_ALARM_URI) ?: ""
                    volume = intent.getIntExtra(EXTRA_ALARM_VOLUME, 0)
                    vibrate = intent.getBooleanExtra(EXTRA_ALARM_VIBRATE, true)
                    fullscreen = intent.getBooleanExtra(EXTRA_ALARM_FULLSCREEN, false)
                }

                if(RingtoneManager.getRingtone(applicationContext, Uri.parse(alarm.filePath)) == null) {
                    Log.w(TAG, "onStartCommand: Cannot find ringtone at ${alarm.filePath}. Setting uri to default alarm ringtone.")
                    alarm.filePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()
                }

                Log.d(TAG, alarm.toString())

                if(alarm.volume > 0) {
                    mediaPlayer = MediaPlayer.create(this, Uri.parse(alarm.filePath)).apply {
                        stop()
                        isLooping = true
                        setAudioAttributes(
                            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .build()
                        ) //알람 볼륨으로 소리 켬
                        prepare()
                    }
                }

                if(alarm.vibrate) {
                    vibrator = getSystemService(Vibrator::class.java)
                }

                playRingtone()
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
        if(alarm.volume > 0) {
            if (mediaPlayer?.isPlaying == false) {
                changeAudioState(alarm.volume)
                mediaPlayer?.start()
            } else {
                Log.d(TAG, "playRingtone: Alarm is already playing")
            }
        }

        if(alarm.vibrate) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                vibrator?.vibrate(longArrayOf(1000, 500), 0)
            }
            else {
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(
                    longArrayOf(1000, 500),
                    intArrayOf(255, 0),
                    0))
            }
        }
    }

    private fun stopRingtone() {
        Log.d(TAG, "stopRingtone: ")
        if(alarm.volume > 0) {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
                restoreAudioState()
            } else {
                Log.d(TAG, "stopRingtone: Alarm is not playing")
            }
        }

        if(alarm.vibrate) {
            vibrator?.cancel()
        }
    }


    //볼륨은 0 ~ 100 사이의 값이 들어와야 함
    private fun changeAudioState(volume: Int) {

        savedRingerMode = audioManager.ringerMode
        savedVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)

        Log.d(TAG, "changeAudioState: " +
                "SavedVolume: $savedVolume " +
                "MaxVolume: ${audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)} " +
                "CurrentVolume: ${(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) * (volume / 100.0)).toInt()}")

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