/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.core.dsl.*
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.set.dsl.SetDsl

data class EntityVersion(
                                  val name: String = "",
                               val version: Int = 0,
                                 val notes: String = "",

                           override val id: Long = -1L,
) : DbEntity





object _EntityVersion : SchemaBundle<EntityVersion>(
    clazz = EntityVersion::class,
    columns = buildColList {
        +EntityVersionFields.Name
        +EntityVersionFields.Version
        +EntityVersionFields.Notes
        +EntityVersionFields.Id
    },

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
    fun tableNamed(name: String): FullDslTable<TableInfo<EntityVersion>, EntityVersionDsl, EntityVersionOrderDsl, EntityVersionExtraDsl, EntityVersionSetDsl> = (name using this).withDsl(::EntityVersionDsl, ::EntityVersionOrderDsl, ::EntityVersionExtraDsl, ::EntityVersionSetDsl)
}


interface EntityVersionDslColumns {
    val Name: Field<EntityVersionDsl, StrCol>
    val Version: Field<EntityVersionDsl, IntCol>
    val Notes: Field<EntityVersionDsl, StrCol>
}

class EntityVersionOrderDsl internal constructor(                  columns: EntityVersionDslColumns
) : OrderDsl<EntityVersionDsl, EntityVersionOrderDsl>(), EntityVersionDslColumns by columns

class EntityVersionExtraDsl internal constructor(                  columns: EntityVersionDslColumns
) : ExtraDsl<EntityVersionDsl>(), EntityVersionDslColumns by columns


class EntityVersionSetDsl(                                         columns: EntityVersionDslColumns
) : SetDsl<EntityVersionDsl>(), EntityVersionDslColumns by columns


class EntityVersionDsl internal constructor(                  context: DslContext<EntityVersionDsl>
) : WhereDsl<EntityVersionDsl>(context), EntityVersionDslColumns {
    override val Name get() = column(EntityVersionFields.Name)
    override val Version get() = column(EntityVersionFields.Version)
    override val Notes get() = column(EntityVersionFields.Notes)
}

private object EntityVersionFields {
    val Name = sCol("name", "")
    val Version = iCol("version", 0)
    val Notes = sCol("notes", "")
    val Id = lCol("id", -1L)
}
