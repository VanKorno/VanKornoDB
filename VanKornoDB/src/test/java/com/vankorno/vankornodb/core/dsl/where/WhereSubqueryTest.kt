/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.TestConstants.cBool
import com.vankorno.vankornodb.TestConstants.cInt
import com.vankorno.vankornodb.TestConstants.cLong
import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.core.dsl.getQuery
import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.misc.columns
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereSubqueryTest {
    
    @Test
    fun `Subquery with inSubquery using TypedColumn`() {
        val result = getQuery(
            table = "Items",
            columns = columns(cLong),
            where = {
                cLong inSubquery subquery(
                    table = "Conditions",
                    column = cLong,
                    where = {
                        cBool equal true
                    },
                )
            },
        )
        
        assertEquals(
            "SELECT cLong FROM Items WHERE cLong IN (SELECT cLong FROM Conditions WHERE cBool=?)",
            result.query,
        )
        assertEquals(
            "1",
            result.args.joinToString(comma),
        )
    }
    
    @Test
    fun `Direct inSubquery and notInSubquery on TypedColumn`() {
        val resultExplicitCol = getQuery(
            table = "Items",
            columns = columns(cLong),
            where = {
                cLong.inSubquery("Conditions", cLong) {
                    cBool equal true
                }
            },
        )
        assertEquals(
            "SELECT cLong FROM Items WHERE cLong IN (SELECT cLong FROM Conditions WHERE cBool=?)",
            resultExplicitCol.query,
        )
        assertEquals("1", resultExplicitCol.args.joinToString(comma))
        
        val resultDefaultCol = getQuery(
            table = "Items",
            columns = columns(cLong),
            where = {
                cLong.inSubquery("Conditions") {
                    cBool equal true
                }
            },
        )
        assertEquals(
            "SELECT cLong FROM Items WHERE cLong IN (SELECT cLong FROM Conditions WHERE cBool=?)",
            resultDefaultCol.query,
        )
        assertEquals("1", resultDefaultCol.args.joinToString(comma))
        
        val resultNotIn = getQuery(
            table = "Items",
            columns = columns(cLong),
            where = {
                cLong.notInSubquery("Blocked") {
                    cBool equal true
                }
            },
        )
        assertEquals(
            "SELECT cLong FROM Items WHERE cLong NOT IN (SELECT cLong FROM Blocked WHERE cBool=?)",
            resultNotIn.query,
        )
        assertEquals("1", resultNotIn.args.joinToString(comma))
    }
    
    @Test
    fun `Subquery with inSubquery using String`() {
        val result = getQuery(
            table = "Items",
            columns = columns("id"),
            where = {
                "id" inSubquery subquery(
                    table = "Conditions",
                    column = "id",
                    where = {
                        "active" equal true
                        "level" greater 5
                    },
                )
            },
        )
        
        assertEquals(
            "SELECT id FROM Items WHERE id IN (SELECT id FROM Conditions WHERE active=? AND level>?)",
            result.query,
        )
        assertEquals(
            arrayOf("1", "5").joinToString(comma),
            result.args.joinToString(comma),
        )
    }
    
    @Test
    fun `Subquery with notInSubquery and equalAny`() {
        val resultNotIn = getQuery(
            table = "Items",
            where = {
                cInt notInSubquery subquery(
                    table = "Banned",
                    column = cInt,
                    where = {
                        cInt greater 100
                    },
                )
            },
        )
        assertEquals(
            "SELECT * FROM Items WHERE cInt NOT IN (SELECT cInt FROM Banned WHERE cInt>?)",
            resultNotIn.query,
        )
        assertEquals("100", resultNotIn.args.joinToString(comma))
        
        val resultEqualAny = getQuery(
            table = "Items",
            where = {
                cInt equalAny subquery(
                    table = "Allowed",
                    column = cInt,
                )
            },
        )
        assertEquals(
            "SELECT * FROM Items WHERE cInt IN (SELECT cInt FROM Allowed)",
            resultEqualAny.query,
        )
    }
    
    @Test
    fun `Subquery with exists and notExists`() {
        val resultExists = getQuery(
            table = "Items",
            where = {
                exists(
                    subquery(
                        table = "Conditions",
                        column = "1",
                        where = {
                            ("Conditions" dot cLong) equalCol ("Items" dot cLong)
                            cBool equal true
                        },
                    ),
                )
            },
        )
        
        assertEquals(
            "SELECT * FROM Items WHERE EXISTS (SELECT 1 FROM Conditions WHERE Conditions.cLong=Items.cLong AND cBool=?)",
            resultExists.query,
        )
        assertEquals("1", resultExists.args.joinToString(comma))
        
        val resultNotExists = getQuery(
            table = "Items",
            where = {
                notExists(
                    subquery(
                        table = "Blocked",
                        column = "1",
                        where = {
                            ("Blocked" dot cLong) equalCol ("Items" dot cLong)
                        },
                    ),
                )
            },
        )
        
        assertEquals(
            "SELECT * FROM Items WHERE NOT EXISTS (SELECT 1 FROM Blocked WHERE Blocked.cLong=Items.cLong)",
            resultNotExists.query,
        )
    }
    
    @Test
    fun `Column equal subquery and NumberColumn compare subquery`() {
        val visibleAtHome = bCol("visibleAtHome")
        val itemId = lCol("id")
        val priority = iCol("priority")
        
        val result = getQuery(
            table = "Items",
            where = {
                visibleAtHome equal subquery(
                    table = "Conditions",
                    column = visibleAtHome,
                    where = {
                        ("Conditions" dot itemId) equalCol ("Items" dot itemId)
                    },
                )
                priority greater subquery(
                    table = "Thresholds",
                    column = priority,
                    where = {
                        priority greater 10
                    },
                )
            },
        )
        
        assertEquals(
            "SELECT * FROM Items WHERE visibleAtHome=(SELECT visibleAtHome FROM Conditions WHERE Conditions.id=Items.id) AND priority>(SELECT priority FROM Thresholds WHERE priority>?)",
            result.query,
        )
        assertEquals(
            "10",
            result.args.joinToString(comma),
        )
    }
    
    @Test
    fun `Subquery with multiple columns`() {
        val colA = iCol("colA")
        val colB = iCol("colB")
        
        val result = getQuery(
            table = "Main",
            where = {
                exists(
                    subquery(
                        table = "Sub",
                        columns = columns(colA, colB),
                        where = {
                            colA equal 1
                        },
                    ),
                )
            },
        )
        
        assertEquals(
            "SELECT * FROM Main WHERE EXISTS (SELECT colA, colB FROM Sub WHERE colA=?)",
            result.query,
        )
        assertEquals("1", result.args.joinToString(comma))
    }
    
    @Test
    fun `Two tables visibility check using IN subquery`() {
        val itemId = lCol("id")
        val conditionA = bCol("condA")
        val minLevel = iCol("minLvl")
        
        val result = getQuery(
            table = "HomeItems",
            where = {
                itemId inSubquery subquery(
                    table = "VisibilityConditions",
                    column = itemId,
                    where = {
                        conditionA equal true
                        minLevel greaterEqual 3
                    },
                )
            },
        )
        
        assertEquals(
            "SELECT * FROM HomeItems WHERE id IN (SELECT id FROM VisibilityConditions WHERE condA=? AND minLvl>=?)",
            result.query,
        )
        assertEquals(
            arrayOf("1", "3").joinToString(comma),
            result.args.joinToString(comma),
        )
    }
    
    @Test
    fun `Two tables visibility check using correlated EXISTS subquery`() {
        val itemId = lCol("id")
        val conditionA = bCol("condA")
        
        val result = getQuery(
            table = "HomeItems",
            where = {
                exists(
                    subquery(
                        table = "VisibilityConditions",
                        column = "1",
                        where = {
                            ("VisibilityConditions" dot itemId) equalCol ("HomeItems" dot itemId)
                            conditionA equal true
                        },
                    ),
                )
            },
        )
        
        assertEquals(
            "SELECT * FROM HomeItems WHERE EXISTS (SELECT 1 FROM VisibilityConditions WHERE VisibilityConditions.id=HomeItems.id AND condA=?)",
            result.query,
        )
        assertEquals("1", result.args.joinToString(comma))
    }
}
