package io.rsbox.cache.disk

import io.rsbox.cache.ext.byteSlice
import io.rsbox.cache.ext.medium
import java.io.Closeable
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.math.min

class DatFile(val file: Path) : Closeable {

    private val channel = FileChannel.open(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ)
    private val buf = ByteBuffer.allocate(SECTOR_SIZE)

    fun read(archive: Int, group: Int, length: Int, initialSector: Int): ByteBuffer {
        var output = ByteBuffer.allocate(length)
        var chunk = 0
        var sector = initialSector
        while(output.hasRemaining()) {
            channel.read(buf, (sector * SECTOR_SIZE).toLong())
            buf.clear()
            val sectorGroup = buf.short.toInt()
            if(group != sectorGroup) throw IOException("Mismatching group $sectorGroup in sector $sector. Expecting $group")
            val sectorChunk = buf.short.toInt()
            if(chunk != sectorChunk) throw IOException("Mismatching chunk $sectorChunk in sector $sector. Expecting $chunk.")
            sector = buf.medium
            val sectorArchive = buf.get().toInt()
            if(archive != sectorArchive) throw IOException("Mismatching archive $sectorArchive in sector $sector. Expecting $archive.")
            output.put(buf.byteSlice(min(buf.remaining(), output.remaining())))
            buf.clear()
            chunk++
        }

        return output.clear()
    }

    override fun close() {
        channel.close()
    }

    companion object {
        const val SECTOR_SIZE = 520
    }
}