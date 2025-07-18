# VanKorno DB

VanKorno DB is a lightweight, expressive SQLite query builder and database management system written in Kotlin.  
It gives developers more control over database operations than traditional ORMs, letting you retrieve exactly the data you need — no more, no less.
Use the full power of SQLite in a safe and convenient way!
The main idea is this: If you want more control - you can have it. If you want more automation - you can have it as well. You choose the amount of control/automation you want!
Designed primarily for Android, but with potential to support other platforms in the future.

### It's still a work in progress and before the 1.0 release, recommended only for people who don't mind experimenting, don't mind everything changing all the time, without a lot of explanations, etc.
### Until v1.0 I'll be working on it almost as if I'm the only user (which will probably be true for a long time anyway). So, don't try it at home :)


## Features

- Concise and readable SQL query builder (build queries with constants and DSL exclusively — no hardcoded strings)
- Structured and intuitive query DSL (supports nested subqueries, joins, etc.)
- Safe query construction without raw SQL
- Declarative condition DSL with clean separation of conditions and arguments, and the ability to see them in one place, side-by-side
- Zero annotation processing
- Customizable and framework-agnostic
- Designed for full control over your database logic


## Examples

getCursor(), getList(), getRow(), getRows(), getRowMap, getRowCount(), updateRow(), deleteRow(), etc.
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
db.getRow(SomeTable, where = { Name equal userName })

db.getRowById(id, SomeTable)
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

db.setMult(
    TableName,
    values = mapOf( // or pass ContentValues
        Column1 to 1,
        Column2 to 2,
        Column3 to 3
    ),
    where = { Mood notEqual bad }
)

```

## Installation
(standard JitPack stuff)
Add the JitPack repository to your build file settings.gradle.kts
```kotlin
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven { url = uri("https://jitpack.io") }
	}
}
```

Add the dependency in build.gradle.kts (App):
```kotlin
dependencies {
    implementation("com.github.VanKorno:VanKornoDB:VersionNumber")
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