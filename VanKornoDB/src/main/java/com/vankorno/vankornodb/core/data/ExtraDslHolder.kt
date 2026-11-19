/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.data

internal class ExtraDslHolder {
    val joins = mutableListOf<String>()
    val joinArgs = mutableListOf<String>()
    var groupBy = ""
    var having = ""
    var havingArgs = emptyArray<String>()
    var limit: Int? = null
    var offset: Int? = null
    var customEnd = ""
}
