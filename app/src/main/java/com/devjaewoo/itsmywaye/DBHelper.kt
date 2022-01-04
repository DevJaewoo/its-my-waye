package com.devjaewoo.itsmywaye

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        var sql = "CREATE TABLE IF NOT EXISTS alarm_enable (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR2(20) NOT NULL," +
                "enable INTEGER NOT NULL);"

        db?.execSQL(sql)


        sql = "CREATE TABLE IF NOT EXISTS alarm_info (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                ");"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var sql = "DROP TABLE IF EXISTS alarm_enable"
        db?.execSQL(sql)

        sql = "DROP TABLE IF EXISTS alarm_info"
        db?.execSQL(sql)

        onCreate(db)
    }

}