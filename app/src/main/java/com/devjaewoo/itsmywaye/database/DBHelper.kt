package com.devjaewoo.itsmywaye.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.devjaewoo.itsmywaye.model.Alarm
import com.devjaewoo.itsmywaye.model.Item

class DBHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Item.SQL_CREATE_TABLE)
        db?.execSQL(Alarm.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(Item.SQL_DROP_TABLE)
        db?.execSQL(Alarm.SQL_DROP_TABLE)
        onCreate(db)
    }

    fun insert(table: String, values: ContentValues) {
        val db = writableDatabase
        db.insert(table, null, values)
    }
}