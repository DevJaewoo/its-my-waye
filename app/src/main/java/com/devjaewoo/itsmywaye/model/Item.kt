package com.devjaewoo.itsmywaye.model

import android.provider.BaseColumns

class Item(var name: String, var enabled: Boolean, var alarm: Alarm? = null) {

    var id: Int = -1

    constructor(id: Int, name: String, enabled: Boolean, alarm: Alarm? = null) : this(name, enabled, alarm) {
        this.id = id
    }


    object TableInfo : BaseColumns {
        const val TABLE_NAME = "Item"
        const val COLUMN_NAME_ITEM = "Name"
        const val COLUMN_NAME_ENABLE = "IsEnabled"
        const val COLUMN_NAME_FK_ITEM_ALARM = "FK_${TABLE_NAME}_${Alarm.TableInfo.TABLE_NAME}"
    }

    companion object {
        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ${TableInfo.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TableInfo.COLUMN_NAME_ITEM} VARCHAR2(20) NOT NULL," +
                "${TableInfo.COLUMN_NAME_ENABLE} INTEGER NOT NULL," +
                "${TableInfo.COLUMN_NAME_FK_ITEM_ALARM} INTEGER NOT NULL," +
                "FOREIGN KEY(${TableInfo.COLUMN_NAME_FK_ITEM_ALARM}) " +
                "REFERENCES ${Alarm.TableInfo.TABLE_NAME}(${BaseColumns._ID}) " +
                "ON DELETE SET NULL)"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
    }

    override fun toString(): String {
        return "Item(ID: $id, Name: $name, Enabled: $enabled, Alarm: ${alarm ?: "NULL"})"
    }
}