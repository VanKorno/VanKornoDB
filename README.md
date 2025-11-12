# VanKorno DB

VanKorno DB is a lightweight, expressive SQLite query builder and database management system written in Kotlin.
It gives developers more control over database operations than traditional ORMs, letting you retrieve exactly the data you need — no more, no less.
It let's y do more on the SQLite level without sacrificing convenience of the ORM approach.
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


## Examples

getCursor(), getList(), getObj(), getObjects(), getObjMap, getRowCount(), updateObj(), deleteRow(), etc.
Multiple convenient functions that use params with the same DSL-functionality:

```kotlin
db.getCursor(SomeTable, where = { Name equal userName })

db.getCursor(
    table = RoundTable,
    column = Name,
    where = {
        ID notEqual 1
        and { Priority equal 1 } // condition clause and value in one place
        and { Volume greater 1.1F }
        or  { Time less 1L }
    }
)
```


From simple one-liners:
```kotlin
db.getObj(SomeTable, where = { Name equal userName })

db.getObjById(id, SomeTable)
```

...to more complex things, like nested queries:

```kotlin
db.getCursor(
    table = Users,
    columns = arrayOf(Name, Address, Phone),
    where = {
        subquery( // subqueries
            table = Posts,
            columns = arrayOf(countAll), // COUNT(*)
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
)
```

Other examples:

```kotlin
db.createTables(
    TableInfo(TableName,     DataClass::class),
    TableInfo(SettingsTable, SettingsEntity::class),
    TableInfo(TabTable,      TabEntity::class)
)

db.insertRow<SettingsEntity>(SettingsTable, new)
cursor.mapToEntity(SettingsEntity::class)

db.getRowById<UserEntity>(TableName, where = { Mood equal bad })
db.getRows<UserEntity>(TableName, where = { Mood equal good })

db.set("new value", TableName, ColumnName, where = { Mood greater normal })

db.setRowVals(
    TableName,
    where = { Mood notEqual bad },
    Column1 to 1,
    Column2 to 2,
    Column3 to 3
)
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




## License
This project is licensed under the Mozilla Public License 2.0.
This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

## Contributing
Contributions, suggestions, and pull requests are welcome!
If you encounter any issues or ideas, feel free to open an issue or discussion.

## Author
Made with ❤️ by [VanKorno](https://github.com/VanKorno)
