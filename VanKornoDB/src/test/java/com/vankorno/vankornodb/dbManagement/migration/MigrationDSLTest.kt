package com.vankorno.vankornodb.dbManagement.migration

import org.junit.Assert.assertEquals
import org.junit.Test

class MigrationDSLTest {

    @Test
    fun `onMissing should return fallback`() {
        val dsl = MigrationDSL().apply {
            modify("someField") {
                onMissing = { "fallback" }
            }
        }
        val result = dsl.getOverride("someField")?.apply(null)
        assertEquals("fallback", result)
    }

    @Test
    fun `onFromInt should override Int`() {
        val dsl = MigrationDSL().apply {
            modify("someField") {
                onFromInt = { it * 2 }
            }
        }
        val result = dsl.getOverride("someField")?.apply(10)
        assertEquals(20, result)
    }

    @Test
    fun `onFromString and finalTransform should work in order`() {
        val dsl = MigrationDSL().apply {
            modify("name") {
                onFromString = { it.uppercase() }
                finalTransform = { (it as String).reversed() }
            }
        }
        val result = dsl.getOverride("name")?.apply("abc")
        assertEquals("CBA", result)
    }

    @Test
    fun `onFromOther should catch unknown type`() {
        val dsl = MigrationDSL().apply {
            modify("weird") {
                onFromOther = { "handled: $it" }
            }
        }
        val result = dsl.getOverride("weird")?.apply(listOf(1, 2, 3))
        assertEquals("handled: [1, 2, 3]", result)
    }

    @Test
    fun `no override should just return original`() {
        val dsl = MigrationDSL().apply {
            modify("test") {
                // no override set
            }
        }
        val result = dsl.getOverride("test")?.apply(123)
        assertEquals(123, result)
    }
}