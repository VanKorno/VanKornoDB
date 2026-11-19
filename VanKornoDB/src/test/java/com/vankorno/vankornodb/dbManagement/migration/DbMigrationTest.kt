package com.vankorno.vankornodb.dbManagement.migration

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.dbManagement.data.DbEntityWithId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DbMigrationTest : MigrationUtils() {
    data class OldV1(override val id: Long = 0L, val title: String = "", val note: String = "") : DbEntity
    data class NewV2(override val id: Long = 0L, val name: String = "", val note: String = "") : DbEntity
    
    @Test
    fun `basic field rename with version map`() {
        val old = OldV1(id = 1L, title = "Task 1", note = "Some note")
        val renameMap = mapOf(Pair("name", "title"))
        
        val new = convertEntity(old, NewV2::class, renameMap) as NewV2
        
        assertEquals(old.id, new.id)
        assertEquals(old.title, new.name)
        assertEquals(old.note, new.note)
    }
    
    data class OldV2(override val id: Long = 0L, val title: String = "", val extra: String = "") : DbEntity
    data class NewV3(override val id: Long = 0L, val name: String = "", val description: String = "default") : DbEntity
    
    @Test
    fun `missing fields fallback to default`() {
        val old = OldV2(id = 2L, title = "Legacy", extra = "ignored")
        val renameMap = mapOf(Pair("name", "title"))
        
        val new = convertEntity(old, NewV3::class, renameMap) as NewV3
        
        assertEquals(2L, new.id)
        assertEquals("Legacy", new.name)
        assertEquals("default", new.description)
    }
    
    data class OldV3(override val id: Long = 0L, val text: String = "abc") : DbEntity
    data class NewV4(override val id: Long = 0L, val text: String = "", val extra: Boolean = true) : DbEntity
    
    @Test
    fun `unmapped field fallback with primitive default`() {
        val old = OldV3(id = 3, text = "abc")
        val new = convertEntity(old, NewV4::class) as NewV4
        
        assertEquals(old.id, new.id)
        assertEquals(old.text, new.text)
        assertTrue(new.extra) // overridden by defaultValueForParam()
    }
    
    
    
    data class OldNumV1(val active: Boolean = true, val score: Int = 42) : DbEntityWithId()
    data class NewNumV2(val active: Int = 0, val score: Double = 0.0) : DbEntityWithId()
    
    @Test
    fun `type auto-conversion for basic numeric types`() {
        val old = OldNumV1(active = true, score = 99)
        val new = convertEntity(old, NewNumV2::class) as NewNumV2
        
        assertEquals(1, new.active)
        assertEquals(99.0, new.score, 0.0001)
    }
    
    data class OldNumV2(val value: Double = 1.0) : DbEntityWithId()
    data class NewNumV3(val value: Boolean = false) : DbEntityWithId()
    
    @Test
    fun `type auto-conversion from Long and Double to Int and Boolean`() {
        val old = OldNumV2(value = 0.0)
        val new = convertEntity(old, NewNumV3::class) as NewNumV3
        
        assertFalse(new.value)
    }
    
    data class OldPartial(val data: Float = 1.1f) : DbEntityWithId()
    data class NewPartial(val data: String = "not convertible") : DbEntityWithId()
    
    @Test
    fun `unsupported type conversion fallback to default`() {
        val old = OldPartial()
        val new = convertEntity(old, NewPartial::class) as NewPartial
        
        assertEquals("not convertible", new.data) // uses constructor default
    }
    
    
    
}
