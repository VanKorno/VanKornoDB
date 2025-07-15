package com.vankorno.vankornodb.dbManagement.migration.dsl

import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord
import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class DefineMigrationsTest {
    class V1
    class V2
    class V3
    
    @Test
    fun `adds versioned classes correctly`() {
        val bundle = defineMigrations(2, V2::class) {
            version(1, V1::class)
            version(2, V2::class)
        }
        assertEquals(2, bundle.versionedClasses.size)
        assertEquals(V1::class, bundle.versionedClasses[1])
        assertEquals(V2::class, bundle.versionedClasses[2])
    }
    @Test
    fun `adds missing latest version and class`() {
        val bundle = defineMigrations(3, V3::class) {
            version(1, V1::class)
            version(2, V2::class)
        }
        assertEquals(3, bundle.versionedClasses.size)
        assertEquals(V1::class, bundle.versionedClasses[1])
        assertEquals(V2::class, bundle.versionedClasses[2])
        assertEquals(V3::class, bundle.versionedClasses[3])
    }
    
    
    @Test
    fun `stores rename history correctly`() {
        val bundle = defineMigrations(2, V2::class) {
            version(1, V1::class) {
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
        val bundle = defineMigrations(2, V2::class) {
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
        
        val dsl = TransformCol()
        milestone.transformColVal?.invoke(dsl)
        val override = dsl.getOverride("someField")
        assertNotNull(override)
        assertEquals("123", override?.fromInt?.invoke(123))
    }
    
    @Test
    fun `stores milestone with processFinalObj`() {
        val migrationFunc: (Any, Any) -> Any = { old, _ -> old }
        
        val bundle = defineMigrations(2, V2::class) {
            version(1, V1::class) {
                milestone(processFinalObj = migrationFunc)
            }
        }
        val (_, milestone) = bundle.milestones.first()
        assertEquals("OLD", milestone.processFinalObj?.invoke("OLD", "NEW"))
    }
    
    
    @Test
    fun `works with only latest version`() {
        val bundle = defineMigrations(1, V1::class) { }
    
        assertEquals(mapOf(1 to V1::class), bundle.versionedClasses)
        assertTrue(bundle.renameHistory.isEmpty())
        assertTrue(bundle.milestones.isEmpty())
    }
    
    
    // TODO Check the following tests
    
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
    
    
    
    
    
    
    @Test
    fun `transform returns original if no function is set`() {
        val override = TransformCol.FieldOverride()
        val result = override.apply(123)
        assertEquals(123, result)
    }
    
    @Test
    fun `returns original for unknown input types`() {
        data class Unknown(val x: Int)
        
        val override = TransformCol.FieldOverride()
        val result = override.apply(Unknown(1))
        assertEquals(Unknown(1), result)
    }
    
    
}