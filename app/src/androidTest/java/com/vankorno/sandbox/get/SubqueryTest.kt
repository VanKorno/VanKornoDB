package com.vankorno.sandbox.get

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox._entities.testEntity.CTest.Enabled
import com.vankorno.sandbox._entities.testEntity.CTest.Id
import com.vankorno.sandbox._entities.testEntity.CTest.Int1
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.delete.clearTable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private const val ItemsTableName = "SubqueryItemsTable"
private val _ItemsTable = ItemsTableName using _Test

private const val ConditionsTableName = "SubqueryConditionsTable"
private val _ConditionsTable = ConditionsTableName using _Test

@MediumTest
class SubqueryTest : BaseAndroidTest() {
    
    @Before
    fun setupTables() {
        if (dbh.tableExists(ItemsTableName))
            dbh.clearTable(ItemsTableName)
        dbh.getLabRats(ItemsTableName)
        
        val conditions = List(50) { idx ->
            val id = (idx + 1).toLong()
            TestEntity(
                id = id,
                name = "Cond $id",
                enabled = id <= 10L,
                int1 = if (id <= 10L) 100 + id.toInt() else 0,
            )
        }
        
        dbh.write("setupConditions", async = false) { db ->
            db.createTable(_ConditionsTable)
            db.clearTable(ConditionsTableName)
            db.addEntities(_ConditionsTable, conditions)
        }
    }
    
    @Test
    fun testInSubqueryTyped() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            Id inSubquery subquery(
                table = ConditionsTableName,
                column = Id,
                where = {
                    Enabled equal true
                },
            )
        }
        
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
    
    @Test
    fun testInSubqueryTableInfo() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            Id inSubquery subquery(
                table = _ConditionsTable,
                column = Id,
                where = {
                    Enabled equal true
                },
            )
        }
        
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
    
    @Test
    fun testNotInSubquery() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            Id notInSubquery subquery(
                table = ConditionsTableName,
                column = Id,
                where = {
                    Int1 greater 0
                },
            )
        }
        
        assertEquals(40, entities.size)
        assertEquals((11L..50L).toList(), entities.map { it.id })
    }
    
    @Test
    fun testCorrelatedExistsSubquery() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            exists(
                subquery(
                    table = ConditionsTableName,
                    column = "1",
                    where = {
                        (ConditionsTableName dot Id) equalCol (ItemsTableName dot Id)
                        Enabled equal true
                    },
                ),
            )
        }
        
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
    
    @Test
    fun testCorrelatedNotExistsSubquery() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            notExists(
                subquery(
                    table = ConditionsTableName,
                    column = "1",
                    where = {
                        (ConditionsTableName dot Id) equalCol (ItemsTableName dot Id)
                        Int1 greater 0
                    },
                ),
            )
        }
        
        assertEquals(40, entities.size)
        assertEquals((11L..50L).toList(), entities.map { it.id })
    }
    
    @Test
    fun testScalarSubqueryOnLeft() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            subquery(
                table = ConditionsTableName,
                column = Int1,
                where = {
                    (ConditionsTableName dot Id) equalCol (ItemsTableName dot Id)
                },
            ) greater 105
        }
        
        assertEquals(5, entities.size)
        assertEquals((6L..10L).toList(), entities.map { it.id })
    }
    
    @Test
    fun testInSubqueryNoty() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            "id" inSubquery subquery(
                table = ConditionsTableName,
                column = "id",
                where = {
                    "enabled" equal true
                },
            )
        }
        
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
}
