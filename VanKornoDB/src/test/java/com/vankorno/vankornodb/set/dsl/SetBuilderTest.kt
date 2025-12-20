package com.vankorno.vankornodb.set.dsl

import com.vankorno.vankornodb.api.SetDsl
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.set.dsl.data.FloatColOp
import com.vankorno.vankornodb.set.dsl.data.IntColOp
import com.vankorno.vankornodb.set.dsl.data.LongColOp
import com.vankorno.vankornodb.set.dsl.data.MathOp
import com.vankorno.vankornodb.set.dsl.data.SetOp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SetBuilderTest {
    
    // Base functionality
    
    @Test
    fun `simple set and add produce correct ops`() {
        val a = IntCol("a")
        
        val ops = SetDsl().apply {
            a setTo 5
            a add 3
        }.ops
        
        assertEquals(
            listOf(
                SetOp.Set(a, 5),
                SetOp.NumOp("a", 3, "+"),
            ),
            ops
        )
    }
    
    
    @Test
    fun `int mult by one produces no op`() {
        val a = IntCol("a")
        
        val ops = SetDsl().apply { a mult 1 }.ops
        
        assertTrue(ops.isEmpty())
    }
    
    
    @Test
    fun `int mult by zero becomes set to zero`() {
        val a = IntCol("a")
        
        val ops = SetDsl().apply { a mult 0 }.ops
        
        assertEquals(listOf(SetOp.Set(a, 0)), ops )
    }
    
    @Test
    fun `bool flip and setAs`() {
        val a = BoolCol("a")
        val b = BoolCol("b")
        
        val ops = SetDsl().apply {
            a.flip()
            a setAs b
        }.ops
        
        assertEquals(
            listOf(
                SetOp.Flip(a),
                SetOp.SetAs("a", "b")
            ),
            ops
        )
    }
    
    @Test
    fun `int setAs without math`() {
        val a = IntCol("a")
        val b = IntCol("b")
        
        val ops = SetDsl().apply { a setAs b }.ops
        
        assertEquals(listOf(SetOp.SetAs("a", "b")), ops)
    }
    
    @Test
    fun `bool setAs`() {
        val a = BoolCol("a")
        val b = BoolCol("b")
        
        val ops = SetDsl().apply { a setAs b }.ops
        
        assertEquals(listOf(SetOp.SetAs("a", "b")), ops)
    }
    
    @Test
    fun `mixed ops order preserved`() {
        val a = IntCol("a")
        val b = IntCol("b")
        
        val ops = SetDsl().apply {
            a setTo 1
            a setAs b
            a add 3
        }.ops
        
        assertEquals(
            listOf(
                SetOp.Set(a, 1),
                SetOp.SetAs("a", "b"),
                SetOp.NumOp("a", 3, "+"),
            ),
            ops
        )
    }
    
    
    
    @Test
    fun `col to col with math setAs produces correct ops`() {
        val a = IntCol("a")
        val b = IntCol("b")
        
        val ops = SetDsl().apply {
            a setAs (b andAdd 5)
            a setAs (b andMult 3)
            a setAs (b andCoerceIn 0..10)
            a setAs (b andCapAt 100)
            a setAs (b andFloorAt 50)
        }.ops
        
        assertEquals(
            listOf(
                SetOp.SetAsModified(a.name, IntColOp("b", 5, MathOp.Add)),
                SetOp.SetAsModified(a.name, IntColOp("b", 3, MathOp.Mult)),
                SetOp.SetAsModified(a.name, IntColOp("b", 0, MathOp.CoerceIn(0, 10))),
                SetOp.SetAsModified(a.name, IntColOp("b", 100, MathOp.CapAt(100))),
                SetOp.SetAsModified(a.name, IntColOp("b", 50, MathOp.FloorAt(50))),
            ),
            ops
        )
    }
    
    @Test
    fun `long col to col with math setAs produces correct ops`() {
        val a = LongCol("a")
        val b = LongCol("b")
        
        val ops = SetDsl().apply {
            a setAs (b andAdd 5L)
            a setAs (b andMult 3L)
        }.ops
        
        assertEquals(
            listOf(
                SetOp.SetAsModified(a.name, LongColOp("b", 5L, MathOp.Add)),
                SetOp.SetAsModified(a.name, LongColOp("b", 3L, MathOp.Mult)),
            ),
            ops
        )
    }
    
    @Test
    fun `float col to col with math setAs produces correct ops`() {
        val a = FloatCol("a")
        val b = FloatCol("b")
        
        val ops = SetDsl().apply {
            a setAs (b andAdd 5.5F)
            a setAs (b andDiv 2F)
        }.ops
        
        assertEquals(
            listOf(
                SetOp.SetAsModified(a.name, FloatColOp("b", 5.5F, MathOp.Add)),
                SetOp.SetAsModified(a.name, FloatColOp("b", 2F, MathOp.Div)),
            ),
            ops
        )
    }
    
    
    
    
    
    
    
    
    // All functionality
    
    private val bananas = IntCol("bananas")
    private val unicorns = LongCol("unicorns")
    private val magic = FloatCol("magic")
    private val chaos = BoolCol("chaos")

    @Test
    fun `string column is set to cursed artifact`() {
        val b = SetDsl().apply {
            "mystery_box" setTo "💩42"
        }
        
        assertEquals(
            listOf(SetOp.SetNoty("mystery_box", "💩42")),
            b.ops
        )
    }
    
    @Test
    fun `chaos is turned OFF to save the universe`() {
        val b = SetDsl().apply { OFF(chaos) }
        
        assertEquals(
            listOf(SetOp.Set(chaos, false)),
            b.ops
        )
    }
    
    @Test
    fun `chaos is turned ON because nobody learned anything`() {
        val b = SetDsl().apply { ON(chaos) }
        
        assertEquals(
            listOf(SetOp.Set(chaos, true)),
            b.ops
        )
    }
    
    @Test
    fun `bananas are subtracted for diet reasons`() {
        val b = SetDsl().apply {
            bananas sub 13
        }

        assertEquals(
            listOf(SetOp.NumOp("bananas", -13, "+")),
            b.ops
        )
    }

    @Test
    fun `unicorn population is reduced dramatically`() {
        val b = SetDsl().apply {
            unicorns sub 666L
        }
        
        assertEquals(
            listOf(SetOp.NumOp("unicorns", -666L, "+")),
            b.ops
        )
    }
    
    @Test
    fun `magic is drained slightly but catastrophically`() {
        val b = SetDsl().apply {
            magic sub 3.14f
        }
        
        assertEquals(
            listOf(SetOp.NumOp("magic", -3.14f, "+")),
            b.ops
        )
    }
    
    @Test
    fun `unicorns are multiplied beyond safe limits`() {
        val b = SetDsl().apply {
            unicorns mult 9001
        }
        
        assertEquals(
            listOf(SetOp.NumOp("unicorns", 9001L, "*")),
            b.ops
        )
    }
    
    @Test
    fun `magic is multiplied until reality glitches`() {
        val b = SetDsl().apply {
            magic mult 1.618f
        }
        
        assertEquals(
            listOf(SetOp.NumOp("magic", 1.618f, "*")),
            b.ops
        )
    }
    
    @Test
    fun `bananas become absolute like forced positivity`() {
        val b = SetDsl().apply {
            bananas.abs()
        }
        
        assertEquals(
            listOf(SetOp.Abs("bananas")),
            b.ops
        )
    }
    
    @Test
    fun `bananas are forced into acceptable social range`() {
        val b = SetDsl().apply {
            bananas coerceIn 1..10
        }
        
        assertEquals(
            listOf(SetOp.CoerceIn("bananas", 1, 10)),
            b.ops
        )
    }
    
    @Test
    fun `unicorns are capped because infinite unicorns are illegal`() {
        val b = SetDsl().apply {
            unicorns capAt 9999999L
        }
        
        assertEquals(
            listOf(SetOp.MinMax("unicorns", 9999999L, false)),
            b.ops
        )
    }
    
    @Test
    fun `magic is humbled to minimum survivable amount`() {
        val b = SetDsl().apply {
            magic floorAt 0.01f
        }
        
        assertEquals(
            listOf(SetOp.MinMax("magic", 0.01f, true)),
            b.ops
        )
    }
    
    @Test
    fun `unicorns copy values from suspicious source`() {
        val sourceOfTruth = LongCol("source_of_truth")
        val b = SetDsl().apply {
            unicorns setAs sourceOfTruth
        }
        
        assertEquals(
            listOf(SetOp.SetAs("unicorns", "source_of_truth")),
            b.ops
        )
    }
    
    @Test
    fun `magic steals power from neighbouring wizard`() {
        val neighbourMagic = FloatCol("neighbour_magic")
        val b = SetDsl().apply {
            magic setAs neighbourMagic
        }
        
        assertEquals(
            listOf(SetOp.SetAs("magic", "neighbour_magic")),
            b.ops
        )
    }
    
    
    
    
    
}