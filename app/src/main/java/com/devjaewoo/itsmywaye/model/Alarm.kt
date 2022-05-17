package com.devjaewoo.itsmywaye.model

import android.content.ContentValues
import android.database.Cursor
import android.media.RingtoneManager
import android.provider.BaseColumns

class Alarm {

    var id: Int = -1
    var filePath: String = ""
    var volume: Int = 100
    var vibrate: Boolean = false
    var fullscreen: Boolean = false
    var offTimeStart: Int = -1
    var offTimeEnd: Int = -1

    constructor()

    constructor(filePath: String, volume: Int, vibrate: Boolean, fullscreen: Boolean, offTimeStart: Int, offTimeEnd: Int) {
        this.filePath = filePath
        this.volume = volume
        this.vibrate = vibrate
        this.fullscreen = fullscreen
        this.offTimeStart = offTimeStart
        this.offTimeEnd = offTimeEnd
    }

    constructor(id: Int, filePath: String, volume: Int, vibrate: Boolean, fullscreen: Boolean, offTimeStart: Int, offTimeEnd: Int)
            : this(filePath, volume, vibrate, fullscreen, offTimeStart, offTimeEnd) {
        this.id = id
    }

    constructor(alarm: Alarm) {
        this.id = alarm.id
        this.filePath = alarm.filePath
        this.volume = alarm.volume
        this.vibrate = alarm.vibrate
        this.fullscreen = alarm.fullscreen
        this.offTimeStart = alarm.offTimeStart
        this.offTimeEnd = alarm.offTimeEnd
    }

    constructor(cursor: Cursor) {
        var index = 0

        this.id = cursor.getInt(index++)
        this.filePath = cursor.getString(index++)
        this.volume = cursor.getInt(index++)
        this.vibrate = (cursor.getInt(index++) == 1)
        this.fullscreen = (cursor.getInt(index++) == 1)
        this.offTimeStart = cursor.getInt(index++)
        this.offTimeEnd = cursor.getInt(index)
    }

    object TableInfo : BaseColumns {
        const val TABLE_NAME = "Alarm"
        const val COLUMN_NAME_FILE_PATH = "filePath"
        const val COLUMN_NAME_VOLUME = "Volume"
        const val COLUMN_NAME_VIBRATE = "Vibrate"
        const val COLUMN_NAME_FULLSCREEN = "Fullscreen"
        const val COLUMN_NAME_OFFTIME_START = "OffTimeStart"
        const val COLUMN_NAME_OFFTIME_END = "OffTimeEnd"
    }

    companion object {
        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ${TableInfo.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TableInfo.COLUMN_NAME_FILE_PATH} VARCHAR(50) NOT NULL," +
                "${TableInfo.COLUMN_NAME_VOLUME} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_VIBRATE} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_FULLSCREEN} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_OFFTIME_START} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_OFFTIME_END} INTEGER NOT NULL" +
                ")"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
    }

    override fun toString(): String {
        return "Alarm(ID: $id, FilePath: $filePath, Volume: $volume, Vibrate: $vibrate, Fullscreen: $fullscreen, OffTime: $offTimeStart ~ $offTimeEnd)"
    }

    fun toContentValues(): ContentValues = ContentValues().apply {
        put(TableInfo.COLUMN_NAME_FILE_PATH, filePath)
        put(TableInfo.COLUMN_NAME_VOLUME, volume)
        put(TableInfo.COLUMN_NAME_VIBRATE, vibrate)
        put(TableInfo.COLUMN_NAME_FULLSCREEN, fullscreen)
        put(TableInfo.COLUMN_NAME_OFFTIME_START, offTimeStart)
        put(TableInfo.COLUMN_NAME_OFFTIME_END, offTimeEnd)
    }
}
