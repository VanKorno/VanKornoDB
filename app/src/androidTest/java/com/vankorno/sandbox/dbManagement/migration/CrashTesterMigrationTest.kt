package com.vankorno.sandbox.dbManagement.migration

import android.database.sqlite.SQLiteDatabase
import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.sandbox._entities.crashTester._CrashTester
import com.vankorno.vankornodb.api.migrateMultiStep
import com.vankorno.vankornodb.get.getEntities
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CrashTesterMigrationTest {
    private val runs = _CrashTester.tableNamed("CrashRuns")
    
    
    @Test
    fun v1FollowsBothRenamesAndRepairsMalformedReadings() {
        SQLiteDatabase.create(null).use { db ->
            db.execSQL("CREATE TABLE CrashRuns (id INTEGER, alias TEXT, trace TEXT, armed INTEGER, retiredCode TEXT)")
            db.execSQL("INSERT INTO CrashRuns VALUES (41, 'O''Brien', '-3|bad| 8 |2|99', -1, 'obsolete')")
            
            db.migrateMultiStep(runs.name, 1, EntityMeta.CrashTester)
            
            val run = db.getEntities(runs).single()
            assertEquals(41L, run.id)
            assertEquals("O'Brien", run.name)
            assertTrue(run.enabled)
            assertEquals(8, run.int1)
            assertEquals(listOf(-3, 0, 8, 2) + List(6) { 0 }, run.dayList)
            assertEquals(listOf(true) + List(19) { false }, run.isBullyList)
            assertEquals(listOf("", "", "O'Brien"), run.textList)
            assertArrayEquals(byteArrayOf(), run.blobBa)
        }
    }
    
    
    @Test
    fun v2PreservesBinaryPacketAcrossBothMilestones() {
        SQLiteDatabase.create(null).use { db ->
            val packet = byteArrayOf(0, -1, 127, -128)
            db.execSQL("CREATE TABLE CrashRuns (id INTEGER, callsign TEXT, trace TEXT, armed INTEGER, packet BLOB)")
            db.execSQL("INSERT INTO CrashRuns VALUES (9, 'Echo', '5', 0, ?)", arrayOf(packet))
            
            db.migrateMultiStep(runs.name, 2, EntityMeta.CrashTester)
            
            val run = db.getEntities(runs).single()
            assertArrayEquals(packet, run.blobBa)
            assertEquals("Echo", run.name)
            assertEquals(listOf(5) + List(9) { 0 }, run.dayList)
        }
    }
    
    
    @Test
    fun v3ReadsAdjacentExpandedListsBeforeBuildingTheFinalLists() {
        SQLiteDatabase.create(null).use { db ->
            db.execSQL("""CREATE TABLE CrashRuns (
                id INTEGER, name TEXT, enabled INTEGER, packet BLOB,
                sample1 INTEGER, sample2 INTEGER, sample3 INTEGER, sample4 INTEGER,
                label1 TEXT, label2 TEXT
            )""")
            db.execSQL("INSERT INTO CrashRuns VALUES (72, 'Delta', 0, X'0080', -8, 2, 17, -1, 'left', 'right')")
            
            db.migrateMultiStep(runs.name, 3, EntityMeta.CrashTester)
            
            val run = db.getEntities(runs).single()
            assertEquals(listOf(-8, 2, 17, -1) + List(6) { 0 }, run.dayList)
            assertEquals(listOf(true, false, false, true) + List(16) { false }, run.isBullyList)
            assertEquals(listOf("left", "right", "Delta"), run.textList)
            assertEquals(17, run.int1)
            assertArrayEquals(byteArrayOf(0, -128), run.blobBa)
        }
    }
}
