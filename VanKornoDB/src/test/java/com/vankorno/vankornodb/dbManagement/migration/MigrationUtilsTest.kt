package com.vankorno.vankornodb.dbManagement.migration

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import kotlin.reflect.full.primaryConstructor

class MigrationUtilsTest {
    @Test
    fun `findOldName returns correct mapping for exact version`() {
        val map = mapOf("newProp" to mapOf(2 to "oldProp"))
        assertEquals("oldProp", MigrationUtils.findOldName("newProp", 2, map))
    }
    
    @Test
    fun `findOldName returns correct mapping for earlier version`() {
        val map = mapOf("newProp" to mapOf(1 to "v1", 2 to "v2"))
        assertEquals("v1", MigrationUtils.findOldName("newProp", 1, map))
        assertEquals("v2", MigrationUtils.findOldName("newProp", 2, map))
        assertEquals("v2", MigrationUtils.findOldName("newProp", 3, map)) // nearest <= version
    }
    
    @Test
    fun `findOldName returns null when no suitable version`() {
        val map = mapOf("newProp" to mapOf(5 to "something"))
        assertNull(MigrationUtils.findOldName("newProp", 3, map))
    }
    
    @Test
    fun `findOldName returns null when property is not found`() {
        val map = mapOf("someOther" to mapOf(1 to "x"))
        assertNull(MigrationUtils.findOldName("nonexistent", 1, map))
    }
    
    
    data class DummyClass(
        val i: Int,
        val l: Long,
        val b: Boolean,
        val s: String,
        val d: Double,
        val f: Float,
        val unsupported: List<String>
    )
    
    @Test
    fun `defaultValueForParam returns correct default for supported types`() {
        val ctor = DummyClass::class.primaryConstructor!!
        val values = ctor.parameters.associateWith {
            MigrationUtils.defaultValueForParam(it)
        }
        assertEquals(0, values[ctor.parameters[0]]) // Int
        assertEquals(0L, values[ctor.parameters[1]]) // Long
        assertEquals(false, values[ctor.parameters[2]]) // Boolean
        assertEquals("", values[ctor.parameters[3]]) // String
        assertEquals(0.0, values[ctor.parameters[4]]) // Double
        assertEquals(0f, values[ctor.parameters[5]]) // Float
        assertNull(values[ctor.parameters[6]]) // List<String> unsupported
    }
}