package com.vankorno.vankornodb.api

import com.vankorno.vankornodb.dbManagement.data.BaseEntity

/**
 * Marker interface for all VanKornoDB entities.
 * 
 * Entities must be data classes and implement this interface
 * to be mappable by VanKornoDB.
 */
interface DbEntity : BaseEntity


interface OldEntity : BaseEntity


/**
 * For classes that are used to get data from db, but do not create or migrate any actual db tables.
 */
interface LiteEntity : BaseEntity



