package com.devjaewoo.itsmywaye.dao

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.devjaewoo.itsmywaye.TAG
import com.devjaewoo.itsmywaye.database.DBHelper
import com.devjaewoo.itsmywaye.model.Alarm

class AlarmDAO(val context: Context) {

    fun insert(alarm: Alarm): Int {
        val db = DBHelper(context)
        val result = db.insert(Alarm.TableInfo.TABLE_NAME, ContentValues().apply {
            put(Alarm.TableInfo.COLUMN_NAME_FILE_PATH, alarm.filePath)
            put(Alarm.TableInfo.COLUMN_NAME_VOLUME, alarm.volume)
            put(Alarm.TableInfo.COLUMN_NAME_REPEAT_TIMES, alarm.repeatTimes)
            put(Alarm.TableInfo.COLUMN_NAME_INTERVAL, alarm.interval)
        })

        return result
    }

    fun selectAll(): List<Alarm> {
        val db = DBHelper(context)
        val cursor = db.selectAll(Alarm.TableInfo.TABLE_NAME)
        val list: ArrayList<Alarm> = ArrayList()

        while(cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val filePath = cursor.getString(1)
            val volume = cursor.getInt(2)
            val repeatTimes = cursor.getInt(3)
            val interval = cursor.getInt(4)
            val alarm = Alarm(id, filePath, volume, repeatTimes, interval)

            Log.d(TAG, "selectAll: $alarm")
            list.add(alarm)
        }

        return list
    }

    fun select(whereClause: String): Alarm? {
        val db = DBHelper(context)
        val cursor = db.select(Alarm.TableInfo.TABLE_NAME, whereClause)

        if(cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val filePath = cursor.getString(1)
            val volume = cursor.getInt(2)
            val repeatTimes = cursor.getInt(3)
            val interval = cursor.getInt(4)
            val alarm = Alarm(id, filePath, volume, repeatTimes, interval)

            Log.d(TAG, "selectAll: $alarm")
            return alarm
        }
        else {
            return null
        }
    }

    fun update(alarm: Alarm) {
        val db = DBHelper(context)
        val values = ContentValues().apply {
            put(Alarm.TableInfo.COLUMN_NAME_FILE_PATH, alarm.filePath)
            put(Alarm.TableInfo.COLUMN_NAME_VOLUME, alarm.volume)
            put(Alarm.TableInfo.COLUMN_NAME_REPEAT_TIMES, alarm.repeatTimes)
            put(Alarm.TableInfo.COLUMN_NAME_INTERVAL, alarm.interval)
        }

        db.update(Alarm.TableInfo.TABLE_NAME, values, "${BaseColumns._ID} = ?", alarm.id.toString())
    }

    fun delete(id: Int) {
        val db = DBHelper(context)
        db.delete(Alarm.TableInfo.TABLE_NAME, "${BaseColumns._ID} = ?", id.toString())
    }
}