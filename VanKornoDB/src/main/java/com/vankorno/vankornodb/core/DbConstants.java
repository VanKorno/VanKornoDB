package com.vankorno.vankornodb.core;

public class DbConstants {
    public static final String
InMemoryDB=":memory:",

select="SELECT ",  from=" FROM ",   selectAllFrom="SELECT * FROM ",
dbCreateT="CREATE TABLE ",
dbDrop="DROP TABLE IF EXISTS ",
deleteFrom="DELETE FROM ",

where=" WHERE ",
and=" AND ",   or=" OR ",
comma=", ",    dot=".",

groupBy=" GROUP BY ",   having=" HAVING ",   count="COUNT",   countAll="COUNT(*)",
orderBy=" ORDER BY ",   ascending=" ASC",    descending=" DESC",

like=" LIKE ",   limit=" LIMIT ",   offset=" OFFSET ",

innerJoin=" INNER JOIN ",   leftJoin=" LEFT JOIN ",   crossJoin=" CROSS JOIN ",

ID="ID", RowID="ROWID",
dbAutoID = "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT",
dbInt=" INT NOT NULL",
dbStr=" TEXT NOT NULL",
dbBool=" BOOL NOT NULL",
dbLong=" BIGINT NOT NULL",
dbFloat=" REAL NOT NULL",
dbBlob=" BLOB NOT NULL",

Name="Name"
;
}
