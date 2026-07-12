package com.indiphile_menziwa.clicka.base

import com.indiphile_menziwa.clicka.base.identifier.DATABASE_ID_INSERTION
import com.indiphile_menziwa.clicka.base.identifier.Identifier

class IdentifierCreator {

    private var lastGeneratedDomainId: Long=0
    fun generateNewIdentifier(): Identifier =
        Identifier(
            databaseId = DATABASE_ID_INSERTION, tempId = ++lastGeneratedDomainId
        )

    fun resetIdCount(){
        lastGeneratedDomainId=0
    }
}