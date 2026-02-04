package com.example.clicka.base.identifier


/**
 * Identifier for all model objects stored in database
 * In order to ease the user creation and edition of those structure,it stores 2 identifiers
 */

data class Identifier(
    val databaseId: Long = DATABASE_ID_INSERTION,
    val tempId: Long? = null,
){
    constructor(id:Long,asTemporary: Boolean= false):this(
        databaseId = if (asTemporary) DATABASE_ID_INSERTION else id,
        tempId = if (asTemporary) id else null,
    )

//    Ensure correctness of the ids
    init {
        if(databaseId== DATABASE_ID_INSERTION && tempId == null)
            throw IllegalArgumentException("DomainId must be provided when using db 0")

        if (databaseId != DATABASE_ID_INSERTION && tempId != null)
            throw IllegalArgumentException("Both ids can't be set")
    }

    fun isInDatabase(): Boolean = databaseId != DATABASE_ID_INSERTION

}

const val DATABASE_ID_INSERTION=0L