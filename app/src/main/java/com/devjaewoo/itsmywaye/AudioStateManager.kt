package com.devjaewoo.itsmywaye

import android.media.AudioManager
import android.util.Log

class AudioStateManager {

    private val audioManager: AudioManager = MainActivity.audioManager

    private var ringerMode: Int = 0
    private var volume: Int = 0

    //볼륨은 0 ~ 100 사이의 값이 들어와야 함
    fun changeAudioState(volume: Int) {

        this.volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
            (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * (volume / 100.0)).toInt(),
            0)
            //AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
    }

    fun restoreAudioState() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
        audioManager.ringerMode = ringerMode
    }
}