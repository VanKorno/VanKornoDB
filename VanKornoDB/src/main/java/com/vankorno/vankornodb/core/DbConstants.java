package com.vankorno.vankornodb.core;

public class DbConstants {
    public static final String

InMemoryDB=":memory:", DbTAG = "VanKornoDB",

select="SELECT ",  from=" FROM ",   selectAllFrom="SELECT * FROM ",
dbCreateT = "CREATE TABLE IF NOT EXISTS ",
dbDefault = " DEFAULT ",
dbDrop = "DROP TABLE IF EXISTS ",
deleteFrom = "DELETE FROM ",

TABLE_AndroidMetadata = "android_metadata",
TABLE_Master = "sqlite_master",
SQL = "sql", TBL_Name = "tbl_name", Rootpage = "rootpage", // columns in the master table (in addition to Name and Type)


where=" WHERE ",
and=" AND ",   or=" OR ",
comma=", ",    dot=".",
max="MAX",

groupBy=" GROUP BY ",   having=" HAVING ",   count="COUNT",   countAll="COUNT(*)",
orderBy=" ORDER BY ",   ascending=" ASC",    descending=" DESC",

like=" LIKE ",   notLike=" NOT LIKE ",
IN=" IN ",       notIN=" NOT IN ",
limit = " LIMIT ",
offset = " OFFSET ",
randomVal = "random()",

innerJoin=" INNER JOIN ",   leftJoin=" LEFT JOIN ",   crossJoin=" CROSS JOIN ",

ID="id", RowID="ROWID",
Name = "name",
Position = "position",
Type = "type", DbTypeTable = "table",


TABLE_EntityVersions = "EntityVersions",
EntityVersion = "version"

;
}
