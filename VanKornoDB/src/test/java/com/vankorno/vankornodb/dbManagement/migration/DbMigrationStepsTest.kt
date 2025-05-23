package com.vankorno.vankornodb.dbManagement.migration

import com.vankorno.vankornodb.dbManagement.migration.MigrationUtils.getMigrationSteps
import org.junit.Assert.assertEquals
import org.junit.Test

class DbMigrationStepsTest {
    
    @Test
    fun alreadyAtTarget() {
        var result = getMigrationSteps(1, 1)
        assertEquals(emptyList<Int>(), result)
        
        result = getMigrationSteps(100, 100)
        assertEquals(emptyList<Int>(), result)
        
        result = getMigrationSteps(777, 777)
        assertEquals(emptyList<Int>(), result)
    }
    
    @Test
    fun fromNonTennerToCloseTarget1() {
        val result = getMigrationSteps(2, 7)
        assertEquals(listOf(7), result)
    }
    
    @Test
    fun fromNonTennerToCloseTarget2() {
        val result = getMigrationSteps(13, 18)
        assertEquals(listOf(18), result)
    }
    
    @Test
    fun fromTennerToCloseTarget() {
        val result = getMigrationSteps(10, 18)
        assertEquals(listOf(18), result)
    }
    
    @Test
    fun throughTiers() {
        val result = getMigrationSteps(12, 112)
        assertEquals(listOf(20, 100, 110, 112), result)
    }
    
    @Test
    fun tennerToTennerDirectStep() {
        val result = getMigrationSteps(100, 500)
        assertEquals(listOf(500), result)
    }
    
    @Test
    fun bigJumpSkippingTiers() {
        val result = getMigrationSteps(97, 1234)
        assertEquals(listOf(100, 1000, 1230, 1234), result)
    }
    
    @Test
    fun skipRedundantTenner() {
        val result = getMigrationSteps(1100, 1234)
        assertEquals(listOf(1230, 1234), result)
    }
    
    @Test
    fun directJumpWhenTargetIsTenner() {
        val result = getMigrationSteps(83, 90)
        assertEquals(listOf(90), result)
    }
    
    @Test
    fun largeTierClimb() {
        val result = getMigrationSteps(1, 10500)
        assertEquals(
            listOf(10, 100, 1000, 10000, 10500),
            result
        )
    }
    
    @Test
    fun fromTennerToTierBoundary() {
        val result = getMigrationSteps(100, 1000)
        assertEquals(listOf(1000), result)
    }
    
    @Test
    fun crossAllTiersFromLow() {
        val result = getMigrationSteps(5, 100_001)
        assertEquals(
            listOf(10, 100, 1000, 10_000, 100_000, 100_001),
            result
        )
    }
    
    @Test
    fun justBelowTier() {
        val result = getMigrationSteps(999, 1001)
        assertEquals(listOf(1000, 1001), result)
    }
    
    @Test
    fun climbToExactlyTier() {
        val result = getMigrationSteps(999, 10_000)
        assertEquals(listOf(1000, 10_000), result)
    }
    
    @Test
    fun climbOneStepBeforeTier() {
        val result = getMigrationSteps(998, 999)
        assertEquals(listOf(999), result)
    }
    
    @Test
    fun jumpOverSeveralTiersAgain() {
        val result = getMigrationSteps(95, 55_555)
        assertEquals(
            listOf(100, 1000, 10_000, 55_550, 55_555),
            result
        )
    }
    
    
    
}