package com.vankorno.vankornodb.dbManagement.migration

import org.junit.Assert.assertEquals
import org.junit.Test

class IdenticalsMapperTest {
    data class OldTask(
        val id: Int = 0,
        val name: String = "",
        val description: String = "",
        val oldField: String = ""
    )
    data class NewTask(
        val id: Int = 0,
        val name: String = "",
        val description: String = "",
        val newField: String = ""
    )
    
    @Test
    fun `test mapIdenticals`() {
        val old = OldTask(
            id = 1,
            name = "Test task",
            description = "Old description",
            oldField = "Value"
        )
        val new = old.mapIdenticals<OldTask, NewTask> { old ->
            copy(newField = old.oldField)
        }
        assertEquals(old.id, new.id)
        assertEquals(old.name, new.name)
        assertEquals(old.description, new.description)
        assertEquals(old.oldField, new.newField)
    }
    
    data class OldTask2(
        val id: Int = 0,
        val name: String = "",
        val description: String = ""
    )
    data class NewTask2(
        val description: String = "",
        val id: Int = 0,
        val name: String = ""
    )
    
    @Test
    fun `test mapIdenticals different order`() {
        val old = OldTask2(
            id = 42,
            name = "Test name",
            description = "Test description"
        )
        val new = old.mapIdenticals<OldTask2, NewTask2>()
        
        assertEquals(old.id, new.id)
        assertEquals(old.name, new.name)
        assertEquals(old.description, new.description)
    }
    
    data class OldTask3(
        val id: Int = 0,
        val name: String = "",
        val description: String = ""
    )
    data class NewTask3(
        val id: Int = 0,
        val name: String = "",
        val description: String = "",
        val extraField: String = "default"
    )
    
    @Test
    fun `test mapIdenticals with extra field in new class`() {
        val old = OldTask3(
            id = 10,
            name = "Old Name",
            description = "Some Description"
        )
        val new = old.mapIdenticals<OldTask3, NewTask3>()
        
        assertEquals(old.id, new.id)
        assertEquals(old.name, new.name)
        assertEquals(old.description, new.description)
        assertEquals("default", new.extraField)
    }
    
    data class OldTask4(
        val id: Int = 0,
        val name: String = "",
        val description: String = "",
        val unusedField: String = ""
    )
    data class NewTask4(
        val id: Int = 0,
        val name: String = ""
    )
    
    @Test
    fun `test mapIdenticals with fewer fields in new class`() {
        val old = OldTask4(
            id = 5,
            name = "Short",
            description = "This won't be mapped",
            unusedField = "Also ignored"
        )
        val new = old.mapIdenticals<OldTask4, NewTask4>()
        
        assertEquals(old.id, new.id)
        assertEquals(old.name, new.name)
    }
    
    data class OldTask5(
        val id: Int = 0,
        val title: String = "Hello",
        val note: String? = "Optional note"
    )
    data class NewTask5(
        val id: Int = 0,
        val title: String = "",
        val note: String = "Default fallback"
    )
    
    @Test
    fun `test mapIdenticals nullable to non-nullable with default`() {
        val old = OldTask5(
            id = 7,
            title = "Nullable test",
            note = null
        )
        val new = old.mapIdenticals<OldTask5, NewTask5>()
        
        assertEquals(old.id, new.id)
        assertEquals(old.title, new.title)
        assertEquals("Default fallback", new.note) // note didn't map because of type mismatch
    }
}