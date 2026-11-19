package com.vankorno.sandbox.get

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox._entities.testEntity.CTest.Id
import com.vankorno.sandbox._entities.testEntity.CTest.Int1
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.get.getEntityPair
import com.vankorno.vankornodb.get.getEntityPairMap
import com.vankorno.vankornodb.get.getEntityPairs
import com.vankorno.vankornodb.get.getEntityTriple
import com.vankorno.vankornodb.get.getEntityTriples
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

private const val ItemsTableName = "PairTestItemsTable"
private val _ItemsTable = _Test.table(ItemsTableName)

private const val ConditionsTableName = "PairTestConditionsTable"
private val _ConditionsTable = _Test.table(ConditionsTableName)

private const val MetaTableName = "PairTestMetaTable"
private val _MetaTable = _Test.table(MetaTableName)

@MediumTest
class EntityPairsTest : BaseAndroidTest() {
    
    @Before
    fun setupTables() {
        val items = List(10) { idx ->
            val id = (idx + 1).toLong()
            TestEntity(
                id = id,
                name = "Item $id",
                int1 = id.toInt(),
            )
        }
        
        val conditions = List(10) { idx ->
            val id = (idx + 1).toLong()
            TestEntity(
                id = id,
                name = "Cond $id",
                enabled = id <= 5L,
                int1 = if (id <= 5L) 100 + id.toInt() else 0,
            )
        }
        
        val meta = List(10) { idx ->
            val id = (idx + 1).toLong()
            TestEntity(
                id = id,
                name = "Meta $id",
                str1 = "MetaVal_$id",
            )
        }
        
        dbh.write("setupPairTables", async = false) { db ->
            db.createTable(_ItemsTable)
            db.clearTable(ItemsTableName)
            db.addEntities(_ItemsTable, items)
            
            db.createTable(_ConditionsTable)
            db.clearTable(ConditionsTableName)
            db.addEntities(_ConditionsTable, conditions)
            
            db.createTable(_MetaTable)
            db.clearTable(MetaTableName)
            db.addEntities(_MetaTable, meta)
        }
    }
    
    @Test
    fun testGetEntityPairById() {
        val pair = dbh.getEntityPair(_ItemsTable, _ConditionsTable, id = 3L)
        
        assertNotNull(pair)
        assertEquals(3L, pair!!.first.id)
        assertEquals("Item 3", pair.first.name)
        assertEquals(3L, pair.second.id)
        assertEquals(true, pair.second.enabled)
        assertEquals(103, pair.second.int1)
        
        val missing = dbh.getEntityPair(_ItemsTable, _ConditionsTable, id = 999L)
        assertNull(missing)
    }
    
    @Test
    fun testGetEntityPairByWhere() {
        val pair = dbh.getEntityPair(_ItemsTable, _ConditionsTable) {
            Id equal 4L
        }
        
        assertNotNull(pair)
        assertEquals(4L, pair!!.first.id)
        assertEquals("Item 4", pair.first.name)
        assertEquals(4L, pair.second.id)
        assertEquals(true, pair.second.enabled)
        
        val missing = dbh.getEntityPair(_ItemsTable, _ConditionsTable) {
            Id equal 999L
        }
        assertNull(missing)
    }
    
    @Test
    fun testGetEntityPairs() {
        val pairs = dbh.getEntityPairs(_ItemsTable, _ConditionsTable) {
            Int1 lessEqual 5
        }
        
        assertEquals(5, pairs.size)
        for (pair in pairs) {
            assertEquals(pair.first.id, pair.second.id)
            assertEquals(true, pair.second.enabled)
        }
        assertEquals(listOf(1L, 2L, 3L, 4L, 5L), pairs.map { it.first.id })
    }
    
    @Test
    fun testGetEntityPairMap() {
        val pairMap = dbh.getEntityPairMap(_ItemsTable, _ConditionsTable)
        
        assertEquals(10, pairMap.size)
        for (id in 1L..10L) {
            val pair = pairMap[id]
            assertNotNull(pair)
            assertEquals(id, pair!!.first.id)
            assertEquals(id, pair.second.id)
        }
    }
    
    @Test
    fun testGetEntityTripleById() {
        val triple = dbh.getEntityTriple(_ItemsTable, _ConditionsTable, _MetaTable, id = 2L)
        
        assertNotNull(triple)
        assertEquals(2L, triple!!.first.id)
        assertEquals("Item 2", triple.first.name)
        assertEquals(2L, triple.second.id)
        assertEquals(true, triple.second.enabled)
        assertEquals(2L, triple.third.id)
        assertEquals("MetaVal_2", triple.third.str1)
        
        val missing = dbh.getEntityTriple(_ItemsTable, _ConditionsTable, _MetaTable, id = 999L)
        assertNull(missing)
    }
    
    @Test
    fun testGetEntityTriples() {
        val triples = dbh.getEntityTriples(_ItemsTable, _ConditionsTable, _MetaTable) {
            Int1 lessEqual 3
        }
        
        assertEquals(3, triples.size)
        for (triple in triples) {
            assertEquals(triple.first.id, triple.second.id)
            assertEquals(triple.first.id, triple.third.id)
        }
        assertEquals(listOf(1L, 2L, 3L), triples.map { it.first.id })
    }
}
