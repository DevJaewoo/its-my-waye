package com.devjaewoo.itsmywaye

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AlarmService : Service() {

    var audioStateManager: AudioStateManager? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}