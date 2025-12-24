package com.vankorno.vankornodb.dbManagement.migration

import com.vankorno.vankornodb.api.CurrEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class DbMigrationListHandlingTest : MigrationUtils() {
    data class OldWithList(val ids: List<Int> = listOf(1, 2, 3)) : CurrEntity
    data class NewWithList(val ids: List<Int> = listOf(0, 0, 0)) : CurrEntity
    
    data class OldWithStringList(val tags: List<String> = listOf("a", "b", "c")) : CurrEntity
    data class NewWithStringList(val tags: List<String> = listOf("", "", "")) : CurrEntity
    
    data class OldWithDifferentListTypes(val nums: List<Int> = listOf(1, 2), val strs: List<String> = listOf("x", "y")) : CurrEntity
    data class NewWithDifferentListTypes(val nums: List<Int> = listOf(0, 0), val strs: List<String> = listOf("", "")) : CurrEntity
    
    data class OldWithListRename(val oldIds: List<Int> = listOf(9, 8, 7)) : CurrEntity
    data class NewWithListRename(val newIds: List<Int> = listOf(0, 0, 0)) : CurrEntity
    
    @Test
    fun `list of ints is copied as is`() {
        val old = OldWithList()
        val new = convertEntity(old, NewWithList::class) as NewWithList
        assertEquals(old.ids, new.ids)
    }
    
    @Test
    fun `list of strings is copied as is`() {
        val old = OldWithStringList()
        val new = convertEntity(old, NewWithStringList::class) as NewWithStringList
        assertEquals(old.tags, new.tags)
    }
    
    @Test
    fun `multiple lists of identical types are copied`() {
        val old = OldWithDifferentListTypes()
        val new = convertEntity(old, NewWithDifferentListTypes::class) as NewWithDifferentListTypes
        assertEquals(old.nums, new.nums)
        assertEquals(old.strs, new.strs)
    }
    
    @Test
    fun `renamed list property is copied correctly`() {
        val old = OldWithListRename()
        val renameMap = mapOf("newIds" to "oldIds")
        val new = convertEntity(old, NewWithListRename::class, renameMap) as NewWithListRename
        assertEquals(old.oldIds, new.newIds)
    }
    
    @Test
    fun `list with different element types fallback to default`() {
        data class Old(val items: List<Int> = listOf(1, 2)) : CurrEntity
        data class New(val items: List<String> = listOf("default")) : CurrEntity
        
        val old = Old()
        val new = convertEntity(old, New::class) as New
        assertEquals(listOf("default"), new.items)
    }
}