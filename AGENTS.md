Even though it's a public lib, but I'll probably be the only one who contributes to it, so I'll be using my own coding style.


===================================================================================================

# Code style
1. I don't remove spaces on empty lines, and I hate when AI agents do that.
An empty line must have the same indentation as if it wasn't empty.
Example (the first and last lines inside the fun block have the same indentation as the line in the middle, even though they are both empty):
```kotlin
fun myFun() {
    
    someStuff()
    
}
```
A more complex example (again, empty lines have the same indentation as if they weren't empty):
```kotlin

class myClass() {
    
    fun myFun() {
        if (true)
            doSomething()
        
        doAnotherThing()
        
        for (idx in 0..3) {
            doStuff()
            
            if (notBob) {
                val chickenName = "Arnold"
                
                killChicken(chickenName)
            }
        }
    }
    
    
}
```

2. I like trailing commas in multi-line blocks.
```kotlin
arrayOf(
    column1,
    column2,
    column3,
)

fun myFun(                                                                          arg1: Int,
                                                                                    arg2: Long,
                                                                                    arg3: String,
) {
    
}

```

3. Comma after a long Compose Modifier chain is put on its own separate line.
That makes reordering or adding new elements to the chain easier. That's not something for you to fix, that's made intentionally:

```kotlin
row(
    modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 1.dp, horizontal = 2.dp)
        .background(...)
    ,
    horizontalArrangement = arrangement,
    verticalAlignment = Alignment.Top,
) {}
```


4. I use multi-line fun params almost always, if it's not a one-line function.

```kotlin
fun myFun(                                                                           arg: Int
) {
    first(arg)
    second()
}

fun singleLineFun(arg: Int) = otherFun(Int)
```

5. Multi-line fun params are placed in a special way:
 - on the right side of the "page"
 - centered around the colon (all arg colons have the same position on a line)
 - the param with the longest right side (type and defaults) determines how far to the right all params will be placed.
 - the first param is usually on the same line as the fun name (but only if it can easily fit, otherwise it goes on the next line).
 - there are other rules I'm not getting into right here.
 - comments next to params should not affect param placement at all.

```kotlin
fun myFun(                                                                 arg1: Int, /* A comment doesn't affect shit */
                                                                           arg2: Long,
                                                                           arg3: List<List<String>>, // A comment doesn't affect shit
) {
    
}

fun myFun(                                                   arg1: Int,
                 reallyLoooooooooooooooooooooooooooooongParamName: Long,
                                                             arg3: LoooooooooooooooooooongRightSide,
                                               anotherNormalParam: String = "",
) {
    
}
```
AI agents are not required to emulate this param placement style. But they are required not to break placement if it's already in place.
I have an IDE plugin that does the formatting automatically after a fun is made, so AI agents don't need to bother generating functions with my exact style,
but it's better if they at least try to put params on the right side, to make it easier for me to read generated code.





6. Class constructor params are also placed similarly to my fun param style.
Normal classes, put things closer to the right page limiter, data classes put things in the middle

```kotlin
class myClass(                                                                val param1: Int,
                                                                                  param2: String,
) {
    
}

data class myDataClass(
                                val param1: Int = -1,
                                val param2: String,
)
```
Again, AI agents don't have to follow this formatting, but it would be nice if they at least try.


7. Return statements that break the flow of a fun are marked with this exact symbol combo: //\/\/\/\/\/\
Return statements that are all close to each other at the end of the fun don't have to be marked that way,
but statements that do break the flow of a fun, all have to be marked like that.
Sometimes I can use a shorter version "//\/\/\" for less flow-breaking things like the continue-statements in a loop.

```kotlin
fun myFun() {
    if (someShit) return //\/\/\/\/\/\
    
    doSomething()
    
    if (otherShit) return //\/\/\/\/\/\
    
    for (idx in 0..10) {
        if (idx == 3)
            continue //\/\/\
        doStuff()
    }
}
```


8. Log statements.
I use my own logger with functions:
```kotlin
fun dLog(                                                                            tag: String,
                                                                                     msg: String,
) {...}

fun wLog(                                                                            tag: String,
                                                                                     msg: String,
) {...}

fun eLog(                                                                    tag: String,
                                                                             msg: String,
                                                                       throwable: Throwable? = null,
) {...}
```
The logger handles any context, including unit-tests, automatically.

I put my log statements inside custom regions. That makes code more readable, because my IDE automatically folds them.
```kotlin
// region LOG
    dLog(TAG, "My message")
// endregion
```


9. Preferred continuation indent: 4 (not 8) spaces
```kotlin
Modifier
    .fillMaxSize()
    .background(...)
```







===================================================================================================

# Tests
For unit-tests (not android tests) I prefer fun names to be formatted like this:
```kotlin
fun `Fun names are written with spaces and shit like that`() {...}
```
Function calls like assertEquals() should be called directly, without the class name:
assertEquals()
not Assert.assertEquals()

with this import:
import org.junit.Assert.*



===================================================================================================

# Constants/vals I'm using

DB Constants:
```Java
package com.vankorno.vankornodb.core.data;

public class DbConstants {
    public static final String
        InMemoryDB = ":memory:",
        DbTAG = "VanKornoDB",
        _ID = "id", // Reserved name for int IDs. Entity data classes should have this exact param name.
        RowID = "ROWID",
        _Name = "name",
        _Position = "position",
        _Type = "type",
        DbTypeTable = "table",
        
        TABLE_EntityVersions = "EntityVersions",
        EntityVersion = "version",
        TABLE_AndroidMetadata = "android_metadata",
        TABLE_Master = "sqlite_master",
        
        SELECT = "SELECT ",  FROM = " FROM ",   SELECT_ALL_FROM = "SELECT * FROM ",
        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ",
        DEFAULT = " DEFAULT ",
        WHERE = " WHERE ",
        and = " AND ",   or = " OR ",
        comma = ", ",
        countAll = "COUNT(*)",
        ORDER_BY = " ORDER BY ",   ASCENDING = " ASC",    DESCENDING = " DESC",
        LIKE = " LIKE ",        NOT_LIKE = " NOT LIKE ",
        IN = " IN ",            NOT_IN = " NOT IN ",
        LIMIT = " LIMIT ",
        OFFSET = " OFFSET ",
        RANDOM = "RANDOM()"
    ;
}
```

