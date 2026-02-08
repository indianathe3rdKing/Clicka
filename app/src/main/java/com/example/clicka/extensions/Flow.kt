package com.example.clicka.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


inline fun <T, R> Flow<List<T>>.mapList(crossinline transform: suspend (value: T) -> R): Flow<List<R>> =
map{
    list ->
    list.map { mapValue ->
        transform(mapValue)
    }
}

inline fun <T, R> Flow<List<T>>.mapListIndexed(crossinline transform: suspend (index: Int, value: T) -> R): Flow<List<R>> =
map{
    list ->
    list.mapIndexed { index, mapValue ->
        transform(index, mapValue)
    }
}