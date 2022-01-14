package com.devjaewoo.itsmywaye.dao

import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import com.devjaewoo.itsmywaye.DATABASE_NAME
import com.devjaewoo.itsmywaye.model.Alarm
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class AlarmDAOTest {

    private lateinit var alarmDAO: AlarmDAO

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(DATABASE_NAME)
        alarmDAO = AlarmDAO(context)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testInsert() {
        val alarm1 = Alarm("ABC", 0, 0, 0)
        val alarm2 = Alarm("DEF", 1, 1, 1)
        val alarm3 = Alarm("GHI", 2, 2, 2)

        val row1 = alarmDAO.insert(alarm1)
        val row2 = alarmDAO.insert(alarm2)
        val row3 = alarmDAO.insert(alarm3)

        assertEquals(1, row1)
        assertEquals(2, row2)
        assertEquals(3, row3)

        alarmDAO.select("${BaseColumns._ID} = 1").also {
            assertEquals(alarm1.filePath, it?.filePath)
            assertEquals(alarm1.volume, it?.volume)
            assertEquals(alarm1.repeatTimes, it?.repeatTimes)
            assertEquals(alarm1.interval, it?.interval)
        }

        alarmDAO.select("${BaseColumns._ID} = 2").also {
            assertEquals(alarm2.filePath, it?.filePath)
            assertEquals(alarm2.volume, it?.volume)
            assertEquals(alarm2.repeatTimes, it?.repeatTimes)
            assertEquals(alarm2.interval, it?.interval)
        }

        alarmDAO.select("${BaseColumns._ID} = 3").also {
            assertEquals(alarm3.filePath, it?.filePath)
            assertEquals(alarm3.volume, it?.volume)
            assertEquals(alarm3.repeatTimes, it?.repeatTimes)
            assertEquals(alarm3.interval, it?.interval)
        }
    }

    @Test
    fun testUpdate() {
        val alarm = Alarm("ABC", 0, 0, 0)
        alarmDAO.insert(alarm)

        var result = alarmDAO.select("${BaseColumns._ID} = 1")
        assertNotNull(result)

        result?.filePath = "DEF"
        result?.volume = 100
        result?.repeatTimes = 20
        result?.interval = 5

        alarmDAO.update(result!!)


        result = alarmDAO.select("${BaseColumns._ID} = 1")
        assertNotNull(result)

        assertEquals("DEF", result?.filePath)
        assertEquals(100, result?.volume)
        assertEquals(20, result?.repeatTimes)
        assertEquals(5, result?.interval)
    }

    @Test
    fun testDeleteSelectAll() {
        val alarm1 = Alarm("ABC", 0, 0, 0)
        val alarm2 = Alarm("DEF", 1, 1, 1)
        val alarm3 = Alarm("GHI", 2, 2, 2)

        alarmDAO.insert(alarm1)
        alarmDAO.insert(alarm2)
        alarmDAO.insert(alarm3)

        alarmDAO.delete(2)


        val alarms: List<Alarm> = alarmDAO.selectAll()
        assertEquals(2, alarms.size)

        assertEquals(1, alarms[0].id)
        assertEquals(3, alarms[1].id)

        assertEquals(alarm1.filePath, alarms[0].filePath)
        assertEquals(alarm3.filePath, alarms[1].filePath)

        assertEquals(alarm1.volume, alarms[0].volume)
        assertEquals(alarm3.volume, alarms[1].volume)

        assertEquals(alarm1.repeatTimes, alarms[0].repeatTimes)
        assertEquals(alarm3.repeatTimes, alarms[1].repeatTimes)

        assertEquals(alarm1.interval, alarms[0].interval)
        assertEquals(alarm3.interval, alarms[1].interval)
    }
}