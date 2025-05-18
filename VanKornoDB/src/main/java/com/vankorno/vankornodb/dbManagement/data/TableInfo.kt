package com.vankorno.vankornodb.dbManagement.data

interface TableInfo {
    val name: String
    fun createQuery(): String
}