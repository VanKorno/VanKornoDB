package com.vankorno.sandbox.set

import androidx.test.filters.MediumTest
import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.entities.testEntity.OrmTest
import org.junit.Test

private const val RoundKnightTable = "RoundKnightTable"

@MediumTest
class ReorderTest : BaseAndroidTest() {
    
    @Test
    fun testReorder() {
        prep()
    }
    
    private fun prep() {
        dbh.createTable(RoundKnightTable, OrmTest)
        
        
    }
    
}