package com.vankorno.vankornodb.dbManagement.migration.dsl

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.EntitySpec
import com.vankorno.vankornodb.api.TransformColDsl
import com.vankorno.vankornodb.api.defineMigrations
import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord
import org.junit.Assert.*
import org.junit.Test

class DefineMigrationsTest {
    class V1 : DbEntity
    class V2 : DbEntity
    class V3 : DbEntity
    object SpecV1 : EntitySpec<V1>(V1::class)
    object SpecV2 : EntitySpec<V2>(V2::class)
    object SpecV3 : EntitySpec<V3>(V3::class)
    
    @Test
    fun `adds versioned specs correctly`() {
        val bundle = defineMigrations(2, SpecV2) {
            version(1, SpecV1)
            version(2, SpecV2)
        }
        assertEquals(2, bundle.versionedSpecs.size)
        assertEquals(SpecV1, bundle.versionedSpecs[1])
        assertEquals(SpecV2, bundle.versionedSpecs[2])
    }
    @Test
    fun `adds missing latest version and spec`() {
        val bundle = defineMigrations(3, SpecV3) {
            version(1, SpecV1)
            version(2, SpecV2)
        }
        assertEquals(3, bundle.versionedSpecs.size)
        assertEquals(SpecV1, bundle.versionedSpecs[1])
        assertEquals(SpecV2, bundle.versionedSpecs[2])
        assertEquals(SpecV3, bundle.versionedSpecs[3])
    }
    
    
    @Test
    fun `stores rename history correctly`() {
        val bundle = defineMigrations(2, SpecV2) {
            version(1, SpecV1) {
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
        val bundle = defineMigrations(2, SpecV2) {
            version(1, SpecV1) {
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
    
    data class Dummy(val value: String) : DbEntity
    
    @Test
    fun `stores milestone with processFinalObj`() {
        val migrationFunc: (oldObj: BaseEntity, newObj: BaseEntity) -> BaseEntity = { old, _ -> old }
        
        val bundle = defineMigrations(2, SpecV2) {
            version(1, SpecV1) {
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
        val bundle = defineMigrations(1, SpecV1) { }
    
        assertEquals(mapOf(1 to SpecV1), bundle.versionedSpecs)
        assertTrue(bundle.renameHistory.isEmpty())
        assertTrue(bundle.milestones.isEmpty())
    }
    
    
    /* TODO Check the following tests
    @Test
    fun `throws error on duplicate rename for same field in same version`() {
        val exception = assertFailsWith<IllegalStateException> {
            defineMigrations(2, V2::class) {
                version(1, V1::class) {
                    rename {
                        "newest" from "putin" to "hujlo"
                        "newest" from "putin" to "motherfucker" // duplicate in same version
                    }
                }
            }
        }
        assertTrue("Duplicate rename for field" in (exception.message ?: ""))
    }
    @Test
    fun `throws error on duplicate rename for different fields with same FROM in same version`() {
        val exception = assertFailsWith<IllegalStateException> {
            defineMigrations(2, V2::class) {
                version(1, V1::class) {
                    rename {
                        "newest" from "putin" to "hujlo"
                        "differentNewest" from "putin" to "motherfucker"
                    }
                }
            }
        }
        assertTrue("Duplicate rename for field" in (exception.message ?: ""))
    }
    @Test
    fun `throws error on duplicate rename for different fields with same TO in same version`() {
        val exception = assertFailsWith<IllegalStateException> {
            defineMigrations(2, V2::class) {
                version(1, V1::class) {
                    rename {
                        "newest" from "putin" to "motherfucker"
                        "differentNewest" from "TuckerCarlson" to "motherfucker"
                    }
                }
            }
        }
        assertTrue("Duplicate rename for field" in (exception.message ?: ""))
    }
    */
    
    
    
    
    
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