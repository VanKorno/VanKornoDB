// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
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
        
        SELECT = "SELECT ",  FROM = " FROM ",   SELECT_ALL_FROM = "SELECT * FROM ",
        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ",
        DEFAULT = " DEFAULT ",
        
        
        WHERE = " WHERE ",
        and = " AND ",   or = " OR ",
        comma = ", ",    //dot = ".",
        //max = "MAX",
        
        //GroupBy = " GROUP BY ",
        //Having = " HAVING ",   
        //count = "COUNT",
        countAll = "COUNT(*)",
    
        ORDER_BY = " ORDER BY ",   ASCENDING = " ASC",    DESCENDING = " DESC",
        
        LIKE = " LIKE ",        NOT_LIKE = " NOT LIKE ",
        IN = " IN ",            NOT_IN = " NOT IN ",
        
        LIMIT = " LIMIT ",
        OFFSET = " OFFSET ",
        RANDOM = "RANDOM()"
        
        //innerJoin = " INNER JOIN ",   leftJoin = " LEFT JOIN ",   crossJoin = " CROSS JOIN "
    ;
}
