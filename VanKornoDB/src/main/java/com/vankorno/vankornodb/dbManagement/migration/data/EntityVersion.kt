// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.dbManagement.data.CurrSchemaBundle
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.sCol

data class EntityVersion(
                                  val name: String = "",
                               val version: Int = 0,
                                 val notes: String = "",

                                    val id: Int = -1,
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
            id = cursor.getInt(idx++)
        )
    },

    setter = { e, cv ->
        cv.put("name", e.name)
        cv.put("version", e.version)
        cv.put("notes", e.notes)
        cv.put("id", e.id)
        cv
    },
)





object CEntityVersion : EntityColumns {
    val Name = sCol("name", "")
    val Version = iCol("version", 0)
    val Notes = sCol("notes", "")
    val Id = iCol("id", -1)

    override val columns = buildColList {
        +Name
        +Version
        +Notes
        +Id
    }
}