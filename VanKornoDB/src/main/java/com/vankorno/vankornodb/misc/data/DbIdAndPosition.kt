/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc.data

/**
 * Holds an entity ID and position pair.
 */
data class DbIdAndPosition(
                                    val id: Long = -1L,
                              val position: Long = -1L,
) {
    /** Returns true if both ID and position are valid (not -1L). */
    val isValid: Boolean get() = id != -1L && position != -1L
    
    
    /** Creates [DbIdAndPosition] using the first element of [ids] and [positions]. */
    constructor(ids: List<Long>?, positions: List<Long>?) : this(
        id = ids?.firstOrNull() ?: -1L,
        position = positions?.firstOrNull() ?: -1L,
    )
    
    /** Creates [DbIdAndPosition] using the first element of each list in [idsAndPositions]. */
    constructor(idsAndPositions: Pair<List<Long>, List<Long>>?) : this(
        ids = idsAndPositions?.first,
        positions = idsAndPositions?.second,
    )
    
    /** Creates [DbIdAndPosition] using the first element of each sublist in [idsAndPositions]. */
    constructor(idsAndPositions: List<List<Long>>?) : this(
        ids = idsAndPositions?.getOrNull(0),
        positions = idsAndPositions?.getOrNull(1),
    )
}

