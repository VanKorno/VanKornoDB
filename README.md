# VanKorno DB

VanKorno DB is a unique system for working with SQLite databases in native Android development (it may become multiplatform in the future). It’s not an ORM, but it can do many of the same things: create and manage db tables based on a data class as a single source of truth, get db rows as objects of that class, add/update rows from objects and much more. 
It is kinda like an ORM, but not really... Instead of forcing a strict 1:1 mapping between entities and database tables, VanKorno DB treats entity classes more like instructions or contracts, rather than fixed table representations.
You can create multiple tables from the same entity class, or use multiple entity classes for the same table.

This enables a very different workflow. For example, instead of storing all TODO items in one huge table, each TODO list can have its own table. When a list is deleted, the entire table is dropped — no need to delete rows one by one.
This approach results in better encapsulation, simpler logic, easier ID management, and often better performance.
It’s similar to organizing files by folders instead of dumping everything into one directory. The database structure better reflects the actual structure of an app.
That approach enables countless possibilities for dynamic generation of tables/columns etc. For example, with traditional ORM you would have to type every single column in its entity definition, but with VanKornoDB you can easily use loops or just lists in entity definitions.


Another key feature is a convenient, modern DSL that lets you do even the craziest nested things with the db, and keep your code readable and pleasant to deal with at the same time. It makes writing queries safer and more convenient, without limiting what you can do with SQLite. It's like Jetpack Compose, but for databases ;)
You can write queries in DSL (even if you're not good at SQL), their parts, db migration instructions, db operation sequences... all in convenient DSL.


VanKorno DB lets you retrieve exactly the data you need — no more, no less, be it a single value, multiple values or the whole objects.
It lets you do more on the SQLite level without sacrificing convenience of the ORM-like mapping.
If you want more control - you can have it. If you want more automation - you can have it as well. You choose the amount of control/automation you want!


### It's still a work in progress and until the 1.0 release, recommended only for people who don't mind experimenting, don't mind everything changing all the time. Until v1.0 I'll be working on it almost as if I'm the only user, so, don't try it at home :)


## Features

- Concise and readable SQL DSL (no raw SQL strings)
- Strongly typed query construction
- Nested queries and joins
- Shared DSL for reading, writing, updating, and deleting
- No annotation processing
- Works with unconventional or highly dynamic schemas
- Can be used with more diverse architecture types than the usual Android ORMs, for example, heavier db-layer logic, etc.
- Built-in database lifecycle and migration system
- Built-in unit test auto-mocking (automatically bypasses database operations and returns defaults during JVM unit tests without needing external mocking libraries)


## Extremely unique features

### Multiple tables per entity (and vice versa)
You can freely map entities to tables in any configuration.
You can dynamically create tables:
```kotlin
for (i in 1..1000) {
    db.createTable(ShopTable + i, ShopSchema)
}
```

### List expansion into columns
List properties are automatically expanded into multiple columns.
```kotlin
data class Shop(
    val someList: List<Int> = List(1000) { 0 }
)
```
This results in 1000 columns generated automatically.

This enables:
Fast filtering and sorting
Zero join overhead
No string parsing or JSON blobs


## Examples

getCursor() - a function that's often used by other functions under the hood. It can also be useful when you want to do some things with the cursor directly:

```kotlin
db.getCursor(SomeTable) { Name equal userName } // Gets cursor from SomeTable where the column 'Name' equals value 'userName' 

db.getCursor(SomeTable, columns(Name, Position), orderByPosition(), limit(10)) {
    // Conditions (where)
    ID notEqual 1
    Order equal 1 // condition clause and value in one place
    Volume greater 1.1F
    or { Time less 1L }
}

```

getEntity() - gets a whole db table row as an object of an entity data class

```kotlin
db.getEntity(_RoundTable) { KnightState notEqual drunk }
```

getEntities() or getEntityMap() - for getting multiple entity objects from a db table.

```kotlin
db.getEntities(_RoundTable) { KnightState less hammered }
```

getInt() (same for other types) - for getting single values from a particular cell.

```kotlin
val aggressionLvl = db.getInt(RoundTable, column = Aggression) { Name equal Arthur }
```


Of course, those one-liner fun examples don't show you what you're able to achieve with VanKornoDB DSL in more complex scenarios...
Here's some of that:

```kotlin
db.getCursor(
    table = Users,
    columns = columns(Name, Address, Phone),
    where = {
        subquery( // subqueries
            table = Posts,
            columns = columns(countAll), // COUNT(*)
            where = {
                (Posts dot user_id)  equal  (Users dot ID)
            }
        ) greater 10
        
        ID notEqual 3
        
        and {  // Nested conditions
            ID notEqual 4
            or {
                ID notEqual 5
                if (someBool) // Custom logic inside
                    ID less someInt
                else
                    ID greater someInt
            }
        }
    },
    orderBy = { // even orderBy has its own DSL that allows you to do some crazy nested things...
        SomeColumn()
        AnotherColumn.flip()
        When(
            orderWhen({ cPosition(); cName.flip() }) { cID equal 1 },
            orderWhen(RANDOM) { cName notEqual "NotBob" },
            Else = { cID() }
        )
    }
)
```

There are multiple setter functions as well. Single value setters, like setInt(), setString(), entity setters, like setEntity(), different convenience functions like addToInt(), flipBool()...

set() - is a universal value setter function that uses its own DSL to modify multiple values, perform different operations, joining them all in a single db query under the hood, optimizing performance.

```kotlin
db.set(RoundKnightTable, whereName(Dudley)) {
    Int1 setTo 5
    Enabled setTo true
    Bool2.flip()
    Int2 add 5
    Long1 mult 3
    Int3.abs()
    Int4 capAt 50
    Int5 floorAt 10
    Float1.coerceIn(0F..1F)
    OneCol setAs AnotherCol
    Int2 setAs (Int1 andAdd 3)
    Int3 setAs (Int2 andCoerceIn 0..10)
    setContentValues(someCV) // don't know who would use ContentValues, when you have this amasing DSL, but still, you can pass CV as well here, along with the other ops
}
```


## Installation
All you need is one dependency in build.gradle.kts (App):
```kotlin
dependencies {
    implementation("com.github.VanKorno:VanKornoDB:VersionNumber")
}
```
For now the lib is published on JitPack for easier updating/dev workflow. When the lib is "done", it'll probably be available on Maven, but until then, make sure you're able to use JitPack libraries in your project:
Add the JitPack repository to your build file settings.gradle.kts
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // <- Enable JitPack libs
    }
}
```


## Migrations
You keep in your files and provide the migration system with the current entity class and the previous classes.
It automatically chooses from which to which class to migrate, and which intermediate steps to take (milestones).
It automatically removes deleted rows and adds new ones. You don't have to state that anywhere.
For things like renaming you'll have to keep a convenient DSL-record, like this example from a real project:

```kotlin
fun migrationsTaskoid() = defineMigrations(
    EntityMeta.Taskoid.currVersion, // EntityMeta is an enum with all your app entities, their versions, etc.
    EntityMeta.Taskoid.currClass
) {
    version(1, V1_Taskoid::class) // Basic minimal data: keeps, which class was used at that version
    version(2, V2_Taskoid::class) // Probably some columns were removed or added here automatically, based on the class differences
    version(3, V3_Taskoid::class) { rename { HideForMins from "hideForMints" to "hideForMins" }} // for renaming you need to keep a record like this
    version(4, V4_Taskoid::class) { rename { TimesCompletedGame from "timesCompleted" to "timesCompletedGame" }}
    version(5, V5_Taskoid::class)
    version(6, V6_Taskoid::class) {
        milestone( // Milestones (migration steps, unskippable migrations, see more info below the code)
            Afterclick modify {
                fromInt = { AfterclickEnum.entries[it].code } // DSL to modify data, not just db itself
            },
            AfterclickOpt modify {
                fromInt = { AfterclickOptEnum.entries[it].code } // Switching from keeping the enum's ordinal to keeping its String code
            },
            Bonus modify {
                finalTransform = { 0 } // Set all Bonuses to 0
            }
        )
    }
    version(7, V7_Taskoid::class) {
        rename { PicDb from "fullPic" to "pic" }
        rename { CountUpIntervalSec from "countUpInterval" to "countUpIntervalSec" }
        rename { CountUpUnpaidSec from "countUpUnpaid" to "countUpUnpaidSec" }
    }
    version(8, V8_Taskoid::class) { rename { Position from "priority" to "position" } }
}
```
Normal migrations get skipped, their differences are handled automatically. There's no need to migrate through each of them. The system is smarter: calculates the differences based on the data you keep, then migrates to the latest class it can.
You define milestones when you want to make the migration non-skippable, it'll become a step in the migration chain, at which you can run more advanced data transformations using the DSL for popular actions or provide your own lambda to modify data the way you want.

Simple type conversions between numeric primitives and Boolean (Int, Long, Float, Double, Boolean) are handled automatically between entity versions without requiring migration DSL configuration. Conversions involving String, ByteArray, or collection element types are not performed automatically and require explicit milestone transformations; otherwise, they fall back to the parameter's default constructor value.


## Lists
As already stated, a unique feature of VanKornoDB is how it handles lists of an entity data class - it expands the elements to individual columns which makes look-up, sorting/filtering operations extremely efficient.
Now you don't have to write every entity column manually, you can just define sets of columns as lists and easily have +500 columns with just one line of code ;)
But there are some rules for lists:
```
1. List parameters must be named with the "List" suffix (e.g., `dayList`).
   Corresponding DB column names must match the base name (e.g., `day`)
   followed by a 1-based index suffix: `day1`, `day2`, ..., etc.

2. Lists must be non-nullable and must have default values with a fixed number of elements
   (used to infer the column count for each list).

3. All list elements must be of the same type, limited to a supported primitive type
   (Boolean, Int, Long, Float, or String).

4. Nested lists or complex types (e.g., data classes) inside lists are NOT SUPPORTED.

5. Lists must be declared at the END of the constructor parameter list, if using the legacy reflection-based mappers.

```



## License
This project is licensed under the Mozilla Public License 2.0.
This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

## Contributing
Contributions, suggestions, and pull requests are welcome!
If you encounter any issues or ideas, feel free to open an issue or discussion.

## Author
Made with ❤️ by [VanKorno](https://github.com/VanKorno)
