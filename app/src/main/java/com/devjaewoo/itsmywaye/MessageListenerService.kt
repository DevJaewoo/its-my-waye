package com.devjaewoo.itsmywaye

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MessageListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        if(!isPackageNameInWhiteList(sbn?.packageName)) {
            Log.d(TAG, "onNotificationPosted: Package ${sbn?.packageName} is not in whitelist.")
            return
        }

        val extras = sbn?.notification?.extras

        val extraTitle: String = extras?.get(Notification.EXTRA_TITLE).toString()
        val extraText: String = extras?.get(Notification.EXTRA_TEXT).toString()
        val extraBigText: String = extras?.get(Notification.EXTRA_BIG_TEXT).toString()
        val extraInfoText: String = extras?.get(Notification.EXTRA_INFO_TEXT).toString()
        val extraSubText: String = extras?.get(Notification.EXTRA_SUB_TEXT).toString()
        val extraSummaryText: String = extras?.get(Notification.EXTRA_SUMMARY_TEXT).toString()

        Log.d(
            TAG, "onNotificationPosted:\n" +
                    "PackageName: $packageName\n" +
                    "Title: $extraTitle\n" +
                    "Text: $extraText\n" +
                    "BigText: $extraBigText\n" +
                    "InfoText: $extraInfoText\n" +
                    "SubText: $extraSubText\n" +
                    "SummaryText: $extraSummaryText\n"
        )

        AlarmManager.startAlarm(extraText + extraBigText)
    }

    private fun isPackageNameInWhiteList(packageName: String?): Boolean =
        when {
            packageName == applicationContext.packageName -> false
            ApplicationManager.isNotificationAllowAll -> true
            else -> ApplicationManager.listAllowedPackageName.any { n -> n == packageName }
        }
}