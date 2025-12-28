package com.vankorno.vankornodb.newTable

import com.vankorno.vankornodb.api.CurrEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class NewTableReflectTest {
    
    data class SimpleEntity(
        val id: Int = 0,
        val name: String = "default",
        val isActive: Boolean = true,
        val age: Int = 25
    ) : CurrEntity
    
    @Test
    fun testSimpleEntityTableCreation() {
        val sql = newTableQuery("SimpleTable", SimpleEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS SimpleTable (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
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
    ) : CurrEntity
    
    @Test
    fun testComplexEntityTableCreation() {
        val sql = newTableQuery("ComplexTable", ComplexEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS ComplexTable (id INTEGER NOT NULL PRIMARY KEY, data BLOB, name TEXT NOT NULL DEFAULT 'blob', flags INT NOT NULL DEFAULT 0)",
            sql
        )
    }
    
    data class NullableDefaultsEntity(
        val id: Int = 0,
        val note: String? = "some", // nullable, should NOT emit DEFAULT
        val enabled: Boolean? = null
    ) : CurrEntity
    
    @Test
    fun testNullableDefaultsEntity() {
        val sql = newTableQuery("NullableDefaults", NullableDefaultsEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS NullableDefaults (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
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
    ) : CurrEntity
    
    @Test
    fun testUnsupportedTypesEntity() {
        val sql = newTableQuery("UnsupportedTypes", UnsupportedTypesEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS UnsupportedTypes (" +
                "id INTEGER NOT NULL PRIMARY KEY" +
            ")",
            sql
        )
    }
    
    data class AllNullableEntity(
        val id: Int? = null,
        val name: String? = null,
        val score: Float? = null
    ) : CurrEntity
    
    @Test
    fun testAllNullableEntity() {
        val sql = newTableQuery("NullableTable", AllNullableEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS NullableTable (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
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
    ) : CurrEntity
    
    @Test
    fun testIdInMiddleEntity() {
        val sql = newTableQuery("MiddleIdTable", IdInMiddleEntity::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS MiddleIdTable (" +
                "name TEXT NOT NULL DEFAULT 'middle', " +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "active BOOL NOT NULL DEFAULT 0" +
            ")",
            sql
        )
    }
    
    
    data class ListEntitySingle(
        val id: Int = 0,
        val scoresList: List<Int> = listOf(1, 2, 3)
    ) : CurrEntity
    
    @Test
    fun testListEntitySingle() {
        val sql = newTableQuery("ListSingleTable", ListEntitySingle::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS ListSingleTable (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
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
    ) : CurrEntity
    
    @Test
    fun testListEntityMixed() {
        val sql = newTableQuery("ListMixedTable", ListEntityMixed::class)
        assertEquals(
            "CREATE TABLE IF NOT EXISTS ListMixedTable (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "flags1 BOOL NOT NULL DEFAULT 1, " +
                "flags2 BOOL NOT NULL DEFAULT 1, " +
                "names1 TEXT NOT NULL DEFAULT 'alpha', " +
                "names2 TEXT NOT NULL DEFAULT 'alpha'" +
            ")",
            sql
        )
    }
    
    
    
    
    
    
    
    
    
    
    
    
}