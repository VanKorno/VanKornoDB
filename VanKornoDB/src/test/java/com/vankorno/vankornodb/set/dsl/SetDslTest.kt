package com.vankorno.vankornodb.set.dsl

import com.vankorno.vankornodb.core.dsl.Field
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import com.vankorno.vankornodb.set.dsl.data.FloatColOp
import com.vankorno.vankornodb.set.dsl.data.IntColOp
import com.vankorno.vankornodb.set.dsl.data.LongColOp
import com.vankorno.vankornodb.set.dsl.data.MathOp
import com.vankorno.vankornodb.set.dsl.data.SetOp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SetDslTest {
    private class TestScope : SetDsl<TestScope>()

    
    // Base functionality
    
    @Test
    fun `simple set and add produce correct ops`() {
        val a = Field<TestScope, IntCol>(IntCol("a"))
        
        val ops = TestScope().apply {
            a setTo 5
            a add 3
        }.ops
        
        assertEquals(
            listOf(
                SetOp.Set(a.definition, 5),
                SetOp.NumOp("a", 3, "+"),
            ),
            ops
        )
    }
    
    
    @Test
    fun `int mult by one produces no op`() {
        val a = Field<TestScope, IntCol>(IntCol("a"))
        
        val ops = TestScope().apply { a mult 1 }.ops
        
        assertTrue(ops.isEmpty())
    }
    
    
    @Test
    fun `int mult by zero becomes set to zero`() {
        val a = Field<TestScope, IntCol>(IntCol("a"))
        
        val ops = TestScope().apply { a mult 0 }.ops
        
        assertEquals(listOf(SetOp.Set(a.definition, 0)), ops )
    }
    
    @Test
    fun `bool flip and setAs`() {
        val a = Field<TestScope, BoolCol>(BoolCol("a"))
        val b = Field<TestScope, BoolCol>(BoolCol("b"))
        
        val ops = TestScope().apply {
            a.flip()
            a setAs b
        }.ops
        
        assertEquals(
            listOf(
                SetOp.Flip(a.definition),
                SetOp.SetAs("a", "b")
            ),
            ops
        )
    }
    
    @Test
    fun `int setAs without math`() {
        val a = Field<TestScope, IntCol>(IntCol("a"))
        val b = Field<TestScope, IntCol>(IntCol("b"))
        
        val ops = TestScope().apply { a setAs b }.ops
        
        assertEquals(listOf(SetOp.SetAs("a", "b")), ops)
    }
    
    @Test
    fun `bool setAs`() {
        val a = Field<TestScope, BoolCol>(BoolCol("a"))
        val b = Field<TestScope, BoolCol>(BoolCol("b"))
        
        val ops = TestScope().apply { a setAs b }.ops
        
        assertEquals(listOf(SetOp.SetAs("a", "b")), ops)
    }
    
    @Test
    fun `mixed ops order preserved`() {
        val a = Field<TestScope, IntCol>(IntCol("a"))
        val b = Field<TestScope, IntCol>(IntCol("b"))
        
        val ops = TestScope().apply {
            a setTo 1
            a setAs b
            a add 3
        }.ops
        
        assertEquals(
            listOf(
                SetOp.Set(a.definition, 1),
                SetOp.SetAs("a", "b"),
                SetOp.NumOp("a", 3, "+"),
            ),
            ops
        )
    }
    
    
    
    @Test
    fun `col to col with math setAs produces correct ops`() {
        val a = Field<TestScope, IntCol>(IntCol("a"))
        val b = Field<TestScope, IntCol>(IntCol("b"))
        
        val ops = TestScope().apply {
            a setAs (b andAdd 5)
            a setAs (b andMult 3)
            a setAs (b andCoerceIn 0..10)
            a setAs (b andCapAt 100)
            a setAs (b andFloorAt 50)
        }.ops
        
        assertEquals(
            listOf(
                SetOp.SetAsModified(a.definition.name, IntColOp<TestScope>("b", 5, MathOp.Add)),
                SetOp.SetAsModified(a.definition.name, IntColOp<TestScope>("b", 3, MathOp.Mult)),
                SetOp.SetAsModified(a.definition.name, IntColOp<TestScope>("b", 0, MathOp.CoerceIn(0, 10))),
                SetOp.SetAsModified(a.definition.name, IntColOp<TestScope>("b", 100, MathOp.CapAt(100))),
                SetOp.SetAsModified(a.definition.name, IntColOp<TestScope>("b", 50, MathOp.FloorAt(50))),
            ),
            ops
        )
    }
    
    @Test
    fun `long col to col with math setAs produces correct ops`() {
        val a = Field<TestScope, LongCol>(LongCol("a"))
        val b = Field<TestScope, LongCol>(LongCol("b"))
        
        val ops = TestScope().apply {
            a setAs (b andAdd 5L)
            a setAs (b andMult 3L)
        }.ops
        
        assertEquals(
            listOf(
                SetOp.SetAsModified(a.definition.name, LongColOp<TestScope>("b", 5L, MathOp.Add)),
                SetOp.SetAsModified(a.definition.name, LongColOp<TestScope>("b", 3L, MathOp.Mult)),
            ),
            ops
        )
    }
    
    @Test
    fun `float col to col with math setAs produces correct ops`() {
        val a = Field<TestScope, FloatCol>(FloatCol("a"))
        val b = Field<TestScope, FloatCol>(FloatCol("b"))
        
        val ops = TestScope().apply {
            a setAs (b andAdd 5.5F)
            a setAs (b andDiv 2F)
        }.ops
        
        assertEquals(
            listOf(
                SetOp.SetAsModified(a.definition.name, FloatColOp<TestScope>("b", 5.5F, MathOp.Add)),
                SetOp.SetAsModified(a.definition.name, FloatColOp<TestScope>("b", 2F, MathOp.Div)),
            ),
            ops
        )
    }
    
    
    
    
    
    
    
    
    // All functionality
    
    private val bananas = Field<TestScope, IntCol>(IntCol("bananas"))
    private val unicorns = Field<TestScope, LongCol>(LongCol("unicorns"))
    private val magic = Field<TestScope, FloatCol>(FloatCol("magic"))
    private val chaos = Field<TestScope, BoolCol>(BoolCol("chaos"))

    @Test
    fun `string column is set to cursed artifact`() {
        val mysteryBox = Field<TestScope, StrCol>(StrCol("mystery_box"))
        val b = TestScope().apply {
            mysteryBox setTo "💩42"
        }
        
        assertEquals(
            listOf(SetOp.Set(mysteryBox.definition, "💩42")),
            b.ops
        )
    }
    
    @Test
    fun `chaos is turned OFF to save the universe`() {
        val b = TestScope().apply { OFF(chaos) }
        
        assertEquals(
            listOf(SetOp.Set(chaos.definition, false)),
            b.ops
        )
    }
    
    @Test
    fun `chaos is turned ON because nobody learned anything`() {
        val b = TestScope().apply { ON(chaos) }
        
        assertEquals(
            listOf(SetOp.Set(chaos.definition, true)),
            b.ops
        )
    }
    
    @Test
    fun `bananas are subtracted for diet reasons`() {
        val b = TestScope().apply {
            bananas sub 13
        }

        assertEquals(
            listOf(SetOp.NumOp("bananas", -13, "+")),
            b.ops
        )
    }

    @Test
    fun `unicorn population is reduced dramatically`() {
        val b = TestScope().apply {
            unicorns sub 666L
        }
        
        assertEquals(
            listOf(SetOp.NumOp("unicorns", -666L, "+")),
            b.ops
        )
    }
    
    @Test
    fun `magic is drained slightly but catastrophically`() {
        val b = TestScope().apply {
            magic sub 3.14f
        }
        
        assertEquals(
            listOf(SetOp.NumOp("magic", -3.14f, "+")),
            b.ops
        )
    }
    
    @Test
    fun `unicorns are multiplied beyond safe limits`() {
        val b = TestScope().apply {
            unicorns mult 9001
        }
        
        assertEquals(
            listOf(SetOp.NumOp("unicorns", 9001L, "*")),
            b.ops
        )
    }
    
    @Test
    fun `magic is multiplied until reality glitches`() {
        val b = TestScope().apply {
            magic mult 1.618f
        }
        
        assertEquals(
            listOf(SetOp.NumOp("magic", 1.618f, "*")),
            b.ops
        )
    }
    
    @Test
    fun `bananas become absolute like forced positivity`() {
        val b = TestScope().apply {
            bananas.abs()
        }
        
        assertEquals(
            listOf(SetOp.Abs("bananas")),
            b.ops
        )
    }
    
    @Test
    fun `bananas are forced into acceptable social range`() {
        val b = TestScope().apply {
            bananas coerceIn 1..10
        }
        
        assertEquals(
            listOf(SetOp.CoerceIn("bananas", 1, 10)),
            b.ops
        )
    }
    
    @Test
    fun `unicorns are capped because infinite unicorns are illegal`() {
        val b = TestScope().apply {
            unicorns capAt 9999999L
        }
        
        assertEquals(
            listOf(SetOp.MinMax("unicorns", 9999999L, false)),
            b.ops
        )
    }
    
    @Test
    fun `magic is humbled to minimum survivable amount`() {
        val b = TestScope().apply {
            magic floorAt 0.01f
        }
        
        assertEquals(
            listOf(SetOp.MinMax("magic", 0.01f, true)),
            b.ops
        )
    }
    
    @Test
    fun `unicorns copy values from suspicious source`() {
        val sourceOfTruth = Field<TestScope, LongCol>(LongCol("source_of_truth"))
        val b = TestScope().apply {
            unicorns setAs sourceOfTruth
        }
        
        assertEquals(
            listOf(SetOp.SetAs("unicorns", "source_of_truth")),
            b.ops
        )
    }
    
    @Test
    fun `magic steals power from neighbouring wizard`() {
        val neighbourMagic = Field<TestScope, FloatCol>(FloatCol("neighbour_magic"))
        val b = TestScope().apply {
            magic setAs neighbourMagic
        }
        
        assertEquals(
            listOf(SetOp.SetAs("magic", "neighbour_magic")),
            b.ops
        )
    }
    
    
    
    
    
}
