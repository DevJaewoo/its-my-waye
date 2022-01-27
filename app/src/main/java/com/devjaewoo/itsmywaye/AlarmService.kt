package com.devjaewoo.itsmywaye

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

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

    /**
     * 알림 메시지 확인 후 받고자 하는 알림 안에 있는지 확인
     * 있을 경우: Index 반환
     * 없을 경우: -1 반환
     */
    private fun getAlarmIndex(message: String): Int {
        val formattedMessage = message.blankRemovedString
        val itemList = resources.getStringArray(R.array.itemList)

        for(i in itemList.indices) {
            if(formattedMessage.contains(itemList[i].blankRemovedString)) {
                Log.d(TAG, "getAlarmIndex: Message $message contains item ${itemList[i]}")
                return i
            }
        }

        return -1
    }

}