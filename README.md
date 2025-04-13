# VanKorno DB

VanKorno DB is a lightweight, expressive SQLite query builder and database management system written in Kotlin.  
It gives developers more control over database operations than traditional ORMs, letting you retrieve exactly the data you need — no more, no less.  
Use the full power of SQLite in a safe and convenient way!  
Designed primarily for Android, but with potential to support other platforms in the future.

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
val cursor = db.query(
    table = Users,
    columns = arrayOf(ColumnName),
    where = {
        innerQuery(
            table = Posts,
            columns = arrayOf(countAll), // COUNT(*)
            where = {
                Posts dot user_id  equal  Users dot id
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