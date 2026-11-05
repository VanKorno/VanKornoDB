/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.api.WhereDsl

/**
 * A convenience fun to avoid tons of ById fun overloads. Used like this:
 * getInt(TableName, Column, whereId(id))
 */
fun whereId(id: Long): WhereDsl.()->Unit = { ID = id }

/**
 * A convenience fun to avoid tons of ById fun overloads. Used like this:
 * getInt(TableName, Column, whereName(name))
 */
fun whereName(name: String): WhereDsl.()->Unit = { Name = name }



