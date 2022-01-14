package com.devjaewoo.itsmywaye.dao

import android.app.Instrumentation
import android.provider.BaseColumns
import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.platform.app.InstrumentationRegistry
import com.devjaewoo.itsmywaye.DATABASE_NAME
import com.devjaewoo.itsmywaye.model.Alarm
import com.devjaewoo.itsmywaye.model.Item
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class ItemDAOTest {

    private lateinit var itemDAO: ItemDAO
    private lateinit var alarmDAO: AlarmDAO

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(DATABASE_NAME)
        itemDAO = ItemDAO(context)
        alarmDAO = AlarmDAO(context)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testInsertSelect() {
        val alarm = Alarm("Test", 1, 2, 3)
        val alarmID = alarmDAO.insert(alarm)
        alarm.id = alarmID

        val item1 = Item("Waye", true, alarm)
        val item2 = Item("Legendary", true, null)
        val item3 = Item("Epic", false, null)

        itemDAO.insert(item1)
        itemDAO.insert(item2)
        itemDAO.insert(item3)


        itemDAO.select("${BaseColumns._ID} = 1").also {

            assertEquals(item1.name, it?.name)
            assertEquals(item1.enabled, it?.enabled)

            assertNotNull(it?.alarm)
            assertEquals(alarm.filePath, it?.alarm!!.filePath)
            assertEquals(alarm.volume, it.alarm!!.volume)
            assertEquals(alarm.repeatTimes, it.alarm!!.repeatTimes)
            assertEquals(alarm.interval, it.alarm!!.interval)
        }

        itemDAO.select("${BaseColumns._ID} = 2").also {
            assertEquals(item2.name, it?.name)
            assertEquals(item2.enabled, it?.enabled)
            
            assertNull(it?.alarm)
        }

        itemDAO.select("${BaseColumns._ID} = 3").also {
            assertEquals(item3.name, it?.name)
            assertEquals(item3.enabled, it?.enabled)
            
            assertNull(it?.alarm)
        }
    }

    @Test
    fun testUpdate() {
        val item = Item("Waye", true)
        itemDAO.insert(item)

        var result = itemDAO.select("${BaseColumns._ID} = 1")
        val alarm = Alarm("Test", 1, 2, 3)
        val alarmID = alarmDAO.insert(alarm)
        alarm.id = alarmID

        assertNotNull(result)

        result?.name = "Legendary"
        result?.enabled = false
        result?.alarm = alarm

        itemDAO.update(result!!)


        result = itemDAO.select("${BaseColumns._ID} = 1")
        assertNotNull(result)

        assertEquals("Legendary", result?.name)
        assertEquals(false, result?.enabled)
        
        assertNotNull(result?.alarm)
        assertEquals(alarm.filePath, result?.alarm!!.filePath)
        assertEquals(alarm.volume, result?.alarm!!.volume)
        assertEquals(alarm.repeatTimes, result?.alarm!!.repeatTimes)
        assertEquals(alarm.interval, result?.alarm!!.interval)
    }

    @Test
    fun testDelete() {
        val item1 = Item("Waye", true)
        val item2 = Item("Legendary", true)
        val item3 = Item("Epic", false)

        itemDAO.insert(item1)
        itemDAO.insert(item2)
        itemDAO.insert(item3)

        itemDAO.delete(2)

        val items: List<Item> = itemDAO.selectAll()
        assertEquals(2, items.size)

        assertEquals(1, items[0].id)
        assertEquals(3, items[1].id)

        assertEquals("Waye", items[0].name)
        assertEquals("Epic", items[1].name)

        assertEquals(true, items[0].enabled)
        assertEquals(false, items[1].enabled)
    }

    @Test
    fun testDelete_AlarmDelete() {
        val alarm = Alarm("Test", 1, 2, 3)
        val alarmID = alarmDAO.insert(alarm)
        alarm.id = alarmID

        val item = Item("Waye", true, alarm)
        itemDAO.insert(item)

        alarmDAO.delete(alarmID)

        val result = itemDAO.select("${BaseColumns._ID} = 1")
        assertNotNull(result)

        assertNull(result?.alarm)
    }
}