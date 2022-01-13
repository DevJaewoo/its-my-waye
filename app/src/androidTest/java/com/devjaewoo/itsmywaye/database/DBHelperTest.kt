package com.devjaewoo.itsmywaye.database

import android.content.ContentValues
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.devjaewoo.itsmywaye.DATABASE_NAME
import com.devjaewoo.itsmywaye.TAG
import com.devjaewoo.itsmywaye.model.Item
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DBHelperTest {
    private lateinit var db: DBHelper

    @Before
    fun setUp() {
        Log.d(TAG, "setUp: ")
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(DATABASE_NAME)
        db = DBHelper(context)
    }

    @After
    fun tearDown() {
        Log.d(TAG, "tearDown: ")
        db.close()
    }

    private fun insertTestData() {
        db.insert(Item.TableInfo.TABLE_NAME, ContentValues().apply {
            put(Item.TableInfo.COLUMN_NAME_ITEM, "Waye")
            put(Item.TableInfo.COLUMN_NAME_ENABLE, 1)
        })

        db.insert(Item.TableInfo.TABLE_NAME, ContentValues().apply {
            put(Item.TableInfo.COLUMN_NAME_ITEM, "Legendary")
            put(Item.TableInfo.COLUMN_NAME_ENABLE, 0)
        })
    }

    @Test
    fun testInsert() {
        insertTestData()

        val result: Cursor = db.selectAll(Item.TableInfo.TABLE_NAME)
        assertEquals(2, result.count)
    }

    @Test
    fun testSelectAll() {
        insertTestData()

        val result: Cursor = db.selectAll(Item.TableInfo.TABLE_NAME)

        result.moveToNext()
        val item1 = Item(
            name = result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ITEM)),
            enabled = result.getInt(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ENABLE)) == 1)

        result.moveToNext()
        val item2 = Item(
            name = result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ITEM)),
            enabled = result.getInt(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ENABLE)) == 1)

        assertEquals("Waye", item1.name)
        assertEquals(true, item1.enabled)
        assertEquals("Legendary", item2.name)
        assertEquals(false, item2.enabled)
    }

    @Test
    fun testSelectByWhere() {
        insertTestData()

        val result = db.select(Item.TableInfo.TABLE_NAME, "${Item.TableInfo.COLUMN_NAME_ITEM} = \"Waye\"")
        assertEquals(1, result.count)

        result.moveToNext()
        assertEquals("1", result.getString(result.getColumnIndex(BaseColumns._ID)))
        assertEquals("Waye", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ITEM)))
        assertEquals("1", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ENABLE)))
    }

    @Test
    fun testSelectByQuery() {
        insertTestData()

        val result = db.select("SELECT * FROM ${Item.TableInfo.TABLE_NAME} WHERE ${BaseColumns._ID} = 2")
        assertEquals(1, result.count)

        result.moveToNext()
        assertEquals("2", result.getString(result.getColumnIndex(BaseColumns._ID)))
        assertEquals("Legendary", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ITEM)))
        assertEquals("0", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ENABLE)))
    }

    @Test
    fun testUpdateBySingleClause() {
        insertTestData()

        val values = ContentValues().apply {
            put(Item.TableInfo.COLUMN_NAME_ITEM, "ITEM1234")
            put(Item.TableInfo.COLUMN_NAME_ENABLE, "0")
        }

        db.update(Item.TableInfo.TABLE_NAME, values, "${BaseColumns._ID} = ?", "1")


        val result = db.selectAll(Item.TableInfo.TABLE_NAME)
        result.moveToNext()

        assertEquals("ITEM1234", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ITEM)))
        assertEquals("0", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ENABLE)))
    }


    @Test
    fun testUpdateByMultiClause() {
        insertTestData()

        val values = ContentValues().apply {
            put(Item.TableInfo.COLUMN_NAME_ITEM, "ITEM1234")
            put(Item.TableInfo.COLUMN_NAME_ENABLE, "0")
        }

        db.update(Item.TableInfo.TABLE_NAME, values, "${BaseColumns._ID} = ? OR ${Item.TableInfo.COLUMN_NAME_ITEM} = ?", arrayOf("1", "Legendary"))


        val result = db.selectAll(Item.TableInfo.TABLE_NAME)

        result.moveToNext()
        assertEquals("1", result.getString(result.getColumnIndex(BaseColumns._ID)))
        assertEquals("ITEM1234", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ITEM)))
        assertEquals("0", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ENABLE)))

        result.moveToNext()
        assertEquals("2", result.getString(result.getColumnIndex(BaseColumns._ID)))
        assertEquals("ITEM1234", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ITEM)))
        assertEquals("0", result.getString(result.getColumnIndex(Item.TableInfo.COLUMN_NAME_ENABLE)))
    }
}