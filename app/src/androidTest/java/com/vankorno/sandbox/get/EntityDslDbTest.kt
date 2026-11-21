package com.vankorno.sandbox.get

import androidx.test.platform.app.InstrumentationRegistry
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.vankornodb.add.addEntity
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.dbManagement.migration.data.EntityVersion
import com.vankorno.vankornodb.dbManagement.migration.data._EntityVersion
import com.vankorno.vankornodb.delete.deleteRows
import com.vankorno.vankornodb.get.getColLongs
import com.vankorno.vankornodb.get.getEntity
import com.vankorno.vankornodb.get.getIDs
import com.vankorno.vankornodb.get.getInt
import com.vankorno.vankornodb.set.setInt
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class EntityDslDbTest {
    @Test
    fun readUpdateAndDeleteWithGeneratedReceivers() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dbName = "entity-where-${UUID.randomUUID()}.db"
        val items = _TestEntity.tableNamed("Items")
        val versions = _EntityVersion.tableNamed("Versions")
        val dbh = Dbh(dbName, 1, context, emptyList(), mustHaveTables = listOf(items.info, versions.info))
        
        try {
            val name = "O'Brien"
            val id = dbh.addEntity(items, TestEntity(name = name, int1 = 7))
            val otherId = dbh.addEntity(items, TestEntity(name = "other", int1 = 3))
            dbh.addEntity(versions, EntityVersion(name = name, version = 2))
            
            val ids = items.col { Id }.orderBy { Id }
            val idsByValue = ids.orderBy { Int1 }
            assertEquals(listOf(otherId, id), dbh.getColLongs(idsByValue))
            assertEquals(listOf(id, otherId), dbh.getColLongs(ids))
            assertEquals(listOf(id, otherId), dbh.getColLongs(idsByValue, orderBy = { Int1.flip() }))
            
            assertEquals(7, dbh.getInt(items.col { Int1 }) { Name equal name })
            assertEquals(listOf(id), dbh.getIDs(items) {
                exists(subquery(Name, versions.col { Name }) {
                    Version equal 2
                })
            })
            assertEquals(listOf(id), dbh.getIDs(items) {
                exists(subquery(Id, items.col { Id }) {
                    Int1 equal 7
                })
            })
            assertEquals(listOf(id), dbh.getIDs(items) {
                Name.inSubquery(versions.col { Name }) { Version greater 1 }
            })
            
            dbh.setInt(9, items.col { Int1 }) {
                Name equal name
                and { Int1 equalAny listOf(7, 8) }
            }
            assertEquals(9, dbh.getEntity(items) { Id equal id }?.int1)
            assertEquals(3, dbh.getInt(items.col { Int1 }) { Id equal otherId })
            
            dbh.deleteRows(items) { Int1 equal 9 }
            assertEquals(listOf(otherId), dbh.getIDs(items) {})
        } finally {
            dbh.closeDb()
            context.deleteDatabase(dbName)
        }
    }
}
