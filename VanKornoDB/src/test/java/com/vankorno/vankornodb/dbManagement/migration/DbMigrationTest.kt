package com.vankorno.vankornodb.dbManagement.migration

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class DbMigrationTest : MigrationUtils() {
    data class OldV1(val id: Int = 0, val title: String = "", val note: String = "")
    data class NewV2(val id: Int = 0, val name: String = "", val note: String = "")
    
    @Test
    fun `basic field rename with version map`() {
        val old = OldV1(id = 1, title = "Task 1", note = "Some note")
        val renameMap = mapOf(Pair("name", "title"))
        
        val new = convertEntity(old, NewV2::class, renameMap) as NewV2
        
        assertEquals(old.id, new.id)
        assertEquals(old.title, new.name)
        assertEquals(old.note, new.note)
    }
    
    data class OldV2(val id: Int = 0, val title: String = "", val extra: String = "")
    data class NewV3(val id: Int = 0, val name: String = "", val description: String = "default")
    
    @Test
    fun `missing fields fallback to default`() {
        val old = OldV2(id = 2, title = "Legacy", extra = "ignored")
        val renameMap = mapOf(Pair("name", "title"))
        
        val new = convertEntity(old, NewV3::class, renameMap) as NewV3
        
        assertEquals(2, new.id)
        assertEquals("Legacy", new.name)
        assertEquals("default", new.description)
    }
    
    data class OldV3(val id: Int = 0, val text: String = "abc")
    data class NewV4(val id: Int = 0, val text: String = "", val extra: Boolean = true)
    
    @Test
    fun `unmapped field fallback with primitive default`() {
        val old = OldV3(id = 3, text = "abc")
        val new = convertEntity(old, NewV4::class) as NewV4
        
        assertEquals(old.id, new.id)
        assertEquals(old.text, new.text)
        assertTrue(new.extra) // overridden by defaultValueForParam()
    }
    
    
    
    data class OldNumV1(val id: Int = 0, val active: Boolean = true, val score: Int = 42)
    data class NewNumV2(val id: Long = 0, val active: Int = 0, val score: Double = 0.0)
    
    @Test
    fun `type auto-conversion for basic numeric types`() {
        val old = OldNumV1(id = 1, active = true, score = 99)
        val new = convertEntity(old, NewNumV2::class) as NewNumV2
        
        assertEquals(1L, new.id)
        assertEquals(1, new.active)
        assertEquals(99.0, new.score, 0.0001)
    }
    
    data class OldNumV2(val id: Long = 0L, val value: Double = 1.0)
    data class NewNumV3(val id: Int = 0, val value: Boolean = false)
    
    @Test
    fun `type auto-conversion from Long and Double to Int and Boolean`() {
        val old = OldNumV2(id = 123456789L, value = 0.0)
        val new = convertEntity(old, NewNumV3::class) as NewNumV3
        
        assertEquals(123456789, new.id)
        assertFalse(new.value)
    }
    
    data class OldPartial(val data: Float = 1.1f)
    data class NewPartial(val data: String = "not convertible")
    
    @Test
    fun `unsupported type conversion fallback to default`() {
        val old = OldPartial()
        val new = convertEntity(old, NewPartial::class) as NewPartial
        
        assertEquals("not convertible", new.data) // uses constructor default
    }
    
    
    
}