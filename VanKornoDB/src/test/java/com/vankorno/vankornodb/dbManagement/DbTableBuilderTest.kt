package com.vankorno.vankornodb.dbManagement

import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.AutoId
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.ColumnDef
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
            "CREATE TABLE $Name ($ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)",
            newTableQueryCustom(
                Name,
                arrayListOf(ColumnDef(ID, AutoId))
            )
        )
    }
    
    @Test
    fun `simple buildCreateTableQuery() returns the rest OK`() {
        assertEquals(
            "CREATE TABLE $Name ($ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, $Priority INT NOT NULL, " +
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
        val sql = newTableQuery<SimpleEntity>("SimpleTable")
        assertEquals(
            "CREATE TABLE SimpleTable (" +
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
        val sql = newTableQuery<ComplexEntity>("ComplexTable")
        assertEquals(
            "CREATE TABLE ComplexTable (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, data BLOB, name TEXT NOT NULL DEFAULT 'blob', flags INT NOT NULL DEFAULT 0)",
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
        val sql = newTableQuery<NullableDefaultsEntity>("NullableDefaults")
        assertEquals(
            "CREATE TABLE NullableDefaults (" +
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
        val sql = newTableQuery<UnsupportedTypesEntity>("UnsupportedTypes")
        assertEquals(
            "CREATE TABLE UnsupportedTypes (" +
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
        val sql = newTableQuery<AllNullableEntity>("NullableTable")
        assertEquals(
            "CREATE TABLE NullableTable (" +
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
        val sql = newTableQuery<IdInMiddleEntity>("MiddleIdTable")
        assertEquals(
            "CREATE TABLE MiddleIdTable (" +
                "name TEXT NOT NULL DEFAULT 'middle', " +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "active BOOL NOT NULL DEFAULT 0" +
            ")",
            sql
        )
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}