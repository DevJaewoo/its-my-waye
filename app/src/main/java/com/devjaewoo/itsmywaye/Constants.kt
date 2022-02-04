package com.devjaewoo.itsmywaye

val Any.TAG: String
    get() = if(javaClass.simpleName.length <= 23) javaClass.simpleName else javaClass.simpleName.substring(0, 23)

val String.blankRemovedString: String
    get() = this.replace("\\s".toRegex(), "")

const val PREFERENCE_NAME: String = "ItsMyWaye"
const val DATABASE_NAME: String = "itsmywaye.db"
const val DATABASE_VERSION: Int = 1

const val EXTRA_ALARM_URI: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_URI"
const val EXTRA_ALARM_VOLUME: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_VOLUME"
const val EXTRA_ALARM_REPEAT: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_REPEAT"
const val EXTRA_ALARM_INTERVAL: String = "com.devjaewoo.itsmywaye.extra.EXTRA_ALARM_INTERVAL"