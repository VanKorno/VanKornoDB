package com.vankorno.vankornodb.dbManagement.migration

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

class DbMigrationTest {
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
        assertEquals("", new.description)
    }
    
    data class OldV3(val id: Int = 0, val text: String = "abc")
    data class NewV4(val id: Int = 0, val text: String = "", val extra: Boolean = true)
    
    @Test
    fun `unmapped field fallback with primitive default`() {
        val old = OldV3(id = 3, text = "abc")
        val new = convertEntity(old, NewV4::class) as NewV4
        
        assertEquals(old.id, new.id)
        assertEquals(old.text, new.text)
        assertFalse(new.extra) // overridden by defaultValueForParam()
    }
    
    
    
    
    
}