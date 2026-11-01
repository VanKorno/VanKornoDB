package com.vankorno.sandbox.add

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRatQuant
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.makeRatsTestNest
import com.vankorno.vankornodb.add.InsertMode
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.misc.data.SharedCol.cID
import org.junit.Assert.assertEquals
import org.junit.Test

private const val TestTable = "AddConvenienceTable"
private val _TestTable = TestTable using _Test

@MediumTest
class AddConvenienceTest {
    val ratQuant = LabRatQuant.toLong()
    
    @Test
    fun addWithAutoIdAndPositionTest() {
        makeRatsTestNest(TestTable)
        sanityCheck()
        checkNew()
        checkCopied()
        checkMoved()
    }
    
    private fun sanityCheck() {
        val lastId = dbh.getLastId(TestTable)
        assertEquals(ratQuant, lastId)
        
        val lastPosition = dbh.getLastPosition(TestTable)
        assertEquals(ratQuant, lastPosition)
    }
    
    private fun checkNew() {
        val name = "new"
        val rat = TestEntity(name = name)
        
        dbh.addWithAutoIdAndPosition(_TestTable, rat, InsertMode.New,
            getPosition = { it.position },
            setIdAndPosition = { entity, id, position -> entity.copy(id = id, position = position) }
        )
        
        val lastId = dbh.getLastId(TestTable)
        assertEquals(ratQuant + 1, lastId)
        
        val lastPosition = dbh.getLastPosition(TestTable)
        assertEquals(ratQuant + 1, lastPosition)
        
        val newRatId = dbh.getLong(TestTable, cID) { Name = name }
        assertEquals(ratQuant + 1, newRatId)
        
        dbh.deleteRows(TestTable) { Name = name }
    }
    
    
    private fun checkCopied() = movedOrCopied("copied", 10, InsertMode.Copy)
    private fun checkMoved() = movedOrCopied("moved", 13, InsertMode.Move)
    
    private fun movedOrCopied(                                                    name: String,
                                                                            idPosition: Long,
                                                                            insertMode: InsertMode,
    ) {
        val rat = TestEntity(id = idPosition, name = name, position = idPosition)
        
        dbh.addWithAutoIdAndPosition(_TestTable, rat, insertMode,
            getPosition = { it.position },
            setIdAndPosition = { entity, id, position -> entity.copy(id = id, position = position) }
        )
        
        val lastId = dbh.getLastId(TestTable)
        assertEquals(ratQuant + 1, lastId)
        
        val lastPosition = dbh.getLastPosition(TestTable)
        assertEquals(ratQuant + 1, lastPosition)
        
        val newRatId = dbh.getLong(TestTable, cID) { Name = name }
        assertEquals(ratQuant + 1, newRatId)
        
        dbh.deleteRows(TestTable) { Name = name }
    }
    
    
    
    
    
}
