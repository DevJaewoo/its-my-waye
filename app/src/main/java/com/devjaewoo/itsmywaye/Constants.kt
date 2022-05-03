package com.devjaewoo.itsmywaye

// Functions
val Any.TAG: String
    get() = if(javaClass.simpleName.length <= 23) javaClass.simpleName else javaClass.simpleName.substring(0, 23)

val String.blankRemovedString: String
    get() = this.replace("\\s".toRegex(), "")

// Application
const val APPLICATION_NAME: String = "It's My Waye"

// Activity Results
const val RESULT_CLOSE: Int = 99

// Preferences
const val PREFERENCE_NAME: String = "ItsMyWaye"

const val PREFERENCE_ALARM_ENABLED = "alarm_enabled"
const val PREFERENCE_ALARM_OFFTIME_START = "alarm_offtime_start"
const val PREFERENCE_ALARM_OFFTIME_END = "alarm_offtime_end"
const val PREFERENCE_NOTIFICATION_ALLOW_ALL = "notification_allow_all"
const val PREFERENCE_NOTIFICATION_ALLOW_DISCORD = "notification_allow_discord"
const val PREFERENCE_NOTIFICATION_ALLOW_KAKAOTALK = "notification_allow_kakaotalk"

// Database
const val DATABASE_NAME: String = "itsmywaye.db"
const val DATABASE_VERSION: Int = 3

// Notification Read
const val PACKAGE_NAME_DISCORD: String = "com.discord"
const val PACKAGE_NAME_KAKAOTALK: String = "com.kakao.talk"

// Notification Write
const val NOTIFICATION_DEFAULT_CHANNEL_ID: String = "default"
const val NOTIFICATION_DEFAULT_CHANNEL_NAME: String = "default"
const val NOTIFICATION_DEFAULT_ID: Int = 333333333  // Notification.IMPORTANCE_DEFAULT = 3

const val EXTRA_NOTIFICATION_ID: String = "com.devjaewoo.itsmywaye.extra.EXTRA_NOTIFICATION_ID"

// Alarm
const val ACTION_ALARM_ON: String = "com.devjaewoo.itsmywaye.extra.ACTION_ALARM_ON"
const val ACTION_ALARM_OFF: String = "com.devjaewoo.itsmywaye.extra.ACTION_ALARM_OFF"

const val EXTRA_ALARM_URI: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_URI"
const val EXTRA_ALARM_VOLUME: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_VOLUME"
const val EXTRA_ALARM_REPEAT: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_REPEAT"
const val EXTRA_ALARM_INTERVAL: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_INTERVAL"