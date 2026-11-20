package com.vankorno.vankornodb.dbManagement.migration

import com.vankorno.vankornodb.dbManagement.data.DbEntityWithId
import com.vankorno.vankornodb.dbManagement.data.SchemaBundle
import com.vankorno.vankornodb.dbManagement.data.buildColList
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.sCol
import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MigrationValidationTest {
    
    data class EntityV1(val usernam: String, val age: Int) : DbEntityWithId()
    object SchemaV1 : SchemaBundle<EntityV1>(
        clazz = EntityV1::class,
        columns = buildColList {
            +sCol("usernam")
            +iCol("age")
        },
    )
    
    data class EntityV2(val username: String, val userAge: Int) : DbEntityWithId()
    object SchemaV2 : SchemaBundle<EntityV2>(
        clazz = EntityV2::class,
        columns = buildColList {
            +sCol("username")
            +iCol("userAge")
        },
    )
    
    class EmptySchemaEntity : DbEntityWithId()
    object SchemaWithoutCols : SchemaBundle<EmptySchemaEntity>(
        clazz = EmptySchemaEntity::class,
    )
    
    
    @Test
    fun `findClosestMatch returns null for empty candidates`() {
        val result = findClosestMatch("target", emptyList())
        assertNull(result)
    }
    
    @Test
    fun `findClosestMatch returns exact match`() {
        val result = findClosestMatch("username", listOf("id", "username", "age"))
        assertEquals("username", result)
    }
    
    @Test
    fun `findClosestMatch returns closest match with small distance`() {
        val result = findClosestMatch("usernam", listOf("id", "username", "age"))
        assertEquals("username", result)
    }
    
    @Test
    fun `findClosestMatch is case-insensitive for distance`() {
        val result = findClosestMatch("userName", listOf("id", "username", "age"))
        assertEquals("username", result)
    }
    
    @Test
    fun `findClosestMatch returns null if distance is too large`() {
        val result = findClosestMatch("completelyDifferent", listOf("id", "username", "age"))
        assertNull(result)
    }
    
    
    @Test
    fun `validateMigrationBundle passes without warnings for valid rename rules`() {
        val originalOut = System.out
        val outContent = ByteArrayOutputStream()
        try {
            System.setOut(PrintStream(outContent))
            val bundles = mapOf(
                1 to SchemaV1,
                2 to SchemaV2,
            )
            val history = listOf(
                RenameRecord(version = 2, from = "usernam", to = "username"),
                RenameRecord(version = 2, from = "age", to = "userAge"),
            )
            validateMigrationBundle(bundles, history)
            
            val output = outContent.toString()
            assertFalse(output.contains("Migration validation warning"))
        } finally {
            System.setOut(originalOut)
        }
    }
    
    @Test
    fun `validateMigrationBundle warns with hint when source column has typo`() {
        val originalOut = System.out
        val outContent = ByteArrayOutputStream()
        try {
            System.setOut(PrintStream(outContent))
            val bundles = mapOf(
                1 to SchemaV1,
                2 to SchemaV2,
            )
            val history = listOf(
                RenameRecord(version = 2, from = "usrnam", to = "username"),
            )
            validateMigrationBundle(bundles, history)
            
            val output = outContent.toString()
            assertTrue(output.contains("Migration validation warning at version 2"))
            assertTrue(output.contains("Source column 'usrnam' in rename rule ('usrnam' -> 'username') was not found in version 1 schema"))
            assertTrue(output.contains("Did you mean 'usernam'?"))
        } finally {
            System.setOut(originalOut)
        }
    }
    
    @Test
    fun `validateMigrationBundle warns with hint when target column has typo`() {
        val originalOut = System.out
        val outContent = ByteArrayOutputStream()
        try {
            System.setOut(PrintStream(outContent))
            val bundles = mapOf(
                1 to SchemaV1,
                2 to SchemaV2,
            )
            val history = listOf(
                RenameRecord(version = 2, from = "usernam", to = "usernme"),
            )
            validateMigrationBundle(bundles, history)
            
            val output = outContent.toString()
            assertTrue(output.contains("Migration validation warning at version 2"))
            assertTrue(output.contains("Target column 'usernme' in rename rule ('usernam' -> 'usernme') was not found in version 2 schema"))
            assertTrue(output.contains("Did you mean 'username'?"))
        } finally {
            System.setOut(originalOut)
        }
    }
    
    @Test
    fun `validateMigrationBundle skips check when schema columns is null`() {
        val originalOut = System.out
        val outContent = ByteArrayOutputStream()
        try {
            System.setOut(PrintStream(outContent))
            val bundles = mapOf(
                1 to SchemaWithoutCols,
                2 to SchemaWithoutCols,
            )
            val history = listOf(
                RenameRecord(version = 2, from = "anySource", to = "anyTarget"),
            )
            validateMigrationBundle(bundles, history)
            
            val output = outContent.toString()
            assertFalse(output.contains("Migration validation warning"))
        } finally {
            System.setOut(originalOut)
        }
    }
}
