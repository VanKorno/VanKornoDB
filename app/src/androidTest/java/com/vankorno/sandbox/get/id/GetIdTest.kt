package com.vankorno.sandbox.get.id

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox.LabRatQuant
import com.vankorno.sandbox._entities.testEntity.CTest.Int1
import com.vankorno.sandbox._entities.testEntity.CTest.Position
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.makeRatsTestNest
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import com.vankorno.vankornodb.misc.orderByIdFlipped
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val GetLastIdTable = "GetLastIdTestTable"
private const val GetIdTable = "GetIdSingleTestTable"
private const val GetIDsTable = "GetIDsTestTable"
private const val GetAllIDsTable = "GetAllIDsTestTable"

@MediumTest
class GetIdTest {
    private val ratQuant = LabRatQuant.toLong()

    @Test
    fun testGetLastId() {
        makeRatsTestNest(GetLastIdTable)

        val lastId = dbh.getLastId(GetLastIdTable)
        assertEquals(ratQuant, lastId)

        // Add a new row with higher ID
        val newId = 100L
        dbh.addEntities(GetLastIdTable using _Test, listOf(TestEntity(newId, "Lab rat 100", newId)))
        val updatedLastId = dbh.getLastId(GetLastIdTable)
        assertEquals(newId, updatedLastId)

        // Delete the new row
        dbh.deleteRows(GetLastIdTable) { ID = newId }
        val restoredLastId = dbh.getLastId(GetLastIdTable)
        assertEquals(ratQuant, restoredLastId)

        // Empty table
        dbh.clearTable(GetLastIdTable)
        val emptyLastId = dbh.getLastId(GetLastIdTable)
        assertEquals(0L, emptyLastId)
    }

    @Test
    fun testGetId() {
        makeRatsTestNest(GetIdTable)
        
        // Existent by name
        val idByName = dbh.getId(GetIdTable) { Name = LabRat + "5" }
        assertEquals(5L, idByName)
        
        // Existent by position
        val idByPosition = dbh.getId(GetIdTable) { Position equal 10L }
        assertEquals(10L, idByPosition)
        
        // Non-existent
        val id2 = dbh.getId(GetIdTable) { Name = "NonExistentRat" }
        assertEquals(-1L, id2)
        
        // Empty table
        dbh.clearTable(GetIdTable)
        val emptyId = dbh.getId(GetIdTable) { Name = LabRat + "1" }
        assertEquals(-1L, emptyId)
    }

    @Test
    fun testGetIDs() {
        makeRatsTestNest(GetIDsTable)

        // Filter single ID
        val idsSingle = dbh.getIDs(GetIDsTable) { ID = 10L }
        assertEquals(listOf(10L), idsSingle)

        // Filter range of IDs
        val idsRange = dbh.getIDs(GetIDsTable) { cID greater 45L }
        assertEquals(listOf(46L, 47L, 48L, 49L, 50L), idsRange)

        // Filter with custom column (Int1 equals 0 for all rats)
        val idsCustomCol = dbh.getIDs(GetIDsTable) { Int1 equal 0 }
        assertEquals((1L..ratQuant).toList(), idsCustomCol)

        // Filter with orderBy (descending)
        val idsDesc = dbh.getIDs(GetIDsTable, orderByIdFlipped()) { cID greater 47L }
        assertEquals(listOf(50L, 49L, 48L), idsDesc)

        // Filter with equalAnyOf and orderBy
        val idsInList = dbh.getIDs(GetIDsTable, orderByIdFlipped()) { cID.equalAnyOf(5L, 15L, 25L) }
        assertEquals(listOf(25L, 15L, 5L), idsInList)

        // Condition matching nothing
        val idsNone = dbh.getIDs(GetIDsTable) { ID = 9999L }
        assertTrue(idsNone.isEmpty())

        // Empty table
        dbh.clearTable(GetIDsTable)
        val idsEmpty = dbh.getIDs(GetIDsTable) { cID greater 0L }
        assertTrue(idsEmpty.isEmpty())
    }

    @Test
    fun testGetAllIDs() {
        makeRatsTestNest(GetAllIDsTable)

        // Default ascending order
        val allIds = dbh.getAllIDs(GetAllIDsTable)
        assertEquals((1L..ratQuant).toList(), allIds)

        // Explicit descending order
        val allIdsDesc = dbh.getAllIDs(GetAllIDsTable, orderByIdFlipped())
        assertEquals((ratQuant downTo 1L).toList(), allIdsDesc)

        // Add an extra row and verify
        val extraId = 51L
        dbh.addEntities(GetAllIDsTable using _Test, listOf(TestEntity(extraId, "Lab rat 51", extraId)))
        val updatedAllIds = dbh.getAllIDs(GetAllIDsTable)
        assertEquals((1L..extraId).toList(), updatedAllIds)

        // Delete specific rows and verify
        dbh.deleteRows(GetAllIDsTable) { cID.equalAnyOf(2L, 4L) }
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
