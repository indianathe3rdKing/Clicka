package com.example.clicka.extensions

import kotlin.math.min


fun <T>List<T>.trim(toIndex:Int):List<T> =
    subList(0,min(size,toIndex))