/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.core.dsl.where

import com.vankorno.vankornodb.core.data.DbConstants.comma
import com.vankorno.vankornodb.core.dsl.builderTable
import com.vankorno.vankornodb.core.dsl.query
import com.vankorno.vankornodb.core.dsl.raw
import org.junit.Assert.assertEquals
import org.junit.Test

class WhereSubqueryTest {
    
    @Test
    fun `Subquery with inSubquery using TypedColumn`() {
        val result = builderTable("Items").col { LongValue }.query(
            where = {
                LongValue inSubquery builderTable("Conditions").col { LongValue }.query(
                    where = {
                        Flag equal true
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
        val resultExplicitCol = builderTable("Items").col { LongValue }.query(
            where = {
                LongValue.inSubquery(builderTable("Conditions"), { LongValue }) {
                    Flag equal true
                }
            },
        )
        assertEquals(
            "SELECT cLong FROM Items WHERE cLong IN (SELECT cLong FROM Conditions WHERE cBool=?)",
            resultExplicitCol.query,
        )
        assertEquals("1", resultExplicitCol.args.joinToString(comma))
        
        val resultDefaultCol = builderTable("Items").col { LongValue }.query(
            where = {
                LongValue.inSubquery(builderTable("Conditions"), { LongValue }) {
                    Flag equal true
                }
            },
        )
        assertEquals(
            "SELECT cLong FROM Items WHERE cLong IN (SELECT cLong FROM Conditions WHERE cBool=?)",
            resultDefaultCol.query,
        )
        assertEquals("1", resultDefaultCol.args.joinToString(comma))
        
        val resultNotIn = builderTable("Items").col { LongValue }.query(
            where = {
                LongValue.notInSubquery(builderTable("Blocked"), { LongValue }) {
                    Flag equal true
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
        val result = builderTable("Items").col { Id }.query(
            where = {
                Id inSubquery builderTable("Conditions").col { Id }.query(
                    where = {
                        Active equal true
                        Level greater 5
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
        val resultNotIn = builderTable("Items").query(
            where = {
                Num notInSubquery builderTable("Banned").col { Num }.query(
                    where = {
                        Num greater 100
                    },
                )
            },
        )
        assertEquals(
            "SELECT * FROM Items WHERE cInt NOT IN (SELECT cInt FROM Banned WHERE cInt>?)",
            resultNotIn.query,
        )
        assertEquals("100", resultNotIn.args.joinToString(comma))
        
        val resultEqualAny = builderTable("Items").query(
            where = {
                Num equalAny builderTable("Allowed").col { Num }.query(
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
        val resultExists = builderTable("Items").query(
            where = {
                exists(
                    builderTable("Conditions").col { raw("1") }.query(
                        where = {
                            rawClause("Conditions.cLong=Items.cLong")
                            Flag equal true
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
        
        val resultNotExists = builderTable("Items").query(
            where = {
                notExists(
                    builderTable("Blocked").col { raw("1") }.query(
                        where = {
                            rawClause("Blocked.cLong=Items.cLong")
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
        
        val result = builderTable("Items").query(
            where = {
                Visible equal builderTable("Conditions").col { Visible }.query(
                    where = {
                        rawClause("Conditions.id=Items.id")
                    },
                )
                Priority greater builderTable("Thresholds").col { Priority }.query(
                    where = {
                        Priority greater 10
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
        
        val result = builderTable("Main").query(
            where = {
                exists(
                    builderTable("Sub").columns { A then B }.query(
                        where = {
                            A equal 1
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
        
        val result = builderTable("HomeItems").query(
            where = {
                Id inSubquery builderTable("VisibilityConditions").col { Id }.query(
                    where = {
                        Condition equal true
                        MinLevel greaterEqual 3
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
        
        val result = builderTable("HomeItems").query(
            where = {
                exists(
                    builderTable("VisibilityConditions").col { raw("1") }.query(
                        where = {
                            rawClause("VisibilityConditions.id=HomeItems.id")
                            Condition equal true
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
