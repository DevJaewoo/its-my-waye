package com.devjaewoo.itsmywaye.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.devjaewoo.itsmywaye.DATABASE_NAME
import com.devjaewoo.itsmywaye.DATABASE_VERSION
import com.devjaewoo.itsmywaye.TAG
import com.devjaewoo.itsmywaye.model.Alarm
import com.devjaewoo.itsmywaye.model.Item
import java.io.IOException

class DBHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(TAG, "onCreate: ")

        db?.execSQL(Item.SQL_CREATE_TABLE)
        db?.execSQL(Alarm.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(TAG, "onUpgrade: ")

        db?.execSQL(Item.SQL_DROP_TABLE)
        db?.execSQL(Alarm.SQL_DROP_TABLE)
        onCreate(db)
    }


    fun insert(table: String, values: ContentValues) {
        val db = writableDatabase
        val result = db.insert(table, null, values)

        Log.i(TAG, "insert: $values into table $table")
        
        if(result == -1L) {
            throw IOException("Insert to table $table failed.")
        }
    }


    fun selectAll(table: String): Cursor {
        val query = "SELECT * FROM $table"
        return select(query)
    }

    fun select(table: String, whereClause: String): Cursor {
        val query = "SELECT * FROM $table WHERE $whereClause"
        return select(query)
    }

    fun select(query: String): Cursor {
        val db = readableDatabase
        val result = db.rawQuery(query, null)
        //db.close()

        Log.i(TAG, "select: query: $query")

        return result
    }


    fun update(table: String, values: ContentValues, whereClause: String, whereArg: String) {
        update(table, values, whereClause, arrayOf(whereArg))
    }

    fun update(table: String, values: ContentValues, whereClause: String, whereArgs: Array<String>) {
        val db = writableDatabase
        val result = db.update(table, values, whereClause, whereArgs)

        Log.i(TAG, "update: set $values into table $table, $result rows affected.")
    }


    fun delete(table: String, whereClause: String, whereArg: String) {
        delete(table, whereClause, arrayOf(whereArg))
    }

    fun delete(table: String, whereClause: String, whereArgs: Array<String>) {
        val db = writableDatabase
        val result = db.delete(table, whereClause, whereArgs)

        Log.i(TAG, "delete: from table $table where $whereClause, $result rows affected.")
    }
}