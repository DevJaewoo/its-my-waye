package com.devjaewoo.itsmywaye.dao

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.devjaewoo.itsmywaye.TAG
import com.devjaewoo.itsmywaye.database.DBHelper
import com.devjaewoo.itsmywaye.model.Alarm
import com.devjaewoo.itsmywaye.model.Item

class ItemDAO(val context: Context) {

    fun insert(item: Item): Int {
        val db = DBHelper(context)
        val result = db.insert(Item.TableInfo.TABLE_NAME, ContentValues().apply {
            put(Item.TableInfo.COLUMN_NAME_ITEM, item.name)
            put(Item.TableInfo.COLUMN_NAME_ENABLE, if(item.enabled) 1 else 0)
            put(Item.TableInfo.COLUMN_NAME_FK_ITEM_ALARM, item.alarm?.id.toString())
        })

        return result
    }

    fun selectAll(): List<Item> {
        val db = DBHelper(context)
        val cursor = db.selectAll(Item.TableInfo.TABLE_NAME)
        val list: ArrayList<Item> = ArrayList()
        val alarmDAO = AlarmDAO(context)

        while(cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val isEnabled = cursor.getInt(2) == 1
            var alarm: Alarm? = null

            if(!cursor.getString(3).equals("NULL")) {
                alarm = alarmDAO.select("${BaseColumns._ID} = ${cursor.getInt(3)}")
            }

            list.add(Item(id, name, isEnabled, alarm))
        }

        return list
    }

    fun select(whereClause: String): Item? {
        val db = DBHelper(context)
        val cursor = db.select(Item.TableInfo.TABLE_NAME, whereClause)
        val alarmDAO = AlarmDAO(context)

        if(cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val isEnabled = cursor.getInt(2) == 1
            var alarm: Alarm? = null

            if(!cursor.getString(3).equals("NULL")) {
                alarm = alarmDAO.select("${BaseColumns._ID} = ${cursor.getInt(3)}")
            }

            return Item(id, name, isEnabled, alarm)
        }
        else {
            return null
        }
    }

    fun update(item: Item) {
        val db = DBHelper(context)
        val values = ContentValues().apply {
            put(Item.TableInfo.COLUMN_NAME_ITEM, item.name)
            put(Item.TableInfo.COLUMN_NAME_ENABLE, if(item.enabled) 1 else 0)
            put(Item.TableInfo.COLUMN_NAME_FK_ITEM_ALARM, item.alarm?.id.toString())
        }

        db.update(Item.TableInfo.TABLE_NAME, values, "${BaseColumns._ID} = ?", item.id.toString())
    }

    fun delete(id: Int) {
        val db = DBHelper(context)
        db.delete(Item.TableInfo.TABLE_NAME, "${BaseColumns._ID} = ?", id.toString())
    }
}