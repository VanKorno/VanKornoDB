// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.core.data.DbConstants.IN
import com.vankorno.vankornodb.core.data.DbConstants.NOT_IN
import com.vankorno.vankornodb.core.data.DbConstants._ID
import com.vankorno.vankornodb.core.data.DbConstants._Name
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol

/**
 *  Internal base — use `WhereDsl` from the api package instead
 */
@Suppress("unused")
open class WhereDslInternal() : WhereDslBase() {
    
    /** To pass your own condition string. Use with caution! (SQL-injection risk)**/
    fun rawClause(str: String) = clauses.add(str)
    
    
    infix fun String.dot(str2: String = "") = this + "." + str2
    
    
    
    // ======================  T Y P E - S A F E   C O M P A R I S O N  ====================== \\
    
    infix fun IntCol.equal(value: Int) = condition(this.name, "=", value.toString())
    infix fun StrCol.equal(value: String) = condition(this.name, "=", value)
    infix fun BoolCol.equal(value: Boolean) = condition(this.name, "=", if (value) "1" else "0")
    infix fun LongCol.equal(value: Long) = condition(this.name, "=", value.toString())
    infix fun FloatCol.equal(value: Float) = condition(this.name, "=", value.toString())
    
    infix fun IntCol.notEqual(value: Int) = condition(this.name, "!=", value.toString())
    infix fun StrCol.notEqual(value: String) = condition(this.name, "!=", value)
    infix fun BoolCol.notEqual(value: Boolean) = condition(this.name, "!=", if (value) "1" else "0")
    infix fun LongCol.notEqual(value: Long) = condition(this.name, "!=", value.toString())
    infix fun FloatCol.notEqual(value: Float) = condition(this.name, "!=", value.toString())
    
    infix fun IntCol.greater(value: Int) = condition(this.name, ">", value.toString())
    infix fun LongCol.greater(value: Long) = condition(this.name, ">", value.toString())
    infix fun FloatCol.greater(value: Float) = condition(this.name, ">", value.toString())
    
    infix fun IntCol.greaterEqual(value: Int) = condition(this.name, ">=", value.toString())
    infix fun LongCol.greaterEqual(value: Long) = condition(this.name, ">=", value.toString())
    infix fun FloatCol.greaterEqual(value: Float) = condition(this.name, ">=", value.toString())
    
    infix fun IntCol.less(value: Int) = condition(this.name, "<", value.toString())
    infix fun LongCol.less(value: Long) = condition(this.name, "<", value.toString())
    infix fun FloatCol.less(value: Float) = condition(this.name, "<", value.toString())
    
    infix fun IntCol.lessEqual(value: Int) = condition(this.name, "<=", value.toString())
    infix fun LongCol.lessEqual(value: Long) = condition(this.name, "<=", value.toString())
    infix fun FloatCol.lessEqual(value: Float) = condition(this.name, "<=", value.toString())
    
    
    
    infix fun IntCol.equalCol(otherCol: IntCol) = conditionRaw(this.name, "=", otherCol.name)
    infix fun StrCol.equalCol(otherCol: StrCol) = conditionRaw(this.name, "=", otherCol.name)
    infix fun BoolCol.equalCol(otherCol: BoolCol) = conditionRaw(this.name, "=", otherCol.name)
    infix fun LongCol.equalCol(otherCol: LongCol) = conditionRaw(this.name, "=", otherCol.name)
    infix fun FloatCol.equalCol(otherCol: FloatCol) = conditionRaw(this.name, "=", otherCol.name)
    
    infix fun IntCol.notEqualCol(otherCol: IntCol) = conditionRaw(this.name, "!=", otherCol.name)
    infix fun StrCol.notEqualCol(otherCol: StrCol) = conditionRaw(this.name, "!=", otherCol.name)
    infix fun BoolCol.notEqualCol(otherCol: BoolCol) = conditionRaw(this.name, "!=", otherCol.name)
    infix fun LongCol.notEqualCol(otherCol: LongCol) = conditionRaw(this.name, "!=", otherCol.name)
    infix fun FloatCol.notEqualCol(otherCol: FloatCol) = conditionRaw(this.name, "!=", otherCol.name)
    
    infix fun IntCol.greaterCol(otherCol: IntCol) = conditionRaw(this.name, ">", otherCol.name)
    infix fun LongCol.greaterCol(otherCol: LongCol) = conditionRaw(this.name, ">", otherCol.name)
    infix fun FloatCol.greaterCol(otherCol: FloatCol) = conditionRaw(this.name, ">", otherCol.name)
    
    infix fun IntCol.greaterEqualCol(otherCol: IntCol) = conditionRaw(this.name, ">=", otherCol.name)
    infix fun LongCol.greaterEqualCol(otherCol: LongCol) = conditionRaw(this.name, ">=", otherCol.name)
    infix fun FloatCol.greaterEqualCol(otherCol: FloatCol) = conditionRaw(this.name, ">=", otherCol.name)
    
    infix fun IntCol.lessCol(otherCol: IntCol) = conditionRaw(this.name, "<", otherCol.name)
    infix fun LongCol.lessCol(otherCol: LongCol) = conditionRaw(this.name, "<", otherCol.name)
    infix fun FloatCol.lessCol(otherCol: FloatCol) = conditionRaw(this.name, "<", otherCol.name)
    
    infix fun IntCol.lessEqualCol(otherCol: IntCol) = conditionRaw(this.name, "<=", otherCol.name)
    infix fun LongCol.lessEqualCol(otherCol: LongCol) = conditionRaw(this.name, "<=", otherCol.name)
    infix fun FloatCol.lessEqualCol(otherCol: FloatCol) = conditionRaw(this.name, "<=", otherCol.name)
    
    
    infix fun StrCol.like(value: String) = condition(this.name, " LIKE ", value)
    infix fun StrCol.notLike(value: String) = condition(this.name, " NOT LIKE ", value)
    
    infix fun StrCol.likeCol(otherCol: StrCol) = conditionRaw(this.name, " LIKE ", otherCol.name)
    infix fun StrCol.notLikeCol(otherCol: StrCol) = conditionRaw(this.name, " NOT LIKE ", otherCol.name)
    
    
    fun StrCol.likeAny(vararg values: String) = multCompare(this.name, " LIKE ", values)
    fun StrCol.notLikeAny(vararg values: String) = multCompare(this.name, " NOT LIKE ", values)
    
    fun StrCol.likeAnyCol(vararg otherCols: StrCol) = multCompareRaw(this.name, " LIKE ", otherCols.map { it.name }.toTypedArray(), true)
    fun StrCol.notLikeAnyCol(vararg otherCols: StrCol) = multCompareRaw(this.name, " NOT LIKE ", otherCols.map { it.name }.toTypedArray(), false)
    
    
    
    infix fun IntCol.equalAny(values: Array<Int>) = multCompare(this.name, IN, values)
    infix fun StrCol.equalAny(values: Array<String>) = multCompare(this.name, IN, values)
    infix fun BoolCol.equalAny(values: Array<Boolean>) = multCompare(this.name, IN, values.map { if (it) "1" else "0" }.toTypedArray())
    infix fun LongCol.equalAny(values: Array<Long>) = multCompare(this.name, IN, values)
    infix fun FloatCol.equalAny(values: Array<Float>) = multCompare(this.name, IN, values)
    
    infix fun IntCol.notEqualAny(values: Array<Int>) = multCompare(this.name, NOT_IN, values)
    infix fun StrCol.notEqualAny(values: Array<String>) = multCompare(this.name, NOT_IN, values)
    infix fun BoolCol.notEqualAny(values: Array<Boolean>) = multCompare(this.name, NOT_IN, values.map { if (it) "1" else "0" }.toTypedArray())
    infix fun LongCol.notEqualAny(values: Array<Long>) = multCompare(this.name, NOT_IN, values)
    infix fun FloatCol.notEqualAny(values: Array<Float>) = multCompare(this.name, NOT_IN, values)
    
    
    infix fun IntCol.equalAny(values: List<Int>) = this equalAny values.toTypedArray()
    infix fun StrCol.equalAny(values: List<String>) = this equalAny values.toTypedArray()
    infix fun BoolCol.equalAny(values: List<Boolean>) = this equalAny values.toTypedArray()
    infix fun LongCol.equalAny(values: List<Long>) = this equalAny values.toTypedArray()
    infix fun FloatCol.equalAny(values: List<Float>) = this equalAny values.toTypedArray()
    
    infix fun IntCol.notEqualAny(values: List<Int>) = this notEqualAny values.toTypedArray()
    infix fun StrCol.notEqualAny(values: List<String>) = this notEqualAny values.toTypedArray()
    infix fun BoolCol.notEqualAny(values: List<Boolean>) = this notEqualAny values.toTypedArray()
    infix fun LongCol.notEqualAny(values: List<Long>) = this notEqualAny values.toTypedArray()
    infix fun FloatCol.notEqualAny(values: List<Float>) = this notEqualAny values.toTypedArray()
    
    
    fun IntCol.equalAnyOf(vararg values: Int) = this equalAny values.toTypedArray()
    fun StrCol.equalAnyOf(vararg values: String) = this equalAny arrayOf(*values)
    fun BoolCol.equalAnyOf(vararg values: Boolean) = this equalAny values.toTypedArray()
    fun LongCol.equalAnyOf(vararg values: Long) = this equalAny values.toTypedArray()
    fun FloatCol.equalAnyOf(vararg values: Float) = this equalAny values.toTypedArray()
    
    fun IntCol.notEqualAnyOf(vararg values: Int) = this notEqualAny values.toTypedArray()
    fun StrCol.notEqualAnyOf(vararg values: String) = this notEqualAny arrayOf(*values)
    fun BoolCol.notEqualAnyOf(vararg values: Boolean) = this notEqualAny values.toTypedArray()
    fun LongCol.notEqualAnyOf(vararg values: Long) = this notEqualAny values.toTypedArray()
    fun FloatCol.notEqualAnyOf(vararg values: Float) = this notEqualAny values.toTypedArray()
    
    
    
    
    
    // ==================  N O T   T Y P E - S A F E   C O M P A R I S O N  ================== \\
    
    /** For comparisons to provided values. The values are automatically put into the arg array. **/
    
    infix fun <T> String.equal(value: T) = condition(this, "=", if (value is Boolean) if (value) "1" else "0" else value.toString())
    infix fun <T> String.notEqual(value: T) = condition(this, "!=", if (value is Boolean) if (value) "1" else "0" else value.toString())
    infix fun <T> String.greater(value: T) = condition(this, ">", value.toString())
    infix fun <T> String.greaterEqual(value: T) = condition(this, ">=", value.toString())
    infix fun <T> String.less(value: T) = condition(this, "<", value.toString())
    infix fun <T> String.lessEqual(value: T) = condition(this, "<=", value.toString())
    
    
    /** For comparisons to values from other columns.
     *  The provided column names aren't treated as values and are not put into the arg array, but put directly inside the query string.
    **/
    infix fun String.equalCol(otherCol: String) = conditionRaw(this, "=", otherCol)
    infix fun String.notEqualCol(otherCol: String) = conditionRaw(this, "!=", otherCol)
    infix fun String.lessCol(otherCol: String) = conditionRaw(this, "<", otherCol)
    infix fun String.lessEqualCol(otherCol: String) = conditionRaw(this, "<=", otherCol)
    infix fun String.greaterCol(otherCol: String) = conditionRaw(this, ">", otherCol)
    infix fun String.greaterEqualCol(otherCol: String) = conditionRaw(this, ">=", otherCol)
    
    
    // TODO Complex nested LIKE and IN:
    // IN (SELECT id FROM Users WHERE banned = 1)
    // LIKE (SELECT default_name FROM Defaults WHERE id = 1)
    // as well as passing raw strings, like with conditionRaw...
    
    infix fun <T> String.like(value: T) = condition(this, " LIKE ", value.toString())
    infix fun <T> String.notLike(value: T) = condition(this, " NOT LIKE ", value.toString())
    
    infix fun String.likeCol(otherCol: String) = conditionRaw(this, " LIKE ", otherCol)
    infix fun String.notLikeCol(otherCol: String) = conditionRaw(this, " NOT LIKE ", otherCol)
    
    
    fun <T> String.likeAny(vararg values: T) = multCompare(this, " LIKE ", values)
    fun <T> String.notLikeAny(vararg values: T) = multCompare(this, " NOT LIKE ", values)
    
    fun String.likeAnyCol(vararg otherCols: String) = multCompareRaw(this, " LIKE ", otherCols, true)
    fun String.notLikeAnyCol(vararg otherCols: String) = multCompareRaw(this, " NOT LIKE ", otherCols, false)
    
    fun <T> String.equalAny(vararg values: T) = multCompare(this, IN, values)
    fun <T> String.notEqualAny(vararg values: T) = multCompare(this, NOT_IN, values)
    
    
    
    
    
    // ===============================  C O N V E N I E N C E  =============================== \\
    
    var ID: Int
        get() = error("id is write-only")
        set(value) {
            condition(_ID, "=", value.toString())
        }
    
    var Name: String
        get() = error("Name is write-only")
        set(value) {
            condition(_Name, "=", value)
        }
    
    
    
    
    
    
    
    
}