package com.devjaewoo.itsmywaye.dao

import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.devjaewoo.itsmywaye.TAG
import com.devjaewoo.itsmywaye.database.DBHelper
import com.devjaewoo.itsmywaye.model.Alarm

class AlarmDAO(val context: Context) {

    fun selectAll(): List<Alarm> {
        val db = DBHelper(context)
        val cursor = db.selectAll(Alarm.TableInfo.TABLE_NAME)
        val list: ArrayList<Alarm> = ArrayList()

        while(cursor.moveToNext()) {
            val alarm = Alarm(cursor)
            Log.d(TAG, "selectAll: $alarm")
            list.add(alarm)
        }

        return list
    }

    fun select(whereClause: String): Alarm? {
        val db = DBHelper(context)
        val cursor = db.select(Alarm.TableInfo.TABLE_NAME, whereClause)

        if(cursor.moveToNext()) {
            val alarm = Alarm(cursor)
            Log.d(TAG, "selectAll: $alarm")
            return alarm
        }
        else {
            return null
        }
    }

    fun insert(alarm: Alarm): Int {
        val db = DBHelper(context)
        return db.insert(Alarm.TableInfo.TABLE_NAME, alarm.toContentValues())
    }

    fun update(alarm: Alarm) {
        val db = DBHelper(context)
        db.update(Alarm.TableInfo.TABLE_NAME, alarm.toContentValues(), "${BaseColumns._ID} = ?", alarm.id.toString())
    }

    fun delete(id: Int) {
        val db = DBHelper(context)
        db.delete(Alarm.TableInfo.TABLE_NAME, "${BaseColumns._ID} = ?", id.toString())
    }
}