package com.vankorno.vankornodb.dbManagement.migration

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.typeOf

class MigrationUtilsTest : MigrationUtils() {
    
    @Test fun intConversions() {
        assertEquals(123, tryAutoConvertValue(123, typeOf<Int>()))
        assertEquals(123, tryAutoConvertValue(123L, typeOf<Int>()))
        assertEquals(123, tryAutoConvertValue(123.9f, typeOf<Int>()))
        assertEquals(123, tryAutoConvertValue(123.9, typeOf<Int>()))
        assertEquals(1, tryAutoConvertValue(true, typeOf<Int>()))
        assertEquals(0, tryAutoConvertValue(false, typeOf<Int>()))
    }

    @Test fun longConversions() {
        assertEquals(123L, tryAutoConvertValue(123, typeOf<Long>()))
        assertEquals(123L, tryAutoConvertValue(123.0f, typeOf<Long>()))
        assertEquals(1L, tryAutoConvertValue(true, typeOf<Long>()))
    }

    @Test fun floatConversions() {
        assertEquals(123.0f, tryAutoConvertValue(123, typeOf<Float>()))
        assertEquals(1.0f, tryAutoConvertValue(true, typeOf<Float>()))
    }

    @Test fun doubleConversions() {
        assertEquals(123.0, tryAutoConvertValue(123, typeOf<Double>()))
        assertEquals(1.0, tryAutoConvertValue(true, typeOf<Double>()))
    }

    @Test fun booleanConversions() {
        assertEquals(true, tryAutoConvertValue(1, typeOf<Boolean>()))
        assertEquals(false, tryAutoConvertValue(0L, typeOf<Boolean>()))
        assertEquals(false, tryAutoConvertValue(0.0, typeOf<Boolean>()))
    }

    @Test fun stringConversions() {
        assertEquals("hi", tryAutoConvertValue("hi", typeOf<String>()))
        assertNull(tryAutoConvertValue(123, typeOf<String>()))
        assertNull(tryAutoConvertValue(true, typeOf<String>()))
    }

    @Test fun unsupportedCases() {
        assertNull(tryAutoConvertValue(listOf(1, 2), typeOf<Int>()))
        assertNull(tryAutoConvertValue(null, typeOf<Float>()))
        assertNull(tryAutoConvertValue("true", typeOf<Boolean>()))
    }
    
    
}