package com.vankorno.vankornodb.dbManagement

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.api.DbLock
import com.vankorno.vankornodb.api.DbRuntime.dbLock
import com.vankorno.vankornodb.dbManagement.data.BaseEntityMeta
import com.vankorno.vankornodb.get.tableExists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DbFunBoilerplateRemover(
                                 context: Context,
                                  dbName: String,
                               dbVersion: Int,
                              entityMeta: Collection<BaseEntityMeta>,
                   createExclusiveTables: Boolean = true,
                                    lock: DbLock = dbLock,
                                onCreate: (SQLiteDatabase)->Unit = {},
                               onUpgrade: (db: SQLiteDatabase, oldVersion: Int)->Unit = { _, _ -> },
) : DbReaderWriter(
    context = context,
    dbName = dbName,
    dbVersion = dbVersion,
    entityMeta = entityMeta,
    createExclusiveTables = createExclusiveTables,
    lock = lock,
    onCreate = onCreate,
    onUpgrade = onUpgrade,
) {
    interface CommonBase {
        val funName: String
    }
    
    interface ReturningBase<T> : CommonBase {
        val defaultVal: T
        val block: (SQLiteDatabase)->T
    }
    
    interface VoidBase : CommonBase {
        val block: (SQLiteDatabase)->Unit
    }
    
    
    
    inner class Writer(                                override val funName: String,
                                                         override val block: (SQLiteDatabase)->Unit,
    ): VoidBase {
        operator fun invoke() {
            write(funName, false, block)
        }
        
        fun async() {
            dbScope.launch {
                write(funName, false, block)
            }
        }
        
        suspend fun susp() = withContext(Dispatchers.IO) {
            write(funName, false, block)
        }
    }
    
    
    inner class Reader<T>(                             override val defaultVal: T,
                                                          override val funName: String,
                                                            override val block: (SQLiteDatabase)->T,
    ): ReturningBase<T> {
        operator fun invoke(): T = read(defaultVal, funName, block)
        
        suspend fun susp(): T = withContext(Dispatchers.IO) {
            read(defaultVal, funName, block)
        }
    }
    
    
    inner class ReadWriter<T>(                         override val defaultVal: T,
                                                          override val funName: String,
                                                            override val block: (SQLiteDatabase)->T,
    ): ReturningBase<T> {
        operator fun invoke(): T = readWrite(defaultVal, funName, block)
        
        suspend fun susp(): T = withContext(Dispatchers.IO) {
            readWrite(defaultVal, funName, block)
        }
    }
    
    
    inner class VoidReader(                            override val funName: String,
                                                         override val block: (SQLiteDatabase)->Unit,
    ): VoidBase {
        operator fun invoke() {
            read(Unit, funName, block)
        }
        
        fun async() {
            dbScope.launch {
                read(Unit, funName, block)
            }
        }
        
        suspend fun susp() = withContext(Dispatchers.IO) {
            read(Unit, funName, block)
        }
    }
    
    
    // ====================== writer ====================== \\
    
    fun writer(funName: String = "write", block: (SQLiteDatabase) -> Unit) =
        Writer(funName) { db -> block(db) }
    
    fun <P1> writer(funName: String = "write", block: (SQLiteDatabase, P1) -> Unit) =
        Writer(funName) { db -> block(db, null as P1) }
    
    fun <P1, P2> writer(funName: String = "write", block: (SQLiteDatabase, P1, P2) -> Unit) =
        Writer(funName) { db -> block(db, null as P1, null as P2) }
    
    fun <P1, P2, P3> writer(funName: String = "write", block: (SQLiteDatabase, P1, P2, P3) -> Unit) =
        Writer(funName) { db -> block(db, null as P1, null as P2, null as P3) }
    
    fun <P1, P2, P3, P4> writer(funName: String = "write", block: (SQLiteDatabase, P1, P2, P3, P4) -> Unit) =
        Writer(funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4) }
    
    fun <P1, P2, P3, P4, P5> writer(funName: String = "write", block: (SQLiteDatabase, P1, P2, P3, P4, P5) -> Unit) =
        Writer(funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5) }
    
    fun <P1, P2, P3, P4, P5, P6> writer(funName: String = "write", block: (SQLiteDatabase, P1, P2, P3, P4, P5, P6) -> Unit) =
        Writer(funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5, null as P6) }
    
    fun <P1, P2, P3, P4, P5, P6, P7> writer(funName: String = "write", block: (SQLiteDatabase, P1, P2, P3, P4, P5, P6, P7) -> Unit) =
        Writer(funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5, null as P6, null as P7) }
    
    
    
    
    // ====================== reader ====================== \\
    
    fun <T> reader(defaultVal: T, funName: String = "read", block: (SQLiteDatabase) -> T) =
        Reader(defaultVal, funName, block)
    
    fun <T, P1> reader(defaultVal: T, funName: String = "read", block: (SQLiteDatabase, P1) -> T) =
        Reader(defaultVal, funName) { db -> block(db, null as P1) }
    
    fun <T, P1, P2> reader(defaultVal: T, funName: String = "read", block: (SQLiteDatabase, P1, P2) -> T) =
        Reader(defaultVal, funName) { db -> block(db, null as P1, null as P2) }
    
    fun <T, P1, P2, P3> reader(defaultVal: T, funName: String = "read", block: (SQLiteDatabase, P1, P2, P3) -> T) =
        Reader(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3) }
    
    fun <T, P1, P2, P3, P4> reader(defaultVal: T, funName: String = "read", block: (SQLiteDatabase, P1, P2, P3, P4) -> T) =
        Reader(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4) }
    
    fun <T, P1, P2, P3, P4, P5> reader(defaultVal: T, funName: String = "read", block: (SQLiteDatabase, P1, P2, P3, P4, P5) -> T) =
        Reader(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5) }
    
    fun <T, P1, P2, P3, P4, P5, P6> reader(defaultVal: T, funName: String = "read", block: (SQLiteDatabase, P1, P2, P3, P4, P5, P6) -> T) =
        Reader(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5, null as P6) }
    
    fun <T, P1, P2, P3, P4, P5, P6, P7> reader(defaultVal: T, funName: String = "read", block: (SQLiteDatabase, P1, P2, P3, P4, P5, P6, P7) -> T) =
        Reader(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5, null as P6, null as P7) }
    
    
    // ====================== readWriter ====================== \\
    
    fun <T> readWriter(defaultVal: T, funName: String = "readWrite", block: (SQLiteDatabase) -> T) =
        ReadWriter(defaultVal, funName, block)
    
    fun <T, P1> readWriter(defaultVal: T, funName: String = "readWrite", block: (SQLiteDatabase, P1) -> T) =
        ReadWriter(defaultVal, funName) { db -> block(db, null as P1) }
    
    fun <T, P1, P2> readWriter(defaultVal: T, funName: String = "readWrite", block: (SQLiteDatabase, P1, P2) -> T) =
        ReadWriter(defaultVal, funName) { db -> block(db, null as P1, null as P2) }
    
    fun <T, P1, P2, P3> readWriter(defaultVal: T, funName: String = "readWrite", block: (SQLiteDatabase, P1, P2, P3) -> T) =
        ReadWriter(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3) }
    
    fun <T, P1, P2, P3, P4> readWriter(defaultVal: T, funName: String = "readWrite", block: (SQLiteDatabase, P1, P2, P3, P4) -> T) =
        ReadWriter(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4) }
    
    fun <T, P1, P2, P3, P4, P5> readWriter(defaultVal: T, funName: String = "readWrite", block: (SQLiteDatabase, P1, P2, P3, P4, P5) -> T) =
        ReadWriter(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5) }
    
    fun <T, P1, P2, P3, P4, P5, P6> readWriter(defaultVal: T, funName: String = "readWrite", block: (SQLiteDatabase, P1, P2, P3, P4, P5, P6) -> T) =
        ReadWriter(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5, null as P6) }
    
    fun <T, P1, P2, P3, P4, P5, P6, P7> readWriter(defaultVal: T, funName: String = "readWrite", block: (SQLiteDatabase, P1, P2, P3, P4, P5, P6, P7) -> T) =
        ReadWriter(defaultVal, funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5, null as P6, null as P7) }
    
    // ====================== voidReader ====================== \\
    
    fun voidReader(funName: String = "exec", block: (SQLiteDatabase) -> Unit) =
        VoidReader(funName, block)
    
    fun <P1> voidReader(funName: String = "exec", block: (SQLiteDatabase, P1) -> Unit) =
        VoidReader(funName) { db -> block(db, null as P1) }
    
    fun <P1, P2> voidReader(funName: String = "exec", block: (SQLiteDatabase, P1, P2) -> Unit) =
        VoidReader(funName) { db -> block(db, null as P1, null as P2) }
    
    fun <P1, P2, P3> voidReader(funName: String = "exec", block: (SQLiteDatabase, P1, P2, P3) -> Unit) =
        VoidReader(funName) { db -> block(db, null as P1, null as P2, null as P3) }
    
    fun <P1, P2, P3, P4> voidReader(funName: String = "exec", block: (SQLiteDatabase, P1, P2, P3, P4) -> Unit) =
        VoidReader(funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4) }
    
    fun <P1, P2, P3, P4, P5> voidReader(funName: String = "exec", block: (SQLiteDatabase, P1, P2, P3, P4, P5) -> Unit) =
        VoidReader(funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5) }
    
    fun <P1, P2, P3, P4, P5, P6> voidReader(funName: String = "exec", block: (SQLiteDatabase, P1, P2, P3, P4, P5, P6) -> Unit) =
        VoidReader(funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5, null as P6) }
    
    fun <P1, P2, P3, P4, P5, P6, P7> voidReader(funName: String = "exec", block: (SQLiteDatabase, P1, P2, P3, P4, P5, P6, P7) -> Unit) =
        VoidReader(funName) { db -> block(db, null as P1, null as P2, null as P3, null as P4, null as P5, null as P6, null as P7) }
    
    
    
    
    
    // Target usage
    
    val myFun = writer() { db -> db.tableExists("lkjlkjlk")}
    
    
    fun otherFun() {
        myFun.async()
    }
    
    
    
}
