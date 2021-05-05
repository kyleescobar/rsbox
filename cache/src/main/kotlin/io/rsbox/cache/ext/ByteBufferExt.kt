package io.rsbox.cache.ext

import java.nio.ByteBuffer
import kotlin.experimental.and

val ByteBuffer.medium: Int get() {
    return (this.short.toInt() shl 8) or ((this.get() and 0xFF.toByte()).toInt())
}

fun ByteBuffer.byteSlice(length: Int): ByteBuffer {
    val index = this.position() + length
    val slice = this.duplicate().limit(index)
    this.position(index)

    return slice
}