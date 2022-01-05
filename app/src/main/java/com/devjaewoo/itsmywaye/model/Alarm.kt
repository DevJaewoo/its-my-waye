package com.devjaewoo.itsmywaye.model

import android.provider.BaseColumns

class Alarm(
    var path: String = "/system/media/audio/ringtones/Homecoming.ogg",
    var volume: Int = 100,
    var repeat: Int = 0,
    var delay: Int = 20 * 60 * 1000) {

    object TableInfo : BaseColumns {
        const val TABLE_NAME = "Alarm"
        const val COLUMN_NAME_PATH = "FilePath"
        const val COLUMN_NAME_VOLUME = "Volume"
        const val COLUMN_NAME_REPEAT = "RepeatTimes"
        const val COLUMN_NAME_DELAY = "RepeatInterval"
        const val COLUMN_NAME_FK_ALARM_ITEM = "FK_${TABLE_NAME}_${Item.TableInfo.TABLE_NAME}"
    }

    companion object {
        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ${TableInfo.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TableInfo.COLUMN_NAME_PATH} VARCHAR(50) NOT NULL" +
                "${TableInfo.COLUMN_NAME_VOLUME} INTEGER NOT NULL" +
                "${TableInfo.COLUMN_NAME_REPEAT} INTEGER NOT NULL" +
                "${TableInfo.COLUMN_NAME_DELAY} INTEGER NOT NULL)" +
                "FOREIGN KEY(${TableInfo.COLUMN_NAME_FK_ALARM_ITEM}) " +
                "REFERENCES ${Item.TableInfo.TABLE_NAME}(${BaseColumns._ID}) " +
                "ON DELETE CASCADE)"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
    }
}
