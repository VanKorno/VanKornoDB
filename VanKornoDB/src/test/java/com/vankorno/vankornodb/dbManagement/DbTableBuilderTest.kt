package com.vankorno.vankornodb.dbManagement

import com.vankorno.vankornodb.core.DbConstants.*
import com.vankorno.vankornodb.dbManagement.data.AutoId
import com.vankorno.vankornodb.dbManagement.data.BlobCol
import com.vankorno.vankornodb.dbManagement.data.BoolCol
import com.vankorno.vankornodb.dbManagement.data.ColumnDef
import com.vankorno.vankornodb.dbManagement.data.FloatCol
import com.vankorno.vankornodb.dbManagement.data.IntCol
import com.vankorno.vankornodb.dbManagement.data.LongCol
import com.vankorno.vankornodb.dbManagement.data.StrCol
import junit.framework.TestCase
import org.junit.Test

class DbTableBuilderTest {
    
    @Test
    fun `buildCreateTableQuery() returns correct beginning`() {
        TestCase.assertEquals(
            "CREATE TABLE $Name (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)",
            DbTableBuilder().newTableQuery(
                Name,
                arrayListOf(ColumnDef(ID, AutoId))
            )
        )
    }
    
    @Test
    fun `buildCreateTableQuery() returns the rest OK`() {
        TestCase.assertEquals(
            "CREATE TABLE $Name (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, $Priority INT NOT NULL, " +
                "$Name TEXT NOT NULL, PitBool BOOL NOT NULL, Live BIGINT NOT NULL, Fleet REAL NOT NULL, Img BLOB NOT NULL)",
            DbTableBuilder().newTableQuery(
                Name,
                arrayListOf(
                    ColumnDef(ID, AutoId),
                    ColumnDef(Priority, IntCol),
                    ColumnDef(Name, StrCol),
                    ColumnDef("PitBool", BoolCol),
                    ColumnDef("Live", LongCol),
                    ColumnDef("Fleet", FloatCol),
                    ColumnDef("Img", BlobCol)
                )
            )
        )
    }
    
    
}