package com.vankorno.vankornodb.core;

public class DbConstants {
    public static final String
        InMemoryDB=":memory:", // You can create an in-memory db for tests with this instead of your db file name
        DbTAG = "VanKornoDB", // for logs
        
        ID = "id", // Reserved name for int IDs. Entity data classes should have this exact param name.
        RowID = "ROWID",
        Name = "name",
        Position = "position", // Optional column name you can use in your own projects. Has some convenience functions in the lib.
        Type = "type",
        DbTypeTable = "table",
        
        
        
        // =====================  Internal tables:  =====================
        
        TABLE_EntityVersions = "EntityVersions",
        EntityVersion = "version",
        
        TABLE_AndroidMetadata = "android_metadata",
        TABLE_Master = "sqlite_master",
        SQL = "sql",
        TBL_Name = "tbl_name",
        Rootpage = "rootpage",
        
        
        
        // =====================  Pre-DSL syntax:  =====================
        
        select = "SELECT ",  from = " FROM ",   selectAllFrom = "SELECT * FROM ",
        dbCreateT = "CREATE TABLE IF NOT EXISTS ",
        dbDefault = " DEFAULT ",
        dbDrop = "DROP TABLE IF EXISTS ",
        deleteFrom = "DELETE FROM ",
        
        
        where = " WHERE ",
        and = " AND ",   or = " OR ",
        comma = ", ",    dot = ".",
        max = "MAX",
        
        groupBy = " GROUP BY ",   having = " HAVING ",   count = "COUNT",   countAll = "COUNT(*)",
        orderBy = " ORDER BY ",   ascending = " ASC",    descending = " DESC",
        
        like = " LIKE ",   notLike = " NOT LIKE ",
        IN = " IN ",       notIN = " NOT IN ",
        limit = " LIMIT ",
        offset = " OFFSET ",
        randomVal = "random()",
        
        innerJoin = " INNER JOIN ",   leftJoin = " LEFT JOIN ",   crossJoin = " CROSS JOIN "
    ;
}
