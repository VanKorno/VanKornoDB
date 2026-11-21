package com.vankorno.vankornodb.dbManagement.migration.dsl

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.TransformColDsl
import com.vankorno.vankornodb.api.defineMigrations
import com.vankorno.vankornodb.dbManagement.data.DbEntityWithId
import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DefineMigrationsTest {
    class V1 : DbEntityWithId()
    class V2 : DbEntityWithId()
    
    class CurrV : DbEntityWithId()
    
    
    @Test
    fun `adds versioned orm bundles correctly`() {
        val bundle = defineMigrations(2, CurrV::class) {
            version(1, V1::class)
            version(2, CurrV::class)
        }
        assertEquals(2, bundle.versionedClasses.size)
        assertEquals(V1::class, bundle.versionedClasses[1])
        assertEquals(CurrV::class, bundle.versionedClasses[2])
    }
    @Test
    fun `adds missing latest version and orm bundle`() {
        val bundle = defineMigrations(3, CurrV::class) {
            version(1, V1::class)
            version(2, V2::class)
        }
        assertEquals(3, bundle.versionedClasses.size)
        assertEquals(V1::class, bundle.versionedClasses[1])
        assertEquals(V2::class, bundle.versionedClasses[2])
        assertEquals(CurrV::class, bundle.versionedClasses[3])
    }
    
    @Test
    fun `stores rename history correctly`() {
        val bundle = defineMigrations(2, CurrV::class) {
            version(1, V1::class) {
                rename {
                    "firstA" to "renamedToA"
                    "firstB" to "renamedToB"
                    "firstC" to "renamedToC"
                }
            }
        }
        assertEquals(3, bundle.renameHistory.size)
        assertEquals(RenameRecord(1, "firstA", "renamedToA"), bundle.renameHistory[0])
        assertEquals(RenameRecord(1, "firstB", "renamedToB"), bundle.renameHistory[1])
        assertEquals(RenameRecord(1, "firstC", "renamedToC"), bundle.renameHistory[2])
    }
    
    @Test
    fun `stores milestone with column transformation`() {
        val bundle = defineMigrations(2, CurrV::class) {
            version(1, V1::class) {
                milestone("someField".modify {
                    fromInt = { it.toString() }
                })
            }
        }
        assertEquals(1, bundle.milestones.size)
        val (version, milestone) = bundle.milestones[0]
        assertEquals(1, version)
        assertNotNull(milestone.transformColVal)
        
        val dsl = TransformColDsl()
        milestone.transformColVal?.invoke(dsl)
        val override = dsl.getOverride("someField")
        assertNotNull(override)
        assertEquals("123", override?.fromInt?.invoke(123))
    }
    
    data class Dummy(val value: String) : DbEntityWithId()
    
    @Test
    fun `stores milestone with processFinalEntity`() {
        val migrationFunc: (oldEntity: DbEntity, newEntity: DbEntity) -> DbEntity = { old, _ -> old }
        
        val bundle = defineMigrations(2, CurrV::class) {
            version(1, V1::class) {
                milestone(processFinalEntity = migrationFunc)
            }
        }
        
        val (_, milestone) = bundle.milestones.first()
        
        val old = Dummy("OLD")
        val new = Dummy("NEW")
        
        assertEquals(old, milestone.processFinalEntity?.invoke(old, new))
    }
    
    
    @Test
    fun `works with only latest version`() {
        val bundle = defineMigrations(1, CurrV::class) { }
    
        assertEquals(mapOf(1 to CurrV::class), bundle.versionedClasses)
        assertTrue(bundle.renameHistory.isEmpty())
        assertTrue(bundle.milestones.isEmpty())
    }
    
    
    
    
    @Test
    fun `transform returns original if no function is set`() {
        val override = TransformColDslInternal.ColOverride()
        val result = override.apply(123)
        assertEquals(123, result)
    }
    
    @Test
    fun `returns original for unknown input types`() {
        data class Unknown(val x: Int)
        
        val override = TransformColDslInternal.ColOverride()
        val result = override.apply(Unknown(1))
        assertEquals(Unknown(1), result)
    }
    
    
}
