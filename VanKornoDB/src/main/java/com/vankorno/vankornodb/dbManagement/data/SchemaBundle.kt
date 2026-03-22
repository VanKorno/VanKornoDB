/* SPDX-License-Identifier: MPL-2.0 */
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
    val columns: EntityColumns?
    val getter: ((Cursor) -> T)?
}


sealed interface NormalSchemaBundle<T : NormalEntity> : BaseSchemaBundle<T> {
    val setter: ((T, ContentValues) -> ContentValues)?
    val withId: (T, Long) -> T
}



open class CurrSchemaBundle<T : CurrEntity>(
                                    override val clazz: KClass<out T>,
                                  override val columns: EntityColumns? = null,
                                   override val getter: ((Cursor)->T)? = null,
                                   override val setter: ((T, ContentValues)->ContentValues)? = null,
                                   override val withId: (T, Long)->T = { obj, _ -> obj },
) : NormalSchemaBundle<T>




open class OldSchemaBundle<T : OldEntity>(
                                    override val clazz: KClass<out T>,
                                   override val getter: ((Cursor)->T)? = null,
                                   override val setter: ((T, ContentValues)->ContentValues)? = null,
                                  override val columns: EntityColumns? = null,
                                   override val withId: (T, Long)->T = { obj, _ -> obj },
) : NormalSchemaBundle<T>




open class LiteSchemaBundle<T : LiteEntity>(              override val clazz: KClass<out T>,
                                                         override val getter: ((Cursor)->T)? = null,
                                                        override val columns: EntityColumns? = null,
) : BaseSchemaBundle<T>







