package com.vankorno.sandbox.dbManagement.table

import com.vankorno.sandbox.BaseAndroidTest
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.sandbox.getLabRats
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.tables.renameTable
import com.vankorno.vankornodb.delete.deleteTables
import com.vankorno.vankornodb.get.getRowCount
import com.vankorno.vankornodb.get.tableExists
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RenameTableTest : BaseAndroidTest() {
    
    @Test
    fun renamesTable() {
        val oldName = "renamesTable_Old"
        val newName = "renamesTable_New"
        dbh.getLabRats(oldName)
        
        dbh.renameTable(oldName, newName)
        
        assertFalse(dbh.tableExists(oldName))
        assertTrue(dbh.tableExists(newName))
        assertTrue(dbh.getRowCount(newName) > 10)
        
        dbh.deleteTables(oldName, newName)
    }
    
    
    @Test
    fun renamesTableWithDslTable() {
        val oldName = "renamesTable_DslOld"
        val newName = "renamesTable_DslNew"
        val oldTable = _TestEntity.tableNamed(oldName)
        dbh.getLabRats(oldName)
        
        dbh.renameTable(oldTable, newName)
        
        assertFalse(dbh.tableExists(oldTable))
        assertTrue(dbh.tableExists(newName))
        assertTrue(dbh.getRowCount(newName) > 10)
        
        dbh.deleteTables(oldName, newName)
    }
    
    
    @Test
    fun ignoresBlankNames() {
        val oldName = "renamesTable_BlankTest"
        dbh.getLabRats(oldName)
        
        dbh.renameTable("", "newName")
        dbh.renameTable("  ", "newName")
        dbh.renameTable(oldName, "")
        dbh.renameTable(oldName, "  ")
        dbh.renameTable("", "")
        
        assertTrue(dbh.tableExists(oldName))
        assertFalse(dbh.tableExists("newName"))
        
        dbh.deleteTables(oldName)
    }
    
    
    @Test
    fun ignoresNonExistingOldTable() {
        val nonExisting = "non_existing_table_xyz"
        val newName = "renamesTable_ShouldNotBeCreated"
        
        dbh.renameTable(nonExisting, newName)
        
        assertFalse(dbh.tableExists(nonExisting))
        assertFalse(dbh.tableExists(newName))
    }
    
    
    @Test
    fun ignoresExistingNewTable() {
        val oldName = "renamesTable_Existing1"
        val newName = "renamesTable_Existing2"
        dbh.getLabRats(oldName)
        dbh.getLabRats(newName)
        
        dbh.renameTable(oldName, newName)
        
        assertTrue(dbh.tableExists(oldName))
        assertTrue(dbh.tableExists(newName))
        
        dbh.deleteTables(oldName, newName)
    }
    
    
    @Test
    fun renamesTableWithBackticks() {
        val oldName = "renamesTable_`BacktickOld`"
        val newName = "renamesTable_`BacktickNew`"
        
        dbh.write { db ->
            val escaped = oldName.replace("`", "``")
            db.execSQL("CREATE TABLE `$escaped` (id INTEGER PRIMARY KEY)")
        }
        
        assertTrue(dbh.tableExists(oldName))
        dbh.renameTable(oldName, newName)
        
        assertFalse(dbh.tableExists(oldName))
        assertTrue(dbh.tableExists(newName))
        
        dbh.deleteTables(oldName, newName)
    }
}
