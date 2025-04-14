# VanKorno DB

VanKorno DB is a lightweight, expressive SQLite query builder and database management system written in Kotlin.  
It gives developers more control over database operations than traditional ORMs, letting you retrieve exactly the data you need — no more, no less.  
Use the full power of SQLite in a safe and convenient way!  
Designed primarily for Android, but with potential to support other platforms in the future.

### It's still a work in progress and before the 1.0 release, recommended only for people who don't mind experimenting.


## Features

- Concise and readable SQL query builder (build queries with constants and DSL exclusively — no hardcoded strings)
- Structured and intuitive query DSL (supports nested subqueries, joins, etc.)
- Safe query construction without raw SQL
- Declarative condition DSL with clean separation of conditions and arguments, and the ability to see them in one place, side-by-side
- Zero annotation processing, no reflection
- Customizable and framework-agnostic
- Designed for full control over your database logic


## Example

```kotlin
val cursor = db.query(SomeTable, where = { Name equal userName })

val cursor = db.query(
    table = RoundTable,
    column = Name,
    where = {
        ID notEqual 1
        and { Priority equal 1 }
        and { Volume greater 1.1F }
        or  { Time less 1L }
    }
)

// Or just a bit more fun and safe (than the usual queries) manual building with constants:
selectAllFrom + SmallTable + orderBy + Size


// Or more advanced stuff, with nested "queries":

val cursor = db.query(
    table = Users,
    columns = arrayOf(Name, Address, Phone),
    where = {
        innerQuery(
            table = Posts,
            columns = arrayOf(countAll), // COUNT(*)
            where = {
                (Posts dot user_id)  equal  (Users dot ID)
            }
        ) greater 10
    }
)
```

## Installation
Coming soon...
(standard JitPack stuff)

## License
This project is licensed under the Mozilla Public License 2.0.
This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

## Contributing
Contributions, suggestions, and pull requests are welcome!
If you encounter any issues or ideas, feel free to open an issue or discussion.

## Author
Made with ❤️ by [VanKorno](https://github.com/VanKorno)