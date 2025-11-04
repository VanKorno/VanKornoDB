package com.vankorno.db.entities

import com.vankorno.db.entities.versions.VersionEntity
import com.vankorno.vankornodb.dbManagement.migration.data.MigrationBundle
import kotlin.reflect.KClass

enum class EntityEnum(                                         val currVersion: Int,
                                                               val dbRowName: String,
                                                               val currClass: KClass<*>,
                                                         val migrationBundle: Lazy<MigrationBundle>
) {
    Versions(1,   "Versions",    VersionEntity::class,       lazy { migrationsVersions() })
    
    
}




const val TABLE_Versions = "versions"