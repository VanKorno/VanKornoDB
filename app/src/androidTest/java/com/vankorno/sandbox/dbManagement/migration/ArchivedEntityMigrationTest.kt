package com.vankorno.sandbox.dbManagement.migration

import android.database.sqlite.SQLiteDatabase
import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.vankornodb.api.migrateMultiStep
import com.vankorno.vankornodb.get.getEntities
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArchivedEntityMigrationTest {
    private val table = _TestEntity.tableNamed("ArchivedRows")
    
    
    @Test
    fun migratesMultipleRowsFromV1WithRenamedBooleanAndNewFieldDefaults() {
        SQLiteDatabase.create(null).use { db ->
            db.createV1Table()
            val insert = "INSERT INTO ${table.name} (id, boo, name, float1, str1, long1, int1) VALUES (?, ?, ?, ?, ?, ?, ?)"
            db.execSQL(insert, arrayOf<Any>(7L, 1, "O'Brien", 1.25, "first", 5000000000L, -8))
            db.execSQL(insert, arrayOf<Any>(31L, 0, "second", -2.5, "", 9L, 17))
            
            db.migrateMultiStep(table.name, 1, EntityMeta.TestEntt)
            
            assertEquals(listOf(
                TestEntity(id = 7L, bool1 = true, name = "O'Brien", float1 = 1.25F, str1 = "first", long1 = 5000000000L, int1 = -8),
                TestEntity(id = 31L, name = "second", float1 = -2.5F, long1 = 9L, int1 = 17),
            ), db.getEntities(table, orderBy = { Id() }))
        }
    }
    
    
    @Test
    fun migratingFromV2KeepsBool1AndUsesDefaultForIncompatiblePosition() {
        SQLiteDatabase.create(null).use { db ->
            db.execSQL("""CREATE TABLE ${table.name} (
                id INTEGER, name TEXT, position TEXT, enabled INTEGER,
                int1 INTEGER, int2 INTEGER, int3 INTEGER, str1 TEXT, str2 TEXT, str3 TEXT,
                bool1 INTEGER, bool2 INTEGER, bool3 INTEGER, long1 INTEGER, long2 INTEGER,
                long3 INTEGER, float1 REAL, float2 REAL, float3 REAL
            )""")
            db.execSQL("""INSERT INTO ${table.name} VALUES (
                17, 'from v2', 'front', 1, 1, 2, 3, 'a', 'b', 'c', 1, 0, 1, 4, 5, 6, 1.5, 2.5, 3.5
            )""")
            
            db.migrateMultiStep(table.name, 2, EntityMeta.TestEntt)
            
            assertEquals(TestEntity(
                id = 17L, name = "from v2", position = 0L, enabled = true,
                int1 = 1, int2 = 2, int3 = 3, str1 = "a", str2 = "b", str3 = "c",
                bool1 = true, bool3 = true, long1 = 4L, long2 = 5L, long3 = 6L,
                float1 = 1.5F, float2 = 2.5F, float3 = 3.5F,
            ), db.getEntities(table).single())
        }
    }
    
    
    @Test
    fun emptyV1TableIsRebuiltWithCurrentColumns() {
        SQLiteDatabase.create(null).use { db ->
            db.createV1Table()
            
            db.migrateMultiStep(table.name, 1, EntityMeta.TestEntt)
            
            assertTrue(db.getEntities(table).isEmpty())
            db.execSQL("INSERT INTO ${table.name} (id, position, bool1) VALUES (23, 42, 1)")
            val row = db.getEntities(table).single()
            assertEquals(23L, row.id)
            assertEquals(42L, row.position)
            assertTrue(row.bool1)
        }
    }
    
    
    private fun SQLiteDatabase.createV1Table() {
        execSQL("""CREATE TABLE ${table.name} (
            id INTEGER, boo INTEGER, name TEXT, float1 REAL, str1 TEXT, long1 INTEGER, int1 INTEGER
        )""")
    }
}
