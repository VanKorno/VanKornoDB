package com.vankorno.sandbox._entities.testEntity

/** Column fixture for the instrumentation tests of the original string-table API. */
internal object CTest {
    private val table = _TestEntity.tableNamed("Test")
    val Id = table.select { Id }
    val Name = table.select { Name }
    val Position = table.select { Position }
    val Enabled = table.select { Enabled }
    val Int1 = table.select { Int1 }
    val Int2 = table.select { Int2 }
    val Int3 = table.select { Int3 }
    val Str1 = table.select { Str1 }
    val Str2 = table.select { Str2 }
    val Str3 = table.select { Str3 }
    val Bool1 = table.select { Bool1 }
    val Bool2 = table.select { Bool2 }
    val Bool3 = table.select { Bool3 }
    val Long1 = table.select { Long1 }
    val Long2 = table.select { Long2 }
    val Long3 = table.select { Long3 }
    val Float1 = table.select { Float1 }
    val Float2 = table.select { Float2 }
    val Float3 = table.select { Float3 }
}
