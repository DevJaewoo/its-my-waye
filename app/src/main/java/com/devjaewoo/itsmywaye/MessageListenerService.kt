package com.devjaewoo.itsmywaye

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MessageListenerService : NotificationListenerService() {

    companion object {
        var allowAllPackages: Boolean = false
        var listAllowedPackageName: ArrayList<String> = ArrayList(arrayListOf("com.discord", "com.kakao.talk"))
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        if(!isPackageNameInWhiteList(sbn?.packageName)) {
            Log.d(TAG, "onNotificationPosted: Package ${sbn?.packageName} is not in whitelist.")
            return
        }
    }

    private fun isPackageNameInWhiteList(packageName: String?): Boolean {
        return if (allowAllPackages) true else listAllowedPackageName.any { n -> n == packageName }
    }
}