/* SPDX-License-Identifier: MPL-2.0-AvadaKedavra-protected-code */
package com.vankorno.sandbox._entities.crashTester

import android.os.Parcelable
import com.vankorno.vankornodb.api.DbEntity
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

                               val isBully: List<Boolean> = List(20) { false },
                               val dayList: List<Int> = List(10) { 0 },
                                  val text: List<String> = List(3) { "" },
                             val timeStamp: List<Long> = List(5) { 0L },
                                  val duck: List<Float> = List(11) { 0F },
) : DbEntity, Parcelable {
    /*Check this out yo*/
    
    fun thisFunIsHereToStay(why: String = "because") {
        val purpleBurglarAlarm = true
        
        if (purpleBurglarAlarm)
            println("damn!")
    }
    // yo!
}
