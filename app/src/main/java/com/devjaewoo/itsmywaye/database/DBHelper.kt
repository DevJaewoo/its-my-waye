package com.devjaewoo.itsmywaye.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.devjaewoo.itsmywaye.*
import com.devjaewoo.itsmywaye.model.Alarm
import com.devjaewoo.itsmywaye.model.Item
import java.io.IOException

// Database
const val DATABASE_NAME: String = "itsmywaye.db"
const val DATABASE_VERSION: Int = 4

class DBHelper(val context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(TAG, "onCreate: ")

        db?.execSQL(Item.SQL_DROP_TABLE)
        db?.execSQL(Alarm.SQL_DROP_TABLE)

        db?.execSQL(Item.SQL_CREATE_TABLE)
        db?.execSQL(Alarm.SQL_CREATE_TABLE)

        context.resources.getStringArray(R.array.itemList).forEach {
            val formattedString = it.blankRemovedString
            db?.insert(Item.TableInfo.TABLE_NAME, null, ContentValues().apply {
                put(Item.TableInfo.COLUMN_NAME_ITEM, formattedString)
                put(Item.TableInfo.COLUMN_NAME_ENABLE, 0)
                put(Item.TableInfo.COLUMN_NAME_FK_ITEM_ALARM, "NULL")
            })
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(TAG, "onUpgrade: ")
        for(version in oldVersion until newVersion) {
            migrate(db, version)
        }
    }

    //다음 버전으로 DB 이전
    private fun migrate(db: SQLiteDatabase?, oldVersion: Int) {
        Log.i(TAG, "migrate: Migrate DB ver.$oldVersion to ver.${oldVersion + 1}")
        when(oldVersion) {
            1 -> { //에버그레이스 추가
                db?.insert(Item.TableInfo.TABLE_NAME, null, ContentValues().apply {
                    put(Item.TableInfo.COLUMN_NAME_ITEM, "에버그레이스".blankRemovedString)
                    put(Item.TableInfo.COLUMN_NAME_ENABLE, 0)
                    put(Item.TableInfo.COLUMN_NAME_FK_ITEM_ALARM, "NULL")
                })
            }

            2 -> { //Alarm에 OffTime 추가
                db?.execSQL("ALTER TABLE ALARM ADD COLUMN ${Alarm.TableInfo.COLUMN_NAME_OFFTIME_START} INTEGER DEFAULT 0") //ALTER TABLE은 NOT NULL 컬럼 추가가 안된다고 함
                db?.execSQL("ALTER TABLE ALARM ADD COLUMN ${Alarm.TableInfo.COLUMN_NAME_OFFTIME_END} INTEGER DEFAULT 0")
            }

            3 -> { //RepeatTimes를 Vibrate로, Interval을 Fullscreen으로 변경
//                db?.execSQL("ALTER TABLE ALARM RENAME COLUMN RepeatTimes TO ${Alarm.TableInfo.COLUMN_NAME_VIBRATE}")
//                db?.execSQL("ALTER TABLE ALARM RENAME COLUMN Interval TO ${Alarm.TableInfo.COLUMN_NAME_FULLSCREEN}")
                db?.execSQL(Alarm.SQL_DROP_TABLE)
                db?.execSQL(Alarm.SQL_CREATE_TABLE)
            }
        }
    }


    fun insert(table: String, values: ContentValues): Int {
        val db = writableDatabase
        val result = db.insert(table, null, values)

        Log.i(TAG, "insert: $values into table $table")
        
        if(result == -1L) {
            throw IOException("Insert to table $table failed.")
        }

        return result.toInt()
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