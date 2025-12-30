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


sealed interface BaseSchemaBundle<T : BaseEntity> {
    val clazz: KClass<out T>
    val getter: ((Cursor) -> T)?
}


sealed interface NormalSchemaBundle<T : NormalEntity> : BaseSchemaBundle<T> {
    val setter: ((T, ContentValues) -> ContentValues)?
}



open class CurrSchemaBundle<T : CurrEntity>(
                                    override val clazz: KClass<out T>,
                                           val columns: EntityColumns? = null,
                                   override val getter: ((Cursor)->T)? = null,
                                   override val setter: ((T, ContentValues)->ContentValues)? = null,
) : NormalSchemaBundle<T>




open class OldSchemaBundle<T : OldEntity>(
                                    override val clazz: KClass<out T>,
                                   override val getter: ((Cursor)->T)? = null,
                                   override val setter: ((T, ContentValues)->ContentValues)? = null,
) : NormalSchemaBundle<T>




open class LiteSchemaBundle<T : LiteEntity>(              override val clazz: KClass<out T>,
                                                         override val getter: ((Cursor)->T)? = null,
                                                                 val columns: EntityColumns? = null,
) : BaseSchemaBundle<T>







