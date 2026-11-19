package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.DslContext
import com.vankorno.vankornodb.api.EntityDsl
import com.vankorno.vankornodb.api.subquery
import com.vankorno.vankornodb.api.withDsl
import com.vankorno.vankornodb.dbManagement.data.CurrSchemaBundle
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.iListCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornodb.dbManagement.migration.data.EntityVersionDsl
import com.vankorno.vankornodb.dbManagement.migration.data._EntityVersion
import org.junit.Assert.*
import org.junit.Test

class EntityDslTest {
    private val versions = _EntityVersion.table("Versions")
    
    @Test
    fun `grouping keeps the entity receiver and preserves argument order`() {
        val name = "O'Brien"
        val result = getQuery(versions.name, where = versions.where {
            Name equal name
            and {
                Version greater 2
                or {}
                or { Notes like "%beta" }
            }
            Id equalAny listOf(10L, 20L)
        })
        
        assertEquals("SELECT * FROM Versions WHERE name=? AND (version>? OR (notes LIKE ?)) AND id IN (?, ?)", result.query)
        assertArrayEquals(arrayOf(name, "2", "%beta", "10", "20"), result.args)
    }
    
    @Test
    fun `subquery switches entity for its projection and its conditions`() {
        val flags = FlagSchema.table("Flags")
        val result = getQuery(versions.name, where = versions.where {
            Version greater 0
            Id.inSubquery(flags, { OwnerId }) {
                Active equalAny arrayOf(true, false)
                and { OwnerId greater 10L }
            }
            notExists(flags) { Active equal false }
            Notes equalCol Name
        })
        
        assertEquals(
            "SELECT * FROM Versions WHERE version>? AND id IN (SELECT ownerId FROM Flags WHERE " +
                "active IN (?, ?) AND (ownerId>?)) AND NOT EXISTS (SELECT 1 FROM Flags WHERE active=?) AND notes=name",
            result.query,
        )
        assertArrayEquals(arrayOf("0", "1", "0", "10", "0"), result.args)
    }
    
    @Test
    fun `correlated subqueries qualify both sides and group additional predicates`() {
        val flags = FlagSchema.table("Flags")
        val result = getQuery(versions.name, where = versions.where {
            exists(subquery(flags, outerColumn = Id, innerColumn = { OwnerId }) {
                Active equal true
                or { OwnerId equal 99L }
            })
        })
        
        assertEquals(
            "SELECT * FROM Versions WHERE EXISTS (SELECT 1 FROM Flags WHERE Flags.ownerId=Versions.id " +
                "AND (active=? OR (ownerId=?)))",
            result.query,
        )
        assertArrayEquals(arrayOf("1", "99"), result.args)
    }
    
    @Test
    fun `expanded columns retain their index and accept primitive varargs`() {
        val flags = FlagSchema.table("Flags")
        val choices = intArrayOf(2, 5)
        val query = subquery(flags, { Scores[0] }) { Scores[1].equalAnyOf(*choices) }
        
        assertEquals("SELECT score1 FROM Flags WHERE score2 IN (?, ?)", query.query)
        assertArrayEquals(arrayOf("2", "5"), query.args)
        assertThrows(IndexOutOfBoundsException::class.java) { flags.select { Scores[2] } }
    }
    
    @Test
    fun `a reusable predicate gets a fresh builder on every execution`() {
        var version = 4
        val receivers = mutableListOf<EntityVersionDsl>()
        val predicate = versions.where {
            receivers += this
            group { Version equal version }
        }
        val first = getQuery(versions.name, where = predicate)
        version = 8
        val second = getQuery(versions.name, where = predicate)
        
        assertEquals("SELECT * FROM Versions WHERE (version=?)", first.query)
        assertEquals(first.query, second.query)
        assertArrayEquals(arrayOf("4"), first.args)
        assertArrayEquals(arrayOf("8"), second.args)
        assertNotSame(receivers.first(), receivers.last())
        assertSame(versions.select { Version }, versions.select { Version })
    }
}

private data class Flag(
                           override val id: Long,
                               val ownerId: Long,
                                val active: Boolean,
                            val scoresList: List<Int> = List(2) { 0 },
) : CurrEntity

private object FlagSchema : CurrSchemaBundle<Flag>(Flag::class) {
    fun table(name: String) = (name using this).withDsl(::FlagDsl)
}

private class FlagDsl(context: DslContext<FlagDsl>) : EntityDsl<Flag, FlagDsl>(context) {
    val OwnerId = column(lCol("ownerId"))
    val Active = column(bCol("active"))
    val Scores get() = column(scoreColumns)
    
    private companion object {
        val scoreColumns = iListCol("score", 2)
    }
}
