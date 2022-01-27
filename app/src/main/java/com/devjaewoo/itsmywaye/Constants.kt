package com.devjaewoo.itsmywaye

val Any.TAG: String
    get() = if(javaClass.simpleName.length <= 23) javaClass.simpleName else javaClass.simpleName.substring(0, 23)

val String.blankRemovedString: String
    get() = this.replace("\\s".toRegex(), "")

const val PREFERENCE_NAME: String = "ItsMyWaye"
const val DATABASE_NAME: String = "itsmywaye.db"
const val DATABASE_VERSION: Int = 1