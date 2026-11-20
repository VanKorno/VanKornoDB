package com.vankorno.sandbox.get

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.api.createTable
import com.vankorno.vankornodb.core.dsl.subquery
import com.vankorno.vankornodb.delete.clearTable
import com.vankorno.vankornodb.get.getEntities
import com.vankorno.vankornodb.get.tableExists
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.nio.file.Files.exists
import java.nio.file.Files.notExists

private const val ItemsTableName = "SubqueryItemsTable"
private val _ItemsTable = _Test.tableNamed(ItemsTableName)

private const val ConditionsTableName = "SubqueryConditionsTable"
private val _ConditionsTable = _Test.tableNamed(ConditionsTableName)

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
    fun inSubqueryInfix() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            Id inSubquery subquery(
                table = ConditionsTableName,
                column = Id,
                where = { Enabled equal true },
            )
        }
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun inSubqueryNonInfix() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            Id.inSubquery(
                table = ConditionsTableName,
                column = Id,
                where = { Enabled equal true },
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
                where = { Int1 greater 0 }
            )
        }
        assertEquals(40, entities.size)
        assertEquals((11L..50L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun correlatedExists() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            exists(ConditionsTableName) {
                (ConditionsTableName dot Id) equalCol (ItemsTableName dot Id)
                Enabled equal true
            }
        }
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun correlatedExistsSubquery() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            exists(
                subquery(
                    table = ConditionsTableName,
                    column = "1",
                    where = {
                        (ConditionsTableName dot Id) equalCol (ItemsTableName dot Id)
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
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            notExists(ConditionsTableName) {
                (ConditionsTableName dot Id) equalCol (ItemsTableName dot Id)
                Int1 greater 0
            }
        }
        assertEquals(40, entities.size)
        assertEquals((11L..50L).toList(), entities.map { it.id })
    }
    
    
    @Test
    fun correlatedNotExistsSubquery() {
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            notExists(
                subquery(
                    table = ConditionsTableName,
                    column = "1",
                    where = {
                        (ConditionsTableName dot Id) equalCol (ItemsTableName dot Id)
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
        val entities: List<TestEntity> = dbh.getEntities(_ItemsTable) {
            subquery(
                table = ConditionsTableName,
                column = Int1,
                where = {
                    (ConditionsTableName dot Id) equalCol (ItemsTableName dot Id)
                }
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
                }
            )
        }
        assertEquals(10, entities.size)
        assertEquals((1L..10L).toList(), entities.map { it.id })
    }
}
