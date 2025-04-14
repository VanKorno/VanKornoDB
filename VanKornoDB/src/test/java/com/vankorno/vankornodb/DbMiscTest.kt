package com.vankorno.vankornodb

import com.vankorno.vankornodb.core.DbConstants.*
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DbMiscTest {
    
    @Test
    fun `buildCreateTableQuery() returns correct beginning`() {
        assertEquals(
            "CREATE TABLE $Name (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)",
            DbMisc().buildCreateTableQuery(Name, arrayListOf(arrayOf(ID, dbAutoID)))
        )
    }
    
    @Test
    fun `buildCreateTableQuery() returns the rest OK`() {
        assertEquals(
            "CREATE TABLE $Name (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, $Priority INT NOT NULL, " +
            "$Name TEXT NOT NULL, PitBool BOOL NOT NULL, Live BIGINT NOT NULL, Fleet REAL NOT NULL, Img BLOB NOT NULL)",
            DbMisc().buildCreateTableQuery(
                Name,
                arrayListOf(
                    arrayOf(ID, dbAutoID),
                    arrayOf(Priority, dbInt),
                    arrayOf(Name, dbStr),
                    arrayOf("PitBool", dbBool),
                    arrayOf("Live", dbLong),
                    arrayOf("Fleet", dbFloat),
                    arrayOf("Img", dbBlob)
                )
            )
        )
    }
    
    
}