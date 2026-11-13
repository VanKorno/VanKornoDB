package com.vankorno.vankornodb.get

import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.api.LiteEntity
import com.vankorno.vankornodb.dbManagement.data.*
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class LiteEntityColumnsTest {
    @Test
    fun `returns lite schema columns in declared order`() {
        val columns = checkedLiteColumns(liteTable, sourceColumns)

        assertEquals(listOf("id", "name", "enabled"), columns.map { it.name })
        assertArrayEquals(liteColumns.columns.toTypedArray(), columns)
    }

    @Test
    fun `rejects a lite column missing from original entity columns`() {
        val error = assertThrows(IllegalStateException::class.java) {
            checkedLiteColumns(liteTable, EntityColumnsFixture(listOf(SourceColumns.Id)))
        }

        assertEquals(
            "Lite column 'name' is not present in the original entity columns",
            error.message
        )
    }

    @Test
    fun `rejects a lite column with a different typed column class`() {
        val wrongTypeColumns = EntityColumnsFixture(
            listOf(SourceColumns.Id, iCol("name"), SourceColumns.Enabled)
        )

        val error = assertThrows(IllegalArgumentException::class.java) {
            checkedLiteColumns(liteTable, wrongTypeColumns)
        }

        assertEquals(
            "Lite column 'name' has type StrCol, but the original entity column has type IntCol",
            error.message
        )
    }

    @Test
    fun `rejects empty lite schema columns`() {
        val emptyLiteTable = TableInfoLite("test", LiteSchemaBundle(LiteFixture::class, columns = EntityColumnsFixture(emptyList())))

        val error = assertThrows(IllegalArgumentException::class.java) {
            checkedLiteColumns(emptyLiteTable, sourceColumns)
        }

        assertEquals("Lite schema for LiteFixture declares no columns", error.message)
    }

    @Test
    fun `rejects duplicate lite column names`() {
        val duplicateColumns = EntityColumnsFixture(listOf(SourceColumns.Id, SourceColumns.Name, sCol("name")))
        val duplicateLiteTable = TableInfoLite(
            "test",
            LiteSchemaBundle(LiteFixture::class, columns = duplicateColumns)
        )

        val error = assertThrows(IllegalArgumentException::class.java) {
            checkedLiteColumns(duplicateLiteTable, sourceColumns)
        }

        assertEquals("Lite schema for LiteFixture declares duplicate column names", error.message)
    }

    @Test
    fun `requires lite schema columns metadata`() {
        val noColumnsTable = TableInfoLite("test", LiteSchemaBundle(LiteFixture::class))

        val error = assertThrows(IllegalStateException::class.java) {
            checkedLiteColumns(noColumnsTable, sourceColumns)
        }

        assertEquals("Lite schema for LiteFixture must declare columns", error.message)
    }

    private data class LiteFixture(
        override val id: Long = -1L,
        val name: String = "",
        val enabled: Boolean = false,
    ) : LiteEntity

    private object SourceColumns : EntityColumns {
        val Id = lCol("id", -1L)
        val Name = sCol("name")
        val Enabled = bCol("enabled")
        val Unused = iCol("unused")

        override val columns = buildColList {
            +Id
            +Name
            +Enabled
            +Unused
        }
    }

    private val sourceColumns = SourceColumns

    private val liteColumns = EntityColumnsFixture(
        listOf(SourceColumns.Id, SourceColumns.Name, SourceColumns.Enabled)
    )

    private val liteTable = TableInfoLite(
        "test",
        LiteSchemaBundle(LiteFixture::class, columns = liteColumns)
    )

    private class EntityColumnsFixture(
        override val columns: List<TypedColumn<*>>,
    ) : EntityColumns
}
