// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.data

/**
 * 
 */
sealed class BaseColumn(val name: String)



sealed class DescendingColumn(name: String): BaseColumn(name)


sealed class AscendingColumn<T>(                                                    name: String,
                                                                              defaultVal: T,
): TypedColumn<T>(name, defaultVal)


sealed class NumberColumn<T: Number>(                                               name: String,
                                                                              defaultVal: T,
): AscendingColumn<T>(name, defaultVal)


class DescendingIntCol(name: String) : DescendingColumn(name)
class DescendingStrCol(name: String) : DescendingColumn(name)
class DescendingBoolCol(name: String) : DescendingColumn(name)
class DescendingLongCol(name: String) : DescendingColumn(name)
class DescendingFloatCol(name: String) : DescendingColumn(name)

