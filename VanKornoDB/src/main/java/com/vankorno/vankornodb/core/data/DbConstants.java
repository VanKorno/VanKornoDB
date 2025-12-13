package com.vankorno.vankornodb.core.data;

public class DbConstants { // TODO Remake, make internal, clean up
    
    public static final String
        InMemoryDB = ":memory:", // You can create an in-memory db for tests with this instead of your db file name
        DbTAG = "VanKornoDB", // for logs
        
        _ID = "id", // Reserved name for int IDs. Entity data classes should have this exact param name.
        RowID = "ROWID",
        _Name = "name",
        _Position = "position", // Optional column name you can use in your own projects. Has some convenience functions in the lib.
        _Type = "type",
        DbTypeTable = "table",
        
        
        
        // =====================  Internal tables:  =====================
        
        TABLE_EntityVersions = "EntityVersions",
        EntityVersion = "version",
        
        TABLE_AndroidMetadata = "android_metadata",
        TABLE_Master = "sqlite_master",
        
        
        
        // =====================  Pre-DSL syntax:  =====================
        
        select = "SELECT ",  from = " FROM ",   selectAllFrom = "SELECT * FROM ",
        dbCreateT = "CREATE TABLE IF NOT EXISTS ",
        dbDefault = " DEFAULT ",
        dbDrop = "DROP TABLE IF EXISTS ",
        deleteFrom = "DELETE FROM ",
        
        
        WHERE = " WHERE ",
        and = " AND ",   or = " OR ",
        comma = ", ",    //dot = ".",
        //max = "MAX",
        
        //GroupBy = " GROUP BY ",
        //Having = " HAVING ",   
        //count = "COUNT",
        countAll = "COUNT(*)",
    
        OrderBy = " ORDER BY ",   ascending = " ASC",    descending = " DESC",
        
        LIKE = " LIKE ",        notLIKE = " NOT LIKE ",
        IN = " IN ",            notIN = " NOT IN ",
        
        limit = " LIMIT ",
        offset = " OFFSET ",
        randomVal = "random()"
        
        //innerJoin = " INNER JOIN ",   leftJoin = " LEFT JOIN ",   crossJoin = " CROSS JOIN "
    ;
}
