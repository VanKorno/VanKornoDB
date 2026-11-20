package com.vankorno.sandbox.Playground

import androidx.compose.runtime.Composable
import com.vankorno.sandbox._entities.TestTableName
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox._navig.Navigator.goToScreen
import com.vankorno.sandbox._navig.ScrDbBrowser
import com.vankorno.sandbox._ui.composables.LargeBtn
import com.vankorno.sandbox._ui.composables.Spa_______________cer
import com.vankorno.sandbox._ui.theme.AccentColor
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.delete.deleteTables
import com.vankorno.vankornodb.get.getAppTableNames
import com.vankorno.vankornodb.newTable.createTables


@Composable
fun BodyPlayground() {
    
    LargeBtn(
        "Create 1000 tables of TestEntity",
        AccentColor.Red,
    ) {
        val tables = Array(1000) {
            _Test.tableNamed(TestTableName + it)
        }
        dbh.createTables(*tables)
        goToScreen(ScrDbBrowser)
    }
    
    LargeBtn(
        "Delete all app-level tables",
        AccentColor.Brown,
    ) {
        val appTables = dbh.getAppTableNames().toTypedArray()
        dbh.deleteTables(*appTables)
        goToScreen(ScrDbBrowser)
    }
    
    
    Spa_______________cer(100)
}





