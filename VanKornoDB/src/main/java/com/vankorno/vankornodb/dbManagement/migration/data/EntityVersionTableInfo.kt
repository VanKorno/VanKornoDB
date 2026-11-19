package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.core.data.DbConstants.TABLE_EntityVersions

internal val TEntityVersions = _EntityVersion.table(TABLE_EntityVersions)
val _TTTEntityVersion = TEntityVersions.info
