package com.vankorno.sandbox.get

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.LabRat
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox._entities.testEntity.lite.TestLiteEntt
import com.vankorno.sandbox._entities.testEntity.lite._TestLite
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.delete.deleteTable
import com.vankorno.vankornodb.get.steal
import com.vankorno.vankornodb.get.stealALot
import org.junit.Assert.assertEquals
import org.junit.Test

@MediumTest
class GetLiteEntityTest {
    @Test
    fun getsLiteEntityFromSelectedOriginalColumns() {
        val table = "GetLiteEntity1"
        dbh.getLabRats(table)
        
        val entity = dbh.steal(
            _Test.tableNamed(table),
            _TestLite,
            default = TestLiteEntt(),
        ) { Id equal 1L }
        
        assertEquals(1L, entity.id)
        assertEquals(LabRat + 1, entity.name)
        assertEquals(1L, entity.position)
        assertEquals(false, entity.enabled)
        
        dbh.deleteTable(_Test.tableNamed(table))
    }
    
    
    @Test
    fun returnsDefaultWhenNoLiteEntityMatches() {
        val table = "GetLiteEntity2"
        dbh.getLabRats(table)
        val default = TestLiteEntt(id = -100L, name = "default")
        
        val entity = dbh.steal(
            _Test.tableNamed(table),
            _TestLite,
            default = default,
        ) { Id equal -100L }
        
        assertEquals(default, entity)
        
        dbh.deleteTable(_Test.tableNamed(table))
    }
    
    
    @Test
    fun getsMultipleLiteEntities() {
        val table = "GetLiteEntities1"
        dbh.getLabRats(table)
        
        val entities = dbh.stealALot(
            _Test.tableNamed(table),
            _TestLite,
        )
        
        assertEquals(50, entities.size)
        assertEquals((1L..50L).toList(), entities.map { it.id })
        assertEquals((1L..50L).map { LabRat + it }, entities.map { it.name })
        assertEquals((1L..50L).toList(), entities.map { it.position })
        
        dbh.deleteTable(_Test.tableNamed(table))
    }
    
    
    @Test
    fun returnsEmptyListWhenNoLiteEntitiesMatch() {
        val table = "GetLiteEntities2"
        dbh.getLabRats(table)
        
        val entities = dbh.stealALot(
            _Test.tableNamed(table),
            _TestLite,
        ) { Id equal -100L }
        
        assertEquals(emptyList<TestLiteEntt>(), entities)
        
        dbh.deleteTable(_Test.tableNamed(table))
    }
}
