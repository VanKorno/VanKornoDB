package com.vankorno.vankornodb.dbManagement

import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.dbManagement.customTableBuilder.newTableQueryCustom
import com.vankorno.vankornodb.dbManagement.data.AutoId
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.customTableBuilder.ColumnDef
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import org.junit.Test
import org.junit.Assert.assertEquals

class DbTableBuilderTest {
    
    @Test
    fun `simple buildCreateTableQuery() returns correct beginning`() {
        assertEquals(
            "CREATE TABLE IF NOT EXISTS $Name ($ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)",
            newTableQueryCustom(
                Name,
                arrayListOf(ColumnDef(ID, AutoId))
            )
        )
    }
    
    @Test
    fun `simple buildCreateTableQuery() returns the rest OK`() {
        assertEquals(
            "CREATE TABLE IF NOT EXISTS $Name ($ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, $Priority INT NOT NULL, " +
                "$Name TEXT NOT NULL, PitBool BOOL NOT NULL, Live BIGINT NOT NULL, Fleet REAL NOT NULL, Img BLOB NOT NULL)",
            newTableQueryCustom(
                Name,
                arrayListOf(
                    ColumnDef(ID, AutoId),
                    ColumnDef(Priority, IntCol),
                    ColumnDef(Name, StrCol),
                    ColumnDef("PitBool", BoolCol),
                    ColumnDef("Live", LongCol),
                    ColumnDef("Fleet", FloatCol),
                    ColumnDef("Img", BlobCol)
                )
            )
        )
    }
    
    
    data class SimpleEntity(
        val id: Int = 0,
        val name: String = "default",
        val isActive: Boolean = true,
        val age: Int = 25
    )
    
    @Test
    fun testSimpleEntityTableCreation() {
        val sql = newTableQuery("SimpleTable", SimpleEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS SimpleTable (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL DEFAULT 'default', " +
                "isActive BOOL NOT NULL DEFAULT 1, " +
                "age INT NOT NULL DEFAULT 25" +
            ")",
            sql
        )
    }
    
    data class ComplexEntity(
        val id: Int = 0,
        val data: ByteArray? = null,
        val name: String = "blob",
        val flags: Int = 0
    )
    
    @Test
    fun testComplexEntityTableCreation() {
        val sql = newTableQuery("ComplexTable", ComplexEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS ComplexTable (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, data BLOB, name TEXT NOT NULL DEFAULT 'blob', flags INT NOT NULL DEFAULT 0)",
            sql
        )
    }
    
    data class NullableDefaultsEntity(
        val id: Int = 0,
        val note: String? = "some", // nullable, should NOT emit DEFAULT
        val enabled: Boolean? = null
    )
    
    @Test
    fun testNullableDefaultsEntity() {
        val sql = newTableQuery("NullableDefaults", NullableDefaultsEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS NullableDefaults (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "note TEXT, " +
                "enabled BOOL" +
            ")",
            sql
        )
    }
    
    data class UnsupportedTypesEntity(
        val id: Int = 0,
        val numbers: List<Int> = emptyList(), // should be skipped
        val values: Array<String> = emptyArray() // should be skipped
    )
    
    @Test
    fun testUnsupportedTypesEntity() {
        val sql = newTableQuery("UnsupportedTypes", UnsupportedTypesEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS UnsupportedTypes (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
            ")",
            sql
        )
    }
    
    data class AllNullableEntity(
        val id: Int? = null,
        val name: String? = null,
        val score: Float? = null
    )
    
    @Test
    fun testAllNullableEntity() {
        val sql = newTableQuery("NullableTable", AllNullableEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS NullableTable (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "score REAL" +
            ")",
            sql
        )
    }
    
    data class IdInMiddleEntity(
        val name: String = "middle",
        val id: Int = 0,
        val active: Boolean = false
    )
    
    @Test
    fun testIdInMiddleEntity() {
        val sql = newTableQuery("MiddleIdTable", IdInMiddleEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS MiddleIdTable (" +
                "name TEXT NOT NULL DEFAULT 'middle', " +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "active BOOL NOT NULL DEFAULT 0" +
            ")",
            sql
        )
    }
    
    
    data class ListEntitySingle(
        val id: Int = 0,
        val scoresList: List<Int> = listOf(1, 2, 3)
    )
    
    @Test
    fun testListEntitySingle() {
        val sql = newTableQuery("ListSingleTable", ListEntitySingle::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS ListSingleTable (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "scores1 INT NOT NULL DEFAULT 1, " +
                "scores2 INT NOT NULL DEFAULT 1, " +
                "scores3 INT NOT NULL DEFAULT 1" +
            ")",
            sql
        )
    }
    
    data class ListEntityMixed(
        val id: Int = 0,
        val flagsList: List<Boolean> = listOf(true, false),
        val namesList: List<String> = listOf("alpha", "beta")
    )
    
    @Test
    fun testListEntityMixed() {
        val sql = newTableQuery("ListMixedTable", ListEntityMixed::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS ListMixedTable (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "flags1 BOOL NOT NULL DEFAULT 1, " +
                "flags2 BOOL NOT NULL DEFAULT 1, " +
                "names1 TEXT NOT NULL DEFAULT 'alpha', " +
                "names2 TEXT NOT NULL DEFAULT 'alpha'" +
            ")",
            sql
        )
    }
    
    
    
    
    
    
    
    
    
    
    
    
}