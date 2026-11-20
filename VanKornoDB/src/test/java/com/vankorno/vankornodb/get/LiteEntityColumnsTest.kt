package com.vankorno.vankornodb.get

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.dbManagement.data.*
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class LiteEntityColumnsTest {
    @Test
    fun `returns lite schema columns in declared order`() {
        val columns = checkedThiefColumns(liteTable.schema, sourceColumns)

        assertEquals(listOf("id", "name", "enabled"), columns.map { it.name })
        assertArrayEquals(liteColumns.toTypedArray(), columns)
    }

    @Test
    fun `rejects a lite column missing from original entity columns`() {
        val error = assertThrows(IllegalStateException::class.java) {
            checkedThiefColumns(liteTable.schema, listOf(SourceColumns.Id))
        }

        assertEquals(
            "Lite column 'name' is not present in the original entity columns",
            error.message
        )
    }

    @Test
    fun `rejects a lite column with a different typed column class`() {
        val wrongTypeColumns = listOf(SourceColumns.Id, iCol("name"), SourceColumns.Enabled)

        val error = assertThrows(IllegalArgumentException::class.java) {
            checkedThiefColumns(liteTable.schema, wrongTypeColumns)
        }

        assertEquals(
            "Lite column 'name' has type StrCol, but the original entity column has type IntCol",
            error.message
        )
    }

    @Test
    fun `rejects empty lite schema columns`() {
        val emptyLiteTable = TableInfo("test", SchemaBundle(LiteFixture::class, columns = emptyList()))

        val error = assertThrows(IllegalArgumentException::class.java) {
            checkedThiefColumns(emptyLiteTable.schema, sourceColumns)
        }

        assertEquals("Lite schema for LiteFixture declares no columns", error.message)
    }

    @Test
    fun `rejects duplicate lite column names`() {
        val duplicateColumns = listOf(SourceColumns.Id, SourceColumns.Name, sCol("name"))
        val duplicateLiteTable = TableInfo(
            "test",
            SchemaBundle(LiteFixture::class, columns = duplicateColumns)
        )

        val error = assertThrows(IllegalArgumentException::class.java) {
            checkedThiefColumns(duplicateLiteTable.schema, sourceColumns)
        }

        assertEquals("Lite schema for LiteFixture declares duplicate column names", error.message)
    }

    @Test
    fun `requires lite schema columns metadata`() {
        val noColumnsTable = TableInfo("test", SchemaBundle(LiteFixture::class))

        val error = assertThrows(IllegalStateException::class.java) {
            checkedThiefColumns(noColumnsTable.schema, sourceColumns)
        }

        assertEquals("Lite schema for LiteFixture must declare columns", error.message)
    }

    private data class LiteFixture(
        override val id: Long = -1L,
        val name: String = "",
        val enabled: Boolean = false,
    ) : DbEntity

    private object SourceColumns {
        val Id = lCol("id", -1L)
        val Name = sCol("name")
        val Enabled = bCol("enabled")
        val Unused = iCol("unused")

        val columns = buildColList {
            +Id
            +Name
            +Enabled
            +Unused
        }
    }

    private val sourceColumns = SourceColumns.columns

    private val liteColumns = listOf(SourceColumns.Id, SourceColumns.Name, SourceColumns.Enabled)

    private val liteTable = TableInfo(
        "test",
        SchemaBundle(LiteFixture::class, columns = liteColumns)
    )

}
