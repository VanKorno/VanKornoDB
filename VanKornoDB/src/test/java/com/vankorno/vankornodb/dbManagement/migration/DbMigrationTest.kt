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
        val renameMap = mapOf("name" to mapOf(1 to "title"))
        
        val new = old.oldToNewEntt<OldV1, NewV2>(oldVersion = 1, renameMap)
        
        assertEquals(old.id, new.id)
        assertEquals(old.title, new.name)
        assertEquals(old.note, new.note)
    }
    
    data class OldV2(val id: Int = 0, val title: String = "", val extra: String = "")
    data class NewV3(val id: Int = 0, val name: String = "", val description: String = "default")
    
    @Test
    fun `missing fields fallback to default`() {
        val old = OldV2(id = 2, title = "Legacy", extra = "ignored")
        val renameMap = mapOf("name" to mapOf(2 to "title"))
        
        val new = old.oldToNewEntt<OldV2, NewV3>(oldVersion = 2, renameMap)
        
        assertEquals(2, new.id)
        assertEquals("Legacy", new.name)
        assertEquals("", new.description)
    }
    
    data class OldV3(val id: Int = 0, val text: String = "abc")
    data class NewV4(val id: Int = 0, val text: String = "", val extra: Boolean = true)
    
    @Test
    fun `unmapped field fallback with primitive default`() {
        val old = OldV3(id = 3, text = "abc")
        val new = old.oldToNewEntt<OldV3, NewV4>(oldVersion = 3)
        
        assertEquals(old.id, new.id)
        assertEquals(old.text, new.text)
        assertFalse(new.extra) // overridden by defaultValueForParam()
    }
    
    data class OldV4(val id: Int = 0, val content: String = "")
    data class NewV5(val id: Int = 0, val text: String = "")
    
    @Test
    fun `custom override after mapping`() {
        val old = OldV4(id = 9, content = "text here")
        val renameMap = mapOf("text" to mapOf(4 to "content"))
        
        val new = old.oldToNewEntt<OldV4, NewV5>(oldVersion = 4, renameMap) { from ->
            copy(text = "[${from.content}]")
        }
        assertEquals(9, new.id)
        assertEquals("[text here]", new.text)
    }
    
    
    
    
}