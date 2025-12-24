// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.data

import android.content.ContentValues
import android.database.Cursor
import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.EntityColumns
import com.vankorno.vankornodb.api.LiteEntity
import com.vankorno.vankornodb.api.OldEntity
import kotlin.reflect.KClass


sealed interface BaseOrmBundle<T : BaseEntity> {
    val clazz: KClass<out T>
    val getter: ((Cursor) -> T)?
}


sealed interface NormalOrmBundle<T : NormalEntity>
    : BaseOrmBundle<T>



open class CurrOrmBundle<T : CurrEntity>(
                                             override val clazz: KClass<out T>,
                                            override val getter: ((Cursor)->T)? = null,
                                                     val setter: ((T, ContentValues)->Unit)? = null,
                                                    val columns: EntityColumns? = null,
                                             val createTableSql: String? = null,
) : NormalOrmBundle<T>




open class OldOrmBundle<T : OldEntity>(                   override val clazz: KClass<out T>,
                                                         override val getter: ((Cursor)->T)? = null,
) : NormalOrmBundle<T>




open class LiteOrmBundle<T : LiteEntity>(                 override val clazz: KClass<out T>,
                                                         override val getter: ((Cursor)->T)? = null,
                                                                 val columns: EntityColumns? = null,
) : BaseOrmBundle<T>







