/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.data

import android.content.ContentValues
import android.database.Cursor
import com.vankorno.vankornodb.api.DbEntity
import kotlin.reflect.KClass


open class SchemaBundle<T : DbEntity>(       val clazz: KClass<out T>,
                                            val getter: ((Cursor)->T)? = null,
                                            val setter: ((T, ContentValues)->ContentValues)? = null,
                                            val withId: (T, Long)->T = { entity, _ -> entity },
                                           val columns: List<TypedColumn<*>>? = null,
)
