package com.devjaewoo.itsmywaye

val Any.TAG: String
    get() = if(javaClass.simpleName.length <= 23) javaClass.simpleName else javaClass.simpleName.substring(0, 23)

val String.blankRemovedString: String
    get() = this.replace("\\s".toRegex(), "")

const val APPLICATION_NAME: String = "It's My Waye"

const val PREFERENCE_NAME: String = "ItsMyWaye"


const val DATABASE_NAME: String = "itsmywaye.db"
const val DATABASE_VERSION: Int = 1


const val NOTIFICATION_DEFAULT_CHANNEL_ID: String = "default"
const val NOTIFICATION_DEFAULT_CHANNEL_NAME: String = "default"
const val NOTIFICATION_DEFAULT_ID: Int = 333333333  // Notification.IMPORTANCE_DEFAULT = 3

const val EXTRA_NOTIFICATION_ID: String = "com.devjaewoo.itsmywaye.extra.EXTRA_NOTIFICATION_ID"


const val ACTION_ALARM_ON: String = "com.devjaewoo.itsmywaye.extra.ACTION_ALARM_ON"
const val ACTION_ALARM_OFF: String = "com.devjaewoo.itsmywaye.extra.ACTION_ALARM_OFF"

const val EXTRA_ALARM_URI: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_URI"
const val EXTRA_ALARM_VOLUME: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_VOLUME"
const val EXTRA_ALARM_REPEAT: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_REPEAT"
const val EXTRA_ALARM_INTERVAL: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_INTERVAL"