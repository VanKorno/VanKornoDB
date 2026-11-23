package com.vankorno.sandbox.Playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vankorno.sandbox._entities.TestTableName
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._TestEntity
import com.vankorno.sandbox._navig.Navigator.goToScreen
import com.vankorno.sandbox._navig.ScrDbBrowser
import com.vankorno.sandbox._ui.composables.LargeBtn
import com.vankorno.sandbox._ui.composables.Spa_______________cer
import com.vankorno.sandbox._ui.theme.AccentColor
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.delete.deleteTables
import com.vankorno.vankornodb.get.getAppTableNames
import com.vankorno.vankornodb.get.getEntities
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.newTable.createTable
import com.vankorno.vankornodb.newTable.createTables
import com.vankorno.vankornodb.set.addToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun BodyPlayground() {
    
    LargeBtn(
        "Create 1000 tables of TestEntity",
        AccentColor.Red,
    ) {
        val tables = Array(1000) {
            _TestEntity.tableNamed(TestTableName + it)
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
    
    SnackPoll()
    WhacAMole()
    
    
    Spa_______________cer(100)
}


private val Snacks = _TestEntity.tableNamed("PlaygroundSnackPoll")

private suspend fun loadSnackPoll(                                           voteFor: Long? = null
): List<TestEntity>? = withContext(Dispatchers.IO) {
    dbh.createTable(Snacks)
    if (dbh.isTableEmpty(Snacks))
        dbh.addEntities(Snacks, listOf("Popcorn", "Pretzels", "Nachos").map { TestEntity(name = it) })
    
    if (voteFor != null)
        dbh.addToInt(1, Snacks.col { Int1 }) { Id equal voteFor }
    
    dbh.getEntities(Snacks, orderBy = { Int1.flip(); Id() })
}


@Composable
private fun SnackPoll() {
    var rows by remember { mutableStateOf<List<TestEntity>?>(null) }
    var busy by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    
    suspend fun refresh(                                                          id: Long? = null
    ) {
        try {
            rows = loadSnackPoll(id)
        } finally {
            busy = false
        }
    }
    
    LaunchedEffect(Unit) { refresh() }
    
    Card(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Movie night snack poll", style = MaterialTheme.typography.titleLarge)
            val snacks = rows
            if (snacks == null) {
                if (busy) {
                    Text("Loading votes…")
                } else {
                    Text("Couldn’t load the poll.")
                    TextButton(onClick = {
                        busy = true
                        scope.launch { refresh() }
                    }) { Text("Retry") }
                }
            } else {
                val total = snacks.sumOf { it.int1 }
                Text("Total votes: $total")
                for (snack in snacks) key(snack.id) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f).padding(end = 12.dp)) {
                            Text("${snack.name} · ${snack.int1}")
                            LinearProgressIndicator(
                                progress = { snack.int1.toFloat() / total.coerceAtLeast(1) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        TextButton(enabled = !busy, onClick = {
                            busy = true
                            scope.launch { refresh(snack.id) }
                        }) { Text("Vote") }
                    }
                }
            }
        }
    }
}
