package com.devjaewoo.itsmywaye.model

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

class Alarm {

    var id: Int = -1
    var filePath: String = ""
    var volume: Int = 100
    var repeatTimes: Int = -1 //반복 횟수
    var interval: Int = 0 //알림이 한번 종료된 후 다음 반복까지의 시간(분), 0: 계속 반복
    var offTimeStart: Int = -1
    var offTimeEnd: Int = -1

    constructor()

    constructor(filePath: String, volume: Int, repeatTimes: Int, interval: Int, offTimeStart: Int, offTimeEnd: Int) {
        this.filePath = filePath
        this.volume = volume
        this.repeatTimes = repeatTimes
        this.interval = interval
        this.offTimeStart = offTimeStart
        this.offTimeEnd = offTimeEnd
    }

    constructor(id: Int, filePath: String, volume: Int, repeatTimes: Int, interval: Int, offTimeStart: Int, offTimeEnd: Int)
            : this(filePath, volume, repeatTimes, interval, offTimeStart, offTimeEnd) {
        this.id = id
    }

    constructor(alarm: Alarm) {
        this.id = alarm.id
        this.filePath = alarm.filePath
        this.volume = alarm.volume
        this.repeatTimes = alarm.repeatTimes
        this.interval = alarm.interval
        this.offTimeStart = alarm.offTimeStart
        this.offTimeEnd = alarm.offTimeEnd
    }

    constructor(cursor: Cursor) {
        var index = 0

        this.id = cursor.getInt(index++)
        this.filePath = cursor.getString(index++)
        this.volume = cursor.getInt(index++)
        this.repeatTimes = cursor.getInt(index++)
        this.interval = cursor.getInt(index++)
        this.offTimeStart = cursor.getInt(index++)
        this.offTimeEnd = cursor.getInt(index)
    }

    object TableInfo : BaseColumns {
        const val TABLE_NAME = "Alarm"
        const val COLUMN_NAME_FILE_PATH = "filePath"
        const val COLUMN_NAME_VOLUME = "Volume"
        const val COLUMN_NAME_REPEAT_TIMES = "RepeatTimes"
        const val COLUMN_NAME_INTERVAL = "Interval"
        const val COLUMN_NAME_OFFTIME_START = "OffTimeStart"
        const val COLUMN_NAME_OFFTIME_END = "OffTimeEnd"
    }

    companion object {
        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ${TableInfo.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TableInfo.COLUMN_NAME_FILE_PATH} VARCHAR(50) NOT NULL," +
                "${TableInfo.COLUMN_NAME_VOLUME} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_REPEAT_TIMES} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_INTERVAL} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_OFFTIME_START} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_OFFTIME_END} INTEGER NOT NULL" +
                ")"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
    }

    override fun toString(): String {
        return "Alarm(ID: $id, FilePath: $filePath, Volume: $volume, RepeatTimes: $repeatTimes, Interval: $interval, OffTime: $offTimeStart ~ $offTimeEnd)"
    }

    fun toContentValues(): ContentValues = ContentValues().apply {
        put(TableInfo.COLUMN_NAME_FILE_PATH, filePath)
        put(TableInfo.COLUMN_NAME_VOLUME, volume)
        put(TableInfo.COLUMN_NAME_REPEAT_TIMES, repeatTimes)
        put(TableInfo.COLUMN_NAME_INTERVAL, interval)
        put(TableInfo.COLUMN_NAME_OFFTIME_START, offTimeStart)
        put(TableInfo.COLUMN_NAME_OFFTIME_END, offTimeEnd)
    }
}
