package com.vankorno.sandbox._entities.crashTester

import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.sandbox._entities.crashTester.old.to100.to10.V1_CrashTester
import com.vankorno.sandbox._entities.crashTester.old.to100.to10.V2_CrashTester
import com.vankorno.sandbox._entities.crashTester.old.to100.to10.V3_CrashTester
import com.vankorno.vankornodb.api.defineMigrations

fun migrationsCrashTester() = defineMigrations(EntityMeta.CrashTester) {
    version(1, V1_CrashTester::class)
    version(2, V2_CrashTester::class) {
        rename { "alias" to "callsign" }
    }
    version(3, V3_CrashTester::class) {
        rename {
            "callsign" to "name"
            "armed" to "enabled"
            "trace" to "sampleList"
        }
        milestone(
            "sampleList" modify {
                fromString = { trace ->
                    val readings = trace.split('|')
                    List(4) { readings.getOrNull(it)?.trim()?.toIntOrNull() ?: 0 }
                }
            },
            "packet" modify { fromOther = { it as ByteArray } },
        )
    }
    version(4, CrashTester::class) {
        rename { "packet" to "blobBa" }
        milestone("blobBa" modify { fromOther = { it as ByteArray } }) { old, new ->
            val run = old as V3_CrashTester
            (new as CrashTester).copy(
                int1 = run.sampleList.max(),
                dayList = List(10) { run.sampleList.getOrElse(it) { 0 } },
                isBullyList = List(20) { (run.sampleList.getOrNull(it) ?: 0) < 0 },
                textList = run.labelList + run.name,
            )
        }
    }
}
