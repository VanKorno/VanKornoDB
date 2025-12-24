// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.dbManagement.data.CurrOrmBundle
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.sCol

data class VersionEntity(
                                  val name: String = "",
                               val version: Int = 0,
                                 val notes: String = "",

                                    val id: Int = -1,
) : CurrEntity



object OrmVersion : CurrOrmBundle<VersionEntity>(
    clazz = VersionEntity::class,
    
    columns = CVersion,
)



object CVersion : EntityColumns {
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