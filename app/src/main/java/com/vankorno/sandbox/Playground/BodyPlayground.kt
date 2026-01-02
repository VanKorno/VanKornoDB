package com.vankorno.sandbox.Playground

import androidx.compose.runtime.Composable
import com.vankorno.sandbox.AppStart.DemoApp.Companion.dbh
import com.vankorno.sandbox._entities.TestTable
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox._navig.ScrDbBrowser
import com.vankorno.sandbox._navig.goTo
import com.vankorno.sandbox._ui.LargeBtn
import com.vankorno.vankornocompose.composables.Spa_______________cerEndScr
import com.vankorno.vankornocompose.theme_main.LibAccentColor
import com.vankorno.vankornodb.dbManagement.data.using


@Composable
fun BodyPlayground() {
    
    LargeBtn(
        "Create 1k tables of TestEntity",
        LibAccentColor.Red,
    ) {
        val tables = Array(1000) {
            (TestTable + it) using _Test
        }
        dbh.createTables(*tables, async = true)
        dbh.goTo(ScrDbBrowser)
    }
    
    /*LargeBtn(
        "Delete all TestTables",
        LibAccentColor.Orange,
    ) {
        
    }*/
    
    
    Spa_______________cerEndScr()
}





