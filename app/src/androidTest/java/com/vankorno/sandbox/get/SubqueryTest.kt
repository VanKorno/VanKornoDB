package com.vankorno.sandbox.get

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.core.dsl.raw
import com.vankorno.vankornodb.core.dsl.subquery
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.get.getEntities
import com.vankorno.vankornodb.get.tableExists
import com.vankorno.vankornodb.newTable.createTable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private const val ItemsTableName = "SubqueryItemsTable"
private val SubqueryItems = _TestEntity.tableNamed(ItemsTableName)

private const val ConditionsTableName = "SubqueryConditionsTable"
private val SubqueryConditions = _TestEntity.tableNamed(ConditionsTableName)

@MediumTest
class SubqueryTest : BaseAndroidTest() {
    
    @Before
    fun setupTables() {
        if (dbh.tableExists(ItemsTableName))
            dbh.clearTable(SubqueryItems)
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
            db.createTable(SubqueryConditions)
            db.clearTable(SubqueryConditions)
            db.addEntities(SubqueryConditions, conditions)
        }
    }
    
    
    @Test
    fun inSubqueryInfix() {
        val entities: List<TestEntity> = dbh.getEntities(SubqueryItems) {
            Id inSubquery subquery(
                SubqueryConditions.col { Id },
                where = { Enabled equal true },
            )
        }
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun inSubqueryNonInfix() {
        val entities: List<TestEntity> = dbh.getEntities(SubqueryItems) {
            Id.inSubquery(
                SubqueryConditions.col { Id },
                where = { Enabled equal true },
            )
        }
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun testNotInSubquery() {
        val entities: List<TestEntity> = dbh.getEntities(SubqueryItems) {
            Id notInSubquery subquery(
                SubqueryConditions.col { Id },
                where = { Int1 greater 0 }
            )
        }
        assertEquals(40, entities.size)
        assertEquals((11L..50L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun correlatedExists() {
        val entities: List<TestEntity> = dbh.getEntities(SubqueryItems) {
            exists(subquery(SubqueryConditions.col { Id }, Id) { Enabled equal true })
        }
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun correlatedExistsSubquery() {
        val entities: List<TestEntity> = dbh.getEntities(SubqueryItems) {
            exists(
                subquery(
                    SubqueryConditions.col { Id }, Id,
                    where = {
                        Enabled equal true
                    }
                )
            )
        }
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun correlatedNotExists() {
        val entities: List<TestEntity> = dbh.getEntities(SubqueryItems) {
            notExists(subquery(SubqueryConditions.col { Id }, Id) { Int1 greater 0 })
        }
        assertEquals(40, entities.size)
        assertEquals((11L..50L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun correlatedNotExistsSubquery() {
        val entities: List<TestEntity> = dbh.getEntities(SubqueryItems) {
            notExists(
                subquery(
                    SubqueryConditions.col { Id }, Id,
                    where = {
                        Int1 greater 0
                    }
                )
            )
        }
        assertEquals(40, entities.size)
        assertEquals((11L..50L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun testScalarSubqueryOnLeft() {
        val entities: List<TestEntity> = dbh.getEntities(SubqueryItems) {
            subquery(
                SubqueryConditions.col { Id }, Id, column = { Int1 },
            ) greater 105
        }
        assertEquals(5, entities.size)
        assertEquals((6L..10L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun testInSubqueryNoty() {
        val entities: List<TestEntity> = dbh.getEntities(SubqueryItems) {
            Id inSubquery subquery(
                SubqueryConditions.col { raw("id") },
                where = {
                    Enabled equal true
                }
            )
        }
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
}
