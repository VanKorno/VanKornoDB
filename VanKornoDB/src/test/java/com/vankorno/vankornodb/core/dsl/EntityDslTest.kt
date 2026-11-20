package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.dbManagement.migration.data.EntityVersionDsl
import com.vankorno.vankornodb.dbManagement.migration.data._EntityVersion
import com.vankorno.vankornodb.set.dsl.SetDsl
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertThrows
import org.junit.Test

class EntityDslTest {
    private val versionTable = _EntityVersion.tableNamed("Versions")
    
    @Test
    fun `raw expressions pass through typed and mixed projections verbatim`() {
        val scalar = versionTable.col { raw("version + 1") }
        val mixed = versionTable.columns { Name then raw("length(name)") }
        
        assertEquals("SELECT version + 1 FROM Versions", versionTable.query(scalar.selectors).query)
        assertEquals("SELECT name, length(name) FROM Versions", versionTable.query(mixed.selectors).query)
    }
    
    @Test
    fun `column selections retain table and projection order`() {
        val single = versionTable.col { Id }
        val chained = versionTable.columns { Id then Name then Version }
        val listed = versionTable.columns { listOf(Id, Name, Version) }
        
        assertEquals("SELECT id FROM Versions", single.table.query(single.selectors).query)
        for (selection in listOf(chained, listed))
            assertEquals("SELECT id, name, version FROM Versions", selection.table.query(selection.selectors).query)
    }
    
    @Test
    fun `each query clause sees its own operations and the same entity columns`() {
        val result = versionTable.query(
            columns = arrayOf({ Name }),
            orderBy = { Version.flip() },
            extra = {
                groupBy(Name)
                limit = 1
                offset = 2
            },
            where = { Version greater 3 },
        )
        
        assertEquals("SELECT name FROM Versions WHERE version>? GROUP BY name ORDER BY version DESC LIMIT 1 OFFSET 2", result.query)
        assertArrayEquals(arrayOf("3"), result.args)
        assertEquals("SELECT * FROM Versions", versionTable.query().query)
    }
    
    @Test
    fun `grouping keeps the entity receiver and preserves argument order`() {
        val name = "O'Brien"
        val result = versionTable.query {
            Name equal name
            and {
                Version greater 2
                or {}
                or { Notes like "%beta" }
            }
            Id equalAny listOf(10L, 20L)
        }
        
        assertEquals("SELECT * FROM Versions WHERE name=? AND (version>? OR (notes LIKE ?)) AND id IN (?, ?)", result.query)
        assertArrayEquals(arrayOf(name, "2", "%beta", "10", "20"), result.args)
    }
    
    @Test
    fun `subquery switches entity for its projection and its conditions`() {
        val flags = FlagSchema.tableNamed("Flags")
        val result = versionTable.query {
            Version greater 0
            Id.inSubquery(flags, { OwnerId }) {
                Active equalAny arrayOf(true, false)
                and { OwnerId greater 10L }
            }
            notExists(flags) { Active equal false }
            Notes equalCol Name
        }
        
        assertEquals(
            "SELECT * FROM Versions WHERE version>? AND id IN (SELECT ownerId FROM Flags WHERE " +
                "active IN (?, ?) AND (ownerId>?)) AND NOT EXISTS (SELECT 1 FROM Flags WHERE active=?) AND notes=name",
            result.query,
        )
        assertArrayEquals(arrayOf("0", "1", "0", "10", "0"), result.args)
    }
    
    @Test
    fun `correlated subqueries qualify both sides and group additional predicates`() {
        val flags = FlagSchema.tableNamed("Flags")
        val result = versionTable.query {
            exists(subquery(flags, outerColumn = Id, innerColumn = { OwnerId }) {
                Active equal true
                or { OwnerId equal 99L }
            })
        }
        
        assertEquals(
            "SELECT * FROM Versions WHERE EXISTS (SELECT 1 FROM Flags WHERE Flags.ownerId=Versions.id " +
                "AND (active=? OR (ownerId=?)))",
            result.query,
        )
        assertArrayEquals(arrayOf("1", "99"), result.args)
    }
    
    @Test
    fun `expanded columns retain their index and accept primitive varargs`() {
        val flags = FlagSchema.tableNamed("Flags")
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
        val predicate: EntityVersionDsl.()->Unit = {
            receivers += this
            group { Version equal version }
        }
        val first = versionTable.query(where = predicate)
        version = 8
        val second = versionTable.query(where = predicate)
        
        assertEquals("SELECT * FROM Versions WHERE (version=?)", first.query)
        assertEquals(first.query, second.query)
        assertArrayEquals(arrayOf("4"), first.args)
        assertArrayEquals(arrayOf("8"), second.args)
        assertNotSame(receivers.first(), receivers.last())
        assertEquals("version", versionTable.select { Version }.name)
    }
}

private data class Flag(
                           override val id: Long,
                               val ownerId: Long,
                                val active: Boolean,
                            val scoresList: List<Int> = List(2) { 0 },
) : DbEntity

private object FlagSchema : SchemaBundle<Flag>(Flag::class, columns = buildColList {
        +FlagFields.Id
        +FlagFields.OwnerId
        +FlagFields.Active
        +FlagFields.Scores
    }) {
    fun tableNamed(name: String) = (name using this).withDsl(::FlagDsl, ::FlagOrderDsl, ::FlagExtraDsl, ::FlagSetDsl)
}

private interface FlagDslColumns {
    val OwnerId: EntityColumn<FlagDsl, LongCol>
    val Active: EntityColumn<FlagDsl, BoolCol>
    val Scores: List<EntityColumn<FlagDsl, IntCol>>
}

private class FlagOrderDsl(                                               columns: FlagDslColumns
) : OrderDsl<FlagDsl, FlagOrderDsl>(), FlagDslColumns by columns

private class FlagExtraDsl(                                               columns: FlagDslColumns
) : ExtraDsl<FlagDsl>(), FlagDslColumns by columns

private class FlagSetDsl(                                                 columns: FlagDslColumns
) : SetDsl<FlagDsl>(), FlagDslColumns by columns


private class FlagDsl(                     context: DslContext<FlagDsl>
) : WhereDsl<FlagDsl>(context), FlagDslColumns {
    override val OwnerId get() = column(FlagFields.OwnerId)
    override val Active get() = column(FlagFields.Active)
    override val Scores get() = column(FlagFields.Scores)
}

private object FlagFields {
    val Id = lCol("id")
    val OwnerId = lCol("ownerId")
    val Active = bCol("active")
    val Scores = iListCol("score", 2)
}
