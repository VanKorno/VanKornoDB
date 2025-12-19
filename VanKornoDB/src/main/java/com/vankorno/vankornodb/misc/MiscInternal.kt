// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.dbManagement.data.TypedColumn


internal fun getColNames(columns: Array<out TypedColumn<*>>): Array<String> = columns.map { it.name }.toTypedArray()
