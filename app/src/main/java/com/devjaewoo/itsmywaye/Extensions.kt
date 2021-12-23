package com.devjaewoo.itsmywaye

val Any.TAG: String
    get() = if(javaClass.simpleName.length <= 23) javaClass.simpleName else javaClass.simpleName.substring(0, 23)