package com.example.clicka.interfaces

interface EntityWithId {
    val id: Long

}

fun Collection<EntityWithId>.containsId(id: Long): Boolean =
    find {entity -> entity.id == id} != null