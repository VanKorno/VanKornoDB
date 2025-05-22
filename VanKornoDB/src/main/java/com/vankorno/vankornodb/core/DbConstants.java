package com.vankorno.vankornodb.core;

public class DbConstants {
    public static final String
InMemoryDB=":memory:",

select="SELECT ",  from=" FROM ",   selectAllFrom="SELECT * FROM ",
dbCreateT="CREATE TABLE ",
dbDefault=" DEFAULT ",
dbDrop="DROP TABLE IF EXISTS ",
deleteFrom="DELETE FROM ",
MasterTable="sqlite_master",

where=" WHERE ",
and=" AND ",   or=" OR ",
comma=", ",    dot=".",

groupBy=" GROUP BY ",   having=" HAVING ",   count="COUNT",   countAll="COUNT(*)",
orderBy=" ORDER BY ",   ascending=" ASC",    descending=" DESC",

like=" LIKE ",   limit=" LIMIT ",   offset=" OFFSET ",
randomVal="random()",

innerJoin=" INNER JOIN ",   leftJoin=" LEFT JOIN ",   crossJoin=" CROSS JOIN ",

ID="id", RowID="ROWID",
Name="name", Priority = "priority"
;
}
