// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.add.internal

import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@PublishedApi
internal fun BaseEntity.hasIdField(): Boolean = this::class.memberProperties.any { it.name == "id" }

@PublishedApi
internal fun BaseEntity.getId(): Int = this::class.memberProperties
    .firstOrNull { it.name == "id" }
    ?.getter?.call(this) as? Int ?: -1

@PublishedApi
internal fun <T : BaseEntity> T.withId(                                              newId: Int
): T {
    val kClass = this::class
    val constructor = kClass.primaryConstructor!!
    val args = constructor.parameters.associateWith { param ->
        if (param.name == "id") newId
        else kClass.memberProperties.first { it.name == param.name }.getter.call(this)
    }
    return constructor.callBy(args)
}








