/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.dbManagement.data.CurrSchemaBundle
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol

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
)





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