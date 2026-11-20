package com.vankorno.sandbox.add

import androidx.test.filters.MediumTest
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.add.addEntitiesWithoutIdCheck
import com.vankorno.vankornodb.add.addEntityWithoutIdCheck
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.delete.deleteRows
import com.vankorno.vankornodb.get.getLong
import org.junit.Assert.assertEquals
import org.junit.Test

private const val TestTable = "AddWithoutIdCheckTable"
private val _TestTable = _Test.table(TestTable)

@MediumTest
class AddEntityWithoutIdCheckTest {
    @Test
    fun addEntityWithoutIdCheckTest() {
        dbh.getLabRats(TestTable)
        
        val name = "Splinter"
        val id = 777L
        val rat = TestEntity(id, name, 777)
        
        val insertedRowId = dbh.addEntityWithoutIdCheck(_TestTable, rat)
        assertEquals(true, insertedRowId > 0L)
        
        val storedId = dbh.getLong(_TestTable.col { Id }) { Name equal name }
        assertEquals(id, storedId)
        
        dbh.deleteRows(_TestTable) { Name equal name }
    }
    
    @Test
    fun addEntitiesWithoutIdCheckTest() {
        dbh.getLabRats(TestTable)
        
        val rat1 = TestEntity(777, "Splinter", 777)
        val rat2 = TestEntity(778, "Ratatouille", 778)
        
        val insertedRowIds = dbh.addEntitiesWithoutIdCheck(
            _TestTable,
            listOf(rat1, rat2),
        )
        assertEquals(2, insertedRowIds.size)
        assertEquals(true, insertedRowIds.all { it > 0L })
        
        val storedId1 = dbh.getLong(_TestTable.col { Id }) { Name equal rat1.name }
        val storedId2 = dbh.getLong(_TestTable.col { Id }) { Name equal rat2.name }
        assertEquals(rat1.id, storedId1)
        assertEquals(rat2.id, storedId2)
        
        dbh.deleteRows(_TestTable) { Name equal rat1.name }
        dbh.deleteRows(_TestTable) { Name equal rat2.name }
    }
}
