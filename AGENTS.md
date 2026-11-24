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
Also stop removing damn empty lines in general if you didn't put them there, agents!



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


4. I have my own way of formatting multi-line fun/class params (centered around the colon and placed further to the right).
My IDE plugin does the aligning automatically. AI don't have to try to emulate that style.



5. Return statements that break the flow of a fun are marked with this exact symbol combo: //\/\/\/\/\/\
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


6. Log statements.
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



7. Preferred continuation indent: 4 (not 8) spaces
```kotlin
Modifier
    .fillMaxSize()
    .background(...)
```



8. Space between fun
In general I like to have more space between functions.
Space between a wrapper-fun and a base fun can be 0-1 lines.
Overloads - 1-2 lines.
Normal fun - 2 and more.

Don't remove spaces anywhere in the file unless you created that space yourself!
And don't add more space than reasonably needed (if I need more space - I'll add it myself)








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



