package io.rsbox.cache.disk

import io.rsbox.cache.ext.medium
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class IdxFile(val file: Path) : Closeable {

    private val channel = FileChannel.open(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ)

    private val buf = ByteBuffer.allocate(ENTRY_SIZE)

    val size: Int get() = (channel.size() / ENTRY_SIZE).toInt()

    fun read(group: Int): IdxEntry? {
        val pos = group * ENTRY_SIZE
        if(pos + ENTRY_SIZE > channel.size()) {
            return null
        }

        channel.read(buf, pos.toLong())
        buf.clear()

        val length = buf.medium
        val sector = buf.medium
        buf.clear()

        if(length <= 0 && sector == 0) {
            return null
        }

        return IdxEntry(length, sector)
    }

    override fun close() {
        channel.close()
    }

    companion object {
        const val ENTRY_SIZE = 6
    }
}