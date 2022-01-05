package com.devjaewoo.itsmywaye.model

import android.provider.BaseColumns

class Item(
    var name: String,
    var enabled: Boolean) {

    object TableInfo : BaseColumns {
        const val TABLE_NAME = "Item"
        const val COLUMN_NAME_ITEM = "Name"
        const val COLUMN_NAME_ENABLE = "IsEnabled"
    }

    companion object {
        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ${TableInfo.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TableInfo.COLUMN_NAME_ITEM} VARCHAR2(20) NOT NULL," +
                "${TableInfo.COLUMN_NAME_ENABLE} INTEGER NOT NULL)"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
    }
}