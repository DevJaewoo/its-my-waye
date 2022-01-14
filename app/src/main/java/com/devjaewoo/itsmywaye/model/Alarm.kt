package com.devjaewoo.itsmywaye.model

import android.provider.BaseColumns

class Alarm(
    var filePath: String = "/system/media/audio/ringtones/Homecoming.ogg",
    var volume: Int = 100,
    var repeatTimes: Int = 20, //반복 시간(분)
    var interval: Int = 0) { //알림이 한번 종료된 후 다음 반복까지의 시간(분), 0: 계속 반복

    var id: Int = -1

    constructor(id: Int, filePath: String, volume: Int, repeatTimes: Int, interval: Int)
            : this(filePath, volume, repeatTimes, interval) {

        this.id = id
    }

    object TableInfo : BaseColumns {
        const val TABLE_NAME = "Alarm"
        const val COLUMN_NAME_FILE_PATH = "filePath"
        const val COLUMN_NAME_VOLUME = "Volume"
        const val COLUMN_NAME_REPEAT_TIMES = "RepeatTimes"
        const val COLUMN_NAME_INTERVAL = "Interval"
    }

    companion object {
        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ${TableInfo.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TableInfo.COLUMN_NAME_FILE_PATH} VARCHAR(50) NOT NULL," +
                "${TableInfo.COLUMN_NAME_VOLUME} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_REPEAT_TIMES} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_INTERVAL} INTEGER NOT NULL)"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
    }

    override fun toString(): String {
        return "Alarm(ID: $id, FilePath: $filePath, Volume: $volume, RepeatTimes: $repeatTimes, Interval: $interval)"
    }
}
