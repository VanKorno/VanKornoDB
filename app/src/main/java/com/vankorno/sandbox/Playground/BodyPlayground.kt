package com.vankorno.sandbox.Playground

import androidx.compose.runtime.Composable
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox._navig.Navigator.goToScreen
import com.vankorno.sandbox._navig.ScrDbBrowser
import com.vankorno.sandbox._ui.composables.LargeBtn
import com.vankorno.sandbox._ui.composables.Spa_______________cer
import com.vankorno.sandbox._ui.theme.AccentColor
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using


@Composable
fun BodyPlayground() {
    
    LargeBtn(
        "Create 1000 tables of TestEntity",
        AccentColor.Red,
    ) {
        val tables = Array(1000) {
            (TestTable + it) using _Test
        }
        dbh.createTables(*tables)
        goToScreen(ScrDbBrowser)
    }
    
    LargeBtn(
        "Delete all app-level tables",
        AccentColor.Brown,
    ) {
        val appTables = dbh.getAppTableNames()
        dbh.deleteTables(appTables)
        goToScreen(ScrDbBrowser)
    }
    
    
    Spa_______________cer(100)
}





