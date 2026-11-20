package com.vankorno.sandbox.Playground

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vankorno.sandbox._entities.testEntity.TestEntity
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.vankornodb.add.addEntities
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.get.getEntities
import com.vankorno.vankornodb.get.isTableEmpty
import com.vankorno.vankornodb.newTable.createTable
import com.vankorno.vankornodb.set.addToInt
import com.vankorno.vankornodb.set.setBool
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

private val Holes = _Test.tableNamed("PlaygroundWhacAMole")
private val gameLock = Mutex()
private const val Rounds = 12

@Composable
internal fun WhacAMole() {
    var playing by remember { mutableStateOf(false) }
    var round by remember { mutableIntStateOf(0) }
    var holes by remember { mutableStateOf(emptyList<TestEntity>()) }
    var hit by remember { mutableStateOf<CompletableDeferred<Unit>?>(null) }
    
    val runGame = playing
    LaunchedEffect(runGame) {
        gameLock.withLock {
            try {
                holes = withContext(Dispatchers.IO) {
                    dbh.readWrite(emptyList(), "loadMoles") { db ->
                        db.createTable(Holes)
                        if (db.isTableEmpty(Holes))
                            db.addEntities(Holes, (1..9).map { TestEntity(name = "Hole $it") })
                        db.setBool(false, Holes.col { Enabled })
                        db.getEntities(Holes, orderBy = { Id() })
                    }
                }
                if (runGame && holes.size == 9) {
                    var previous = -1L
                    for (turn in 1..Rounds) {
                        val target = holes.filter { it.id != previous }.random().id
                        previous = target
                        holes = withContext(Dispatchers.IO) {
                            dbh.readWrite(emptyList(), "raiseMole") { db ->
                                db.setBool(true, Holes.col { Enabled }) { Id equal target }
                                db.getEntities(Holes, orderBy = { Id() })
                            }
                        }
                        if (holes.isEmpty()) break
                        round = turn
                        val tap = CompletableDeferred<Unit>()
                        hit = tap
                        val scored = withTimeoutOrNull(1000L) { tap.await(); true } == true
                        hit = null
                        holes = withContext(Dispatchers.IO) {
                            dbh.readWrite(emptyList(), "lowerMole") { db ->
                                if (scored)
                                    db.addToInt(1, Holes.col { Int1 }) { Id equal target; Enabled equal true }
                                db.setBool(false, Holes.col { Enabled }) { Id equal target }
                                db.getEntities(Holes, orderBy = { Id() })
                            }
                        }
                        if (holes.isEmpty()) break
                    }
                }
            } finally {
                hit = null
                if (runGame) withContext(NonCancellable + Dispatchers.IO) {
                    dbh.setBool(false, Holes.col { Enabled }) {}
                }
                if (runGame) playing = false
            }
        }
    }
    
    Card(Modifier.fillMaxWidth().padding(12.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Whac-A-Mole", style = MaterialTheme.typography.titleLarge)
            Text(if (playing) "Round $round / $Rounds" else "Tap the mole before it moves. Each square keeps its hits.")
            for (row in holes.chunked(3)) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (hole in row) key(hole.id) {
                        val roundHit = hit
                        Button(
                            onClick = { roundHit?.complete(Unit) },
                            enabled = playing && hole.enabled && roundHit != null,
                            modifier = Modifier.weight(1f).height(54.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                        ) { Text(if (hole.enabled && playing) "Mole!  ${hole.int1}" else hole.int1.toString()) }
                    }
                }
            }
            if (!playing) TextButton(onClick = { round = 0; playing = true }) {
                Text(if (round == 0) "Play 12 rounds" else "Play again")
            }
        }
    }
}
