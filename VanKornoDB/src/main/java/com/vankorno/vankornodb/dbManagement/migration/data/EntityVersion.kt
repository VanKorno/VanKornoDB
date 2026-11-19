/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.*
import com.vankorno.vankornodb.dbManagement.data.*

data class EntityVersion(
                                  val name: String = "",
                               val version: Int = 0,
                                 val notes: String = "",

                           override val id: Long = -1L,
) : CurrEntity





object _EntityVersion : CurrSchemaBundle<EntityVersion>(
    clazz = EntityVersion::class,

    columns = CEntityVersion,

    getter = { cursor ->
        var idx = 0

        EntityVersion(
            name = cursor.getString(idx++),
            version = cursor.getInt(idx++),
            notes = cursor.getString(idx++),
            id = cursor.getLong(idx++)
        )
    },

    setter = { e, cv ->
        cv.put("name", e.name)
        cv.put("version", e.version)
        cv.put("notes", e.notes)
        cv.put("id", e.id)
        cv
    },

    withId = { obj, newId -> obj.copy(id = newId) }
) {
    fun table(name: String): QueryTable<TableInfoCurr<EntityVersion>, EntityVersionDsl, EntityVersionOrderDsl, EntityVersionExtraDsl> = (name using this).withDsl(::EntityVersionDsl, ::EntityVersionOrderDsl, ::EntityVersionExtraDsl)
}


interface EntityVersionDslColumns {
    val Name: EntityColumn<EntityVersionDsl, StrCol>
    val Version: EntityColumn<EntityVersionDsl, IntCol>
    val Notes: EntityColumn<EntityVersionDsl, StrCol>
    val Id: EntityColumn<EntityVersionDsl, LongCol>
}

class EntityVersionOrderDsl internal constructor(                  columns: EntityVersionDslColumns
) : EntityOrderDsl<EntityVersionDsl>(), EntityVersionDslColumns by columns

class EntityVersionExtraDsl internal constructor(                  columns: EntityVersionDslColumns
) : EntityExtraDsl<EntityVersionDsl>(), EntityVersionDslColumns by columns


class EntityVersionDsl internal constructor(                  context: DslContext<EntityVersionDsl>
) : EntityDsl<EntityVersion, EntityVersionDsl>(context), EntityVersionDslColumns {
    override val Name get() = column(sCol("name", ""))
    override val Version get() = column(iCol("version", 0))
    override val Notes get() = column(sCol("notes", ""))
    override val Id get() = column(lCol("id", -1L))
}




// Hopefully this won't be needed later
object CEntityVersion : EntityColumns {
    val Name = sCol("name", "")
    val Version = iCol("version", 0)
    val Notes = sCol("notes", "")
    val Id = lCol("id", -1L)

    override val columns = buildColList {
        +Name
        +Version
        +Notes
        +Id
    }
}
