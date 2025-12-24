package com.vankorno.vankornodb.dbManagement.migration.dsl

import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.OldEntity
import com.vankorno.vankornodb.api.TransformColDsl
import com.vankorno.vankornodb.api.defineMigrations
import com.vankorno.vankornodb.dbManagement.data.CurrOrmBundle
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.OldOrmBundle
import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord
import org.junit.Assert.*
import org.junit.Test

class DefineMigrationsTest {
    class V1 : OldEntity
    object OrmV1 : OldOrmBundle<V1>(V1::class)
    class V2 : OldEntity
    object OrmV2 : OldOrmBundle<V2>(V2::class)
    
    class CurrV : CurrEntity
    object OrmCurr: CurrOrmBundle<CurrV>(CurrV::class)
    
    
    @Test
    fun `adds versioned orm bundles correctly`() {
        val bundle = defineMigrations(2, OrmCurr) {
            version(1, OrmV1)
            version(2, OrmCurr)
        }
        assertEquals(2, bundle.versionedSpecs.size)
        assertEquals(OrmV1, bundle.versionedSpecs[1])
        assertEquals(OrmCurr, bundle.versionedSpecs[2])
    }
    @Test
    fun `adds missing latest version and orm bundle`() {
        val bundle = defineMigrations(3, OrmCurr) {
            version(1, OrmV1)
            version(2, OrmV2)
        }
        assertEquals(3, bundle.versionedSpecs.size)
        assertEquals(OrmV1, bundle.versionedSpecs[1])
        assertEquals(OrmV2, bundle.versionedSpecs[2])
        assertEquals(OrmCurr, bundle.versionedSpecs[3])
    }
    
    @Test
    fun `stores rename history correctly`() {
        val bundle = defineMigrations(2, OrmCurr) {
            version(1, OrmV1) {
                rename {
                    "latestA" from "firstA" to "renamedToA"
                    "latestB" from "firstB" to "renamedToB"
                }
            }
        }
        assertEquals(2, bundle.renameHistory.size)
        assertEquals(listOf(RenameRecord(1, "firstA", "renamedToA")), bundle.renameHistory["latestA"])
        assertEquals(listOf(RenameRecord(1, "firstB", "renamedToB")), bundle.renameHistory["latestB"])
    }
    
    @Test
    fun `stores milestone with column transformation`() {
        val bundle = defineMigrations(2, OrmCurr) {
            version(1, OrmV1) {
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
    
    data class Dummy(val value: String) : CurrEntity
    
    @Test
    fun `stores milestone with processFinalObj`() {
        val migrationFunc: (oldObj: NormalEntity, newObj: NormalEntity) -> NormalEntity = { old, _ -> old }
        
        val bundle = defineMigrations(2, OrmCurr) {
            version(1, OrmV1) {
                milestone(processFinalObj = migrationFunc)
            }
        }
        
        val (_, milestone) = bundle.milestones.first()
        
        val old = Dummy("OLD")
        val new = Dummy("NEW")
        
        assertEquals(old, milestone.processFinalObj?.invoke(old, new))
    }
    
    
    @Test
    fun `works with only latest version`() {
        val bundle = defineMigrations(1, OrmCurr) { }
    
        assertEquals(mapOf(1 to OrmCurr), bundle.versionedSpecs)
        assertTrue(bundle.renameHistory.isEmpty())
        assertTrue(bundle.milestones.isEmpty())
    }
    
    
    
    
    @Test
    fun `transform returns original if no function is set`() {
        val override = TransformColDslInternal.FieldOverride()
        val result = override.apply(123)
        assertEquals(123, result)
    }
    
    @Test
    fun `returns original for unknown input types`() {
        data class Unknown(val x: Int)
        
        val override = TransformColDslInternal.FieldOverride()
        val result = override.apply(Unknown(1))
        assertEquals(Unknown(1), result)
    }
    
    
}