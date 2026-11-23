/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.tables

import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.Dbh
import com.vankorno.vankornodb.core.dsl.MiniDslTable
import com.vankorno.vankornodb.misc.wLog


fun Dbh.renameTable(                                                   oldTable: MiniDslTable<*, *>,
                                                                        newName: String,
) = write("renameTable") { db ->
    db.renameTable(oldTable.name, newName)
}
fun SQLiteDatabase.renameTable(                                        oldTable: MiniDslTable<*, *>,
                                                                        newName: String,
) {
    renameTable(oldTable.name, newName)
}


fun Dbh.renameTable(                                                             oldName: String,
                                                                                 newName: String,
) = write("renameTable") { db ->
    db.renameTable(oldName, newName)
}
fun SQLiteDatabase.renameTable(                                                  oldName: String,
                                                                                 newName: String,
) {
    if (oldName.isBlank() && newName.isBlank()) {
        // region LOG
            wLog("renameTable(): Both the new and the old table names are blank. Returning...")
        // endregion
        return //\/\/\/\/\/\
    }
    if (oldName.isBlank()) {
        // region LOG
            wLog("renameTable(): The old table name is blank. Returning...")
        // endregion
        return //\/\/\/\/\/\
    }
    if (newName.isBlank()) {
        // region LOG
            wLog("renameTable(): The new table name is blank. Returning...")
        // endregion
        return //\/\/\/\/\/\
    }
    if (oldName == newName) {
        // region LOG
            wLog("renameTable(): The old and new table names are identical. Returning...")
        // endregion
        return //\/\/\/\/\/\
    }
    val oldExists = DatabaseUtils.longForQuery(
        this,
        "SELECT EXISTS(SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?)",
        arrayOf(oldName),
    ) != 0L
    
    if (!oldExists) {
        // region LOG
            wLog("renameTable(): The old table '$oldName' does not exist. Returning...")
        // endregion
        return //\/\/\/\/\/\
    }
    val newExists = DatabaseUtils.longForQuery(
        this,
        "SELECT EXISTS(SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?)",
        arrayOf(newName),
    ) != 0L
    
    if (newExists) {
        // region LOG
            wLog("renameTable(): The new table '$newName' already exists. Returning...")
        // endregion
        return //\/\/\/\/\/\
    }
    val escapedOldName = oldName.replace("`", "``")
    val escapedNewName = newName.replace("`", "``")
    
    execSQL("ALTER TABLE `$escapedOldName` RENAME TO `$escapedNewName`")
}
