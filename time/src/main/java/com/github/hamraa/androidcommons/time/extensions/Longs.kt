package com.github.hamraa.androidcommons.time.extensions

fun Long.floorDiv(y: Long): Long {
    var r = this / y
    // if the signs are different and modulo not zero, round down
    if (this xor y < 0 && r * y != this) {
        r--
    }
    return r
}

fun Long.floorMod(y: Long): Long {
    return this - floorDiv(y) * y
}