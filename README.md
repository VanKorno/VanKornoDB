# VanKorno DB

VanKorno DB is a lightweight, expressive SQLite query builder and database management system written in Kotlin.
It gives developers more control over database operations than traditional ORMs, letting you retrieve exactly the data you need — no more, no less.
It lets you do more on the SQLite level without sacrificing convenience of the ORM approach.
Use the full power of SQLite in a safe and convenient way!

### The main idea is this:
If you want more control - you can have it. If you want more automation - you can have it as well. You choose the amount of control/automation you want!

Designed primarily for Android, but with potential to support other platforms in the future.


### It's still a work in progress and until the 1.0 release, recommended only for people who don't mind experimenting, don't mind everything changing all the time. Until v1.0 I'll be working on it almost as if I'm the only user, so, don't try it at home :)


## Features

- Concise and readable SQL query builder (build queries with DSL and constants — no hardcoded strings)
- Structured and intuitive query DSL (supports nested subqueries, joins, etc.)
- Safe query construction without raw SQL
- Same familiar param sets with the same DSL shared by most functions. Understand it once - and use it to get, set, create, delete everything. Although there are some alternative functions with even simpler param sets for convenience there as well.
- Declarative condition DSL with clean separation of conditions and arguments, and the ability to see them in one place, side-by-side
- Zero annotation processing
- Can be used with more diverse architecture types than the usual Android ORMs.
- Full db management system, working out of the box.
- Convenient, efficient and reliable multi-step migration system with its own DSL that minimizes boilerplate code.
- Designed for full control over your database logic.


## Extremely unique features

- Entities are not limited to single tables. You can generate multiple db tables from the same entity and vice versa (use multiple different entities with the same db table).
- Dynamic table creation.
```kotlin
for (i in 1..1000) { db.createTable(ShopTable + i, SbShop) }
```

- List params of an entity class are automatically expanded, mapping the list elements to individual columns, which enables more logic to be done at the SQL-query level, without parsing strings or multiple connected tables.
For example, with VanKornoDB you can even generate a thousand of tables with a thousand of columns via 2 lines of code:
```kotlin
data class Shop(val someList: List<Int> = List(1000) { 0 } ) : CurrEntity  // all those list elements become individual columns

for (i in 1..1000) { db.createTable(ShopTable + i, Shop::class) }
```


## Examples

getCursor() - a function that's often used by other unctions under the hood. It can also be useful when you want do do some things with the cursor directly:

```kotlin
db.getCursor(SomeTable) { Name equal userName } // Gets cursor from SomeTable where the column 'Name' equals to value 'userName' 

db.getCursorPro(SomeTable, columns(Name, Position)) { // If simple 'where' DSL isn't enough, you can use extended DSL (functions that use it have 'Pro' suffix)
    where {
        ID notEqual 1
        and { Order equal 1 } // condition clause and value in one place
        and { Volume greater 1.1F }
        or  { Time less 1L }
    }
    orderByPosition()
    limit = 10
}
```

getObj() - gets the whole db table row as an object of the entity data class

```kotlin
db.getObj(table = RoundTable, schemaBundle = SbKnight) { KnightState notEqual drunk }
```

getObjects() or getObjMap() - for getting multiple objects from a db table.

```kotlin
db.getObjects(RoundTable, SbKnight) { KnightState less hammered }
```

getInt() (same for other types) - for getting single values from a particular cell.

```kotlin
val aggressionLvl = db.getInt(RoundTable, column = Aggression) { Name equal Arthur }
```


Of course, those one-liner fun examples don't show you what you're able to achieve with VanKornoDB DSL in more complex scenarios...
Here's some of that:

```kotlin
db.getCursorPro(Users, columns(Name, Address, Phone)) {
    where {
        subquery( // subqueries
            table = Posts,
            columns = columns(countAll), // COUNT(*)
            where = {
                (Posts dot user_id)  equal  (Users dot ID)
            }
        ) greater 10
        
        and { ID notEqual 3 }
        
        andGroup {  // Nested conditions
            ID notEqual 4
            orGroup {
                ID notEqual 5
                if (someBool) // Custom logic inside
                    ID less someInt
                else
                    ID greater someInt
            }
        }
    }
    
    orderBy { // even orderBy has its own DSL that allows you to do some crazy nested things...
        SomeColumn()
        AnotherColumn.flip()
        When(
            orderWhen({ cPosition(); cName.flip() }) { cID equal 1 },
            orderWhen(RANDOM) { cName notEqual "NotBob" },
            Else = { cID() }
        )
    }
}
```

There are multiple setter functions as well. Single value setters, like setInt(), setString(), object setters, like setObj(), different convenience functions like addToInt(), flipBool()...

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

And make sure you have a setup that lets you use JitPack libraries:
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

## R8
If you're using the R8 optimization, put this into your app's proguard-rules.pro:
```
# Keep all classes implementing DbEntity and their primary constructors
-keep class * implements com.vankorno.vankornodb.api.DbEntity {
    <init>(...);
}

# Keep all fields and methods (needed for defaultInstanceValueOf and reflection)
-keepclassmembers class * implements com.vankorno.vankornodb.api.DbEntity {
    <fields>;
    <methods>;
}

# Keep constructor parameter names (needed for reflection-based mapping)
-keepattributes RuntimeVisibleParameterAnnotations,ParameterNames

```
It is needed because VanKornoDB maps db column name directly to a class param name and we don't want
R8 to shrink those names and break the mapping.



## Migrations
You keep in your files and provide the migration system with the current entity class and the previous classes.
It automatically chooses from which to which class to migrate. , and which intermediate steps to do (milestones).
It automatically removes deleted rows and adds new ones. You don't have to state that anywhere.
For things like renaming you'll have to keep the a convenient DSL-record, like this example from a real project:

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
Normal migrations get skipped, their differences are handled automatically. There's no need to migrate through each of them. The system is more smart: calculates the differences based on the data you keep, then migrates to the latest class it can.
You define milestones when you want to make the migration non-skippable, it'll became a step in the migration chain, at which you can run more advanced data transformations using the DSL for popular actions or provide your own lambda to modify data the way you want.


## Lists
Another unique feature of VanKornoDB is how it handles lists of the entity data class - it expands the elements to individual columns which makes look-up, sorting/filtering operations extremely efficient.
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
