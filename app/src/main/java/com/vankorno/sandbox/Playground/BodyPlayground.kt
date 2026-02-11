package com.vankorno.sandbox.Playground

import androidx.compose.runtime.Composable
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox._navig.ScrDbBrowser
import com.vankorno.sandbox._ui.LargeBtn
import com.vankorno.vankornocompose.composables.Spa_______________cerEndScr
import com.vankorno.vankornocompose.navig.goTo
import com.vankorno.vankornocompose.theme_main.LibAccentColor
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.api.DbRuntime.lops
import com.vankorno.vankornodb.dbManagement.data.using


@Composable
fun BodyPlayground() {
    
    LargeBtn(
        "Create 1000 tables of TestEntity",
        LibAccentColor.Red,
    ) {
        lops.async {
            val tables = Array(1000) {
                (TestTable + it) using _Test
            }
            dbh.createTables(*tables)
            lops.goTo(ScrDbBrowser)
        }
    }
    
    LargeBtn(
        "Delete all app-level tables",
        LibAccentColor.Brown,
    ) {
        lops.async {
            val appTables = dbh.getAppTableNames()
            dbh.deleteTables(appTables)
            lops.goTo(ScrDbBrowser)
        }
    }
    
    
    Spa_______________cerEndScr()
}





