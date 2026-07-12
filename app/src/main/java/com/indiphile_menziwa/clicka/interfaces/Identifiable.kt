package com.indiphile_menziwa.clicka.interfaces

import com.indiphile_menziwa.clicka.base.identifier.Identifier

interface Identifiable {

    val id: Identifier

    fun getDatabaseId(): Long = id.databaseId
    fun getDomainId(): Long? = id.tempId
    fun getValidId(): Long = if (isInDatabase()) id.databaseId else id.tempId
        ?: throw IllegalArgumentException("Invalid identifier")

    fun isInDatabase(): Boolean = id.isInDatabase()

}

fun List<Identifiable>.containsIdentifiable(id: Identifier): Boolean =
    find { it.id == id } != null