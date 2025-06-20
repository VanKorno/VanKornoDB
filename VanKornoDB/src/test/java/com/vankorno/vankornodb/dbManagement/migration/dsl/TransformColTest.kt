package com.vankorno.vankornodb.dbManagement.migration.dsl

import org.junit.Assert
import org.junit.Test

class TransformColTest {

    @Test
    fun `onMissing should return fallback`() {
        val dsl = TransformCol().apply {
            modify("someField") {
                fallback = { "fallback" }
            }
        }
        val result = dsl.getOverride("someField")?.apply(null)
        Assert.assertEquals("fallback", result)
    }

    @Test
    fun `onFromInt should override Int`() {
        val dsl = TransformCol().apply {
            modify("someField") {
                fromInt = { it * 2 }
            }
        }
        val result = dsl.getOverride("someField")?.apply(10)
        Assert.assertEquals(20, result)
    }

    @Test
    fun `onFromString and finalTransform should work in order`() {
        val dsl = TransformCol().apply {
            modify("name") {
                fromString = { it.uppercase() }
                finalTransform = { (it as String).reversed() }
            }
        }
        val result = dsl.getOverride("name")?.apply("abc")
        Assert.assertEquals("CBA", result)
    }

    @Test
    fun `onFromOther should catch unknown type`() {
        val dsl = TransformCol().apply {
            modify("weird") {
                fromOther = { "handled: $it" }
            }
        }
        val result = dsl.getOverride("weird")?.apply(listOf(1, 2, 3))
        Assert.assertEquals("handled: [1, 2, 3]", result)
    }

    @Test
    fun `no override should just return original`() {
        val dsl = TransformCol().apply {
            modify("test") {
                // no override set
            }
        }
        val result = dsl.getOverride("test")?.apply(123)
        Assert.assertEquals(123, result)
    }
}