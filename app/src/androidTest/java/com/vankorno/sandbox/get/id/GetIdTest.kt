package com.vankorno.sandbox.get.id

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox.LabRatQuant
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.delete.deleteRows
import com.vankorno.vankornodb.get.id.getAllIDs
import com.vankorno.vankornodb.get.id.getIDs
import com.vankorno.vankornodb.get.id.getId
import com.vankorno.vankornodb.get.id.getLastId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private val GetLastIdTable = _TestEntity.tableNamed("GetLastIdTestTable")
private val GetIdTable = _TestEntity.tableNamed("GetIdSingleTestTable")
private val GetIDsTable = _TestEntity.tableNamed("GetIDsTestTable")
private val GetAllIDsTable = _TestEntity.tableNamed("GetAllIDsTestTable")

@MediumTest
class GetIdTest {
    private val ratQuant = LabRatQuant.toLong()

    @Test
    fun testGetLastId() {
        dbh.getLabRats(GetLastIdTable.name)

        val lastId = dbh.getLastId(GetLastIdTable)
        assertEquals(ratQuant, lastId)

        // Add a new row with higher ID
        val newId = 100L
        dbh.addEntities(GetLastIdTable, listOf(TestEntity(newId, "Lab rat 100", newId)))
        val updatedLastId = dbh.getLastId(GetLastIdTable)
        assertEquals(newId, updatedLastId)

        // Delete the new row
        dbh.deleteRows(GetLastIdTable) { Id equal newId }
        val restoredLastId = dbh.getLastId(GetLastIdTable)
        assertEquals(ratQuant, restoredLastId)

        // Empty table
        dbh.clearTable(GetLastIdTable)
        val emptyLastId = dbh.getLastId(GetLastIdTable)
        assertEquals(0L, emptyLastId)
    }

    @Test
    fun testGetId() {
        dbh.getLabRats(GetIdTable.name)
        
        // Existent by name
        val idByName = dbh.getId(GetIdTable) { Name equal LabRat + "5" }
        assertEquals(5L, idByName)
        
        // Existent by position
        val idByPosition = dbh.getId(GetIdTable) { Position equal 10L }
        assertEquals(10L, idByPosition)
        
        // Non-existent
        val id2 = dbh.getId(GetIdTable) { Name equal "NonExistentRat" }
        assertEquals(-1L, id2)
        
        // Empty table
        dbh.clearTable(GetIdTable)
        val emptyId = dbh.getId(GetIdTable) { Name equal LabRat + "1" }
        assertEquals(-1L, emptyId)
    }

    @Test
    fun testGetIDs() {
        dbh.getLabRats(GetIDsTable.name)

        // Filter single ID
        val idsSingle = dbh.getIDs(GetIDsTable) { Id equal 10L }
        assertEquals(listOf(10L), idsSingle)

        // Filter range of IDs
        val idsRange = dbh.getIDs(GetIDsTable) { Id greater 45L }
        assertEquals(listOf(46L, 47L, 48L, 49L, 50L), idsRange)

        // Filter with custom column (Int1 equals 0 for all rats)
        val idsCustomCol = dbh.getIDs(GetIDsTable) { Int1 equal 0 }
        assertEquals((1L..ratQuant).toList(), idsCustomCol)

        // Filter with orderBy (descending)
        val idsDesc = dbh.getIDs(GetIDsTable, orderBy = { Id.flip() }) { Id greater 47L }
        assertEquals(listOf(50L, 49L, 48L), idsDesc)

        // Filter with equalAnyOf and orderBy
        val idsInList = dbh.getIDs(GetIDsTable, orderBy = { Id.flip() }) { Id.equalAnyOf(5L, 15L, 25L) }
        assertEquals(listOf(25L, 15L, 5L), idsInList)

        // Condition matching nothing
        val idsNone = dbh.getIDs(GetIDsTable) { Id equal 9999L }
        assertTrue(idsNone.isEmpty())

        // Empty table
        dbh.clearTable(GetIDsTable)
        val idsEmpty = dbh.getIDs(GetIDsTable) { Id greater 0L }
        assertTrue(idsEmpty.isEmpty())
    }

    @Test
    fun testGetAllIDs() {
        dbh.getLabRats(GetAllIDsTable.name)

        // Default ascending order
        val allIds = dbh.getAllIDs(GetAllIDsTable)
        assertEquals((1L..ratQuant).toList(), allIds)

        // Explicit descending order
        val allIdsDesc = dbh.getAllIDs(GetAllIDsTable, orderBy = { Id.flip() })
        assertEquals((ratQuant downTo 1L).toList(), allIdsDesc)

        // Add an extra row and verify
        val extraId = 51L
        dbh.addEntities(GetAllIDsTable, listOf(TestEntity(extraId, "Lab rat 51", extraId)))
        val updatedAllIds = dbh.getAllIDs(GetAllIDsTable)
        assertEquals((1L..extraId).toList(), updatedAllIds)

        // Delete specific rows and verify
        dbh.deleteRows(GetAllIDsTable) { Id.equalAnyOf(2L, 4L) }
        val remainingIds = dbh.getAllIDs(GetAllIDsTable)
        assertEquals(49, remainingIds.size)
        assertFalse(remainingIds.contains(2L))
        assertFalse(remainingIds.contains(4L))

        // Empty table
        dbh.clearTable(GetAllIDsTable)
        val emptyAllIds = dbh.getAllIDs(GetAllIDsTable)
        assertTrue(emptyAllIds.isEmpty())
    }
}
