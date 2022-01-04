package com.devjaewoo.itsmywaye

class Alarm(
    var filePath: String = "/system/media/audio/ringtones/Homecoming.ogg",
    var volume: Int = 100,
    var onTime: Int = 20 * 60 * 1000)
