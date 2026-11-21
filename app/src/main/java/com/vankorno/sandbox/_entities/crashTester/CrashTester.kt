/* SPDX-License-Identifier: MPL-2.0-AvadaKedavra-protected-code */
package com.vankorno.sandbox._entities.crashTester

import android.os.Parcelable
import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.core.dsl.DslContext
import com.vankorno.vankornodb.core.dsl.ExtraDsl
import com.vankorno.vankornodb.core.dsl.Field
import com.vankorno.vankornodb.core.dsl.OrderDsl
import com.vankorno.vankornodb.core.dsl.WhereDsl
import com.vankorno.vankornodb.core.dsl.withDsl
import com.vankorno.vankornodb.dbManagement.data.*
import com.vankorno.vankornodb.misc.getBoolean
import com.vankorno.vankornodb.set.dsl.SetDsl
import kotlinx.parcelize.Parcelize

/**
 * This shit is for testing both the lib and the code generator, so it's messy by design
 * 
 * data class FusRoDataCalss() {}
 */
@Parcelize
data class CrashTester(
                                  val name: String = "", // like Sir Fartsalot, Barry McKockiner, Lord Wafflebottom, Captain Noodle, Bob McScribbles, Professor Pickle, Hugh Jass, Randy Marshmallow, Count Spatulа, Dingus McGee, Sir Reginald Puddington, Chuckles McSnort, Kevin Baconator, Wiggly McWiggleface, Dr. Stinkypants, Gary Giggles, Fluffy von Fluffington, Morty McButter, Chad Thunderpants

                           override val id: Long = -1L, /* like in "russian warship ID nahuj" */
                              val position: Long = 0L,
                               val enabled: Boolean = false,
                                  val int1: Int = 0,
                                  val int2: Int = 0,
                                  val str1: String = "",
                                  val str2: String = "",
                                 val bool1: Boolean = false,
                                 val bool2: Boolean = false,
                                 val long1: Long = 0L,
                                 val long2: Long = 0L,
                                val float1: Float = 0F,
                                val float2: Float = 0F,
                                val blobBa: ByteArray = byteArrayOf(),

                           val isBullyList: List<Boolean> = List(20) { false },
                               val dayList: List<Int> = List(10) { 0 },
    // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA data class FusRoDataCalss() {}
                              val textList: List<String> = List(3) { "" },
                         val timeStampList: List<Long> = List(5) { 0L },
                              val duckList: List<Float> = List(11) { 0F },
) : DbEntity, Parcelable {
    /*Check this out yo*/
    
    fun thisFunIsHereToStay(why: String = "because") {
        val purpleBurglarAlarm = true
        
        if (purpleBurglarAlarm)
            println("damn!")
    }
    // yo!
}





object _CrashTester : SchemaBundle<CrashTester>(
    clazz = CrashTester::class,

    getter = { cursor ->
        var idx = 0

        CrashTester(
            name = cursor.getString(idx++),
            id = cursor.getLong(idx++),
            position = cursor.getLong(idx++),
            enabled = cursor.getBoolean(idx++),
            int1 = cursor.getInt(idx++),
            int2 = cursor.getInt(idx++),
            str1 = cursor.getString(idx++),
            str2 = cursor.getString(idx++),
            bool1 = cursor.getBoolean(idx++),
            bool2 = cursor.getBoolean(idx++),
            long1 = cursor.getLong(idx++),
            long2 = cursor.getLong(idx++),
            float1 = cursor.getFloat(idx++),
            float2 = cursor.getFloat(idx++),
            blobBa = cursor.getBlob(idx++),
            isBullyList = List(20) { cursor.getBoolean(idx++) },
            dayList = List(10) { cursor.getInt(idx++) },
            textList = List(3) { cursor.getString(idx++) },
            timeStampList = List(5) { cursor.getLong(idx++) },
            duckList = List(11) { cursor.getFloat(idx++) }
        )
    },

    setter = { e, cv ->
        cv.put("name", e.name)
        cv.put("id", e.id)
        cv.put("position", e.position)
        cv.put("enabled", e.enabled)
        cv.put("int1", e.int1)
        cv.put("int2", e.int2)
        cv.put("str1", e.str1)
        cv.put("str2", e.str2)
        cv.put("bool1", e.bool1)
        cv.put("bool2", e.bool2)
        cv.put("long1", e.long1)
        cv.put("long2", e.long2)
        cv.put("float1", e.float1)
        cv.put("float2", e.float2)
        cv.put("blobBa", e.blobBa)
        for (i in 0 until 20) {
            cv.put("isBully${i + 1}", e.isBullyList[i])
        }
        for (i in 0 until 10) {
            cv.put("day${i + 1}", e.dayList[i])
        }
        for (i in 0 until 3) {
            cv.put("text${i + 1}", e.textList[i])
        }
        for (i in 0 until 5) {
            cv.put("timeStamp${i + 1}", e.timeStampList[i])
        }
        for (i in 0 until 11) {
            cv.put("duck${i + 1}", e.duckList[i])
        }
        cv
    },

    withId = { obj, newId -> obj.copy(id = newId) },

    columns = buildColList {
        +CrashTesterFields.Name
        +CrashTesterFields.Id
        +CrashTesterFields.Position
        +CrashTesterFields.Enabled
        +CrashTesterFields.Int1
        +CrashTesterFields.Int2
        +CrashTesterFields.Str1
        +CrashTesterFields.Str2
        +CrashTesterFields.Bool1
        +CrashTesterFields.Bool2
        +CrashTesterFields.Long1
        +CrashTesterFields.Long2
        +CrashTesterFields.Float1
        +CrashTesterFields.Float2
        +CrashTesterFields.BlobBa
        +CrashTesterFields.IsBullyList
        +CrashTesterFields.DayList
        +CrashTesterFields.TextList
        +CrashTesterFields.TimeStampList
        +CrashTesterFields.DuckList
    },
) {
    fun tableNamed(name: String) = (name using this).withDsl(::CrashTesterDsl, ::CrashTesterOrderDsl, ::CrashTesterExtraDsl, ::CrashTesterSetDsl)
}


interface CrashTesterDslColumns {
    val Name: Field<CrashTesterDsl, StrCol>
    val Id: Field<CrashTesterDsl, LongCol>
    val Position: Field<CrashTesterDsl, LongCol>
    val Enabled: Field<CrashTesterDsl, BoolCol>
    val Int1: Field<CrashTesterDsl, IntCol>
    val Int2: Field<CrashTesterDsl, IntCol>
    val Str1: Field<CrashTesterDsl, StrCol>
    val Str2: Field<CrashTesterDsl, StrCol>
    val Bool1: Field<CrashTesterDsl, BoolCol>
    val Bool2: Field<CrashTesterDsl, BoolCol>
    val Long1: Field<CrashTesterDsl, LongCol>
    val Long2: Field<CrashTesterDsl, LongCol>
    val Float1: Field<CrashTesterDsl, FloatCol>
    val Float2: Field<CrashTesterDsl, FloatCol>
    val BlobBa: Field<CrashTesterDsl, BlobCol>
    val IsBullyList: List<Field<CrashTesterDsl, BoolCol>>
    val DayList: List<Field<CrashTesterDsl, IntCol>>
    val TextList: List<Field<CrashTesterDsl, StrCol>>
    val TimeStampList: List<Field<CrashTesterDsl, LongCol>>
    val DuckList: List<Field<CrashTesterDsl, FloatCol>>
}


class CrashTesterOrderDsl internal constructor(columns: CrashTesterDslColumns) : OrderDsl<CrashTesterDsl, CrashTesterOrderDsl>(), CrashTesterDslColumns by columns

class CrashTesterExtraDsl internal constructor(columns: CrashTesterDslColumns) : ExtraDsl<CrashTesterDsl>(), CrashTesterDslColumns by columns

class CrashTesterSetDsl(columns: CrashTesterDslColumns) : SetDsl<CrashTesterDsl>(), CrashTesterDslColumns by columns


class CrashTesterDsl internal constructor(                     context: DslContext<CrashTesterDsl>
) : WhereDsl<CrashTesterDsl>(context), CrashTesterDslColumns {
    override val Name get() = column(CrashTesterFields.Name)
    override val Id get() = column(CrashTesterFields.Id)
    override val Position get() = column(CrashTesterFields.Position)
    override val Enabled get() = column(CrashTesterFields.Enabled)
    override val Int1 get() = column(CrashTesterFields.Int1)
    override val Int2 get() = column(CrashTesterFields.Int2)
    override val Str1 get() = column(CrashTesterFields.Str1)
    override val Str2 get() = column(CrashTesterFields.Str2)
    override val Bool1 get() = column(CrashTesterFields.Bool1)
    override val Bool2 get() = column(CrashTesterFields.Bool2)
    override val Long1 get() = column(CrashTesterFields.Long1)
    override val Long2 get() = column(CrashTesterFields.Long2)
    override val Float1 get() = column(CrashTesterFields.Float1)
    override val Float2 get() = column(CrashTesterFields.Float2)
    override val BlobBa get() = column(CrashTesterFields.BlobBa)
    override val IsBullyList get() = column(CrashTesterFields.IsBullyList)
    override val DayList get() = column(CrashTesterFields.DayList)
    override val TextList get() = column(CrashTesterFields.TextList)
    override val TimeStampList get() = column(CrashTesterFields.TimeStampList)
    override val DuckList get() = column(CrashTesterFields.DuckList)
}

private object CrashTesterFields {
    val Name = sCol("name", "")
    val Id = lCol("id", -1L)
    val Position = lCol("position", 0L)
    val Enabled = bCol("enabled", false)
    val Int1 = iCol("int1", 0)
    val Int2 = iCol("int2", 0)
    val Str1 = sCol("str1", "")
    val Str2 = sCol("str2", "")
    val Bool1 = bCol("bool1", false)
    val Bool2 = bCol("bool2", false)
    val Long1 = lCol("long1", 0L)
    val Long2 = lCol("long2", 0L)
    val Float1 = fCol("float1", 0F)
    val Float2 = fCol("float2", 0F)
    val BlobBa = pCol("blobBa", byteArrayOf())
    val IsBullyList = bListCol("isBully", 20, false)
    val DayList = iListCol("day", 10, 0)
    val TextList = sListCol("text", 3, "")
    val TimeStampList = lListCol("timeStamp", 5, 0L)
    val DuckList = fListCol("duck", 11, 0F)
}
