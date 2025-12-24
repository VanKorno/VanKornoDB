// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.api

import com.vankorno.vankornodb.dbManagement.data.BaseEntity
import com.vankorno.vankornodb.dbManagement.data.EntityColumnsInternal
import com.vankorno.vankornodb.dbManagement.data.NormalEntity

/**
 * Marker interface for all VanKornoDB entities.
 * 
 * Entities must be data classes and implement this interface
 * to be mappable by VanKornoDB.
 */
interface CurrEntity : NormalEntity


interface OldEntity : NormalEntity


/**
 * For classes that are used to get data from db, but do not create or migrate any actual db tables.
 */
interface LiteEntity : BaseEntity







interface EntityColumns : EntityColumnsInternal






