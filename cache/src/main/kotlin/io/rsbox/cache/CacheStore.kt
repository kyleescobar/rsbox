package io.rsbox.cache

import io.rsbox.cache.disk.DatFile
import io.rsbox.cache.disk.IdxFile
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path

class CacheStore private constructor(private val directory: Path) : Closeable {

    private val datFile = DatFile(directory.resolve(DAT_FILE_NAME))
    private val idxFiles = mutableMapOf<Int, IdxFile>()

    val archiveCount: Int get() {
        return findIdxFile(MASTER_ARCHIVE_ID).size
    }

    private fun findIdxFile(archive: Int): IdxFile {
        return if (!idxFiles.containsKey(archive)) {
            val f = IdxFile(directory.resolve(IDX_FILE_NAME + archive))
            idxFiles[archive] = f
            f
        } else {
            idxFiles[archive]!!
        }
    }

    fun readGroup(archive: Int, group: Int): ByteBuffer {
        val idxEntry = findIdxFile(archive).read(group)
        return if(idxEntry == null) {
            ByteBuffer.allocate(0)
        } else {
            datFile.read(archive, group, idxEntry.length, idxEntry.sector)
        }
    }

    override fun close() {
        datFile.close()
        idxFiles.forEach { it.value.close() }
    }

    companion object {

        fun open(directory: Path): CacheStore {
            Files.createDirectories(directory)
            return CacheStore(directory)
        }
    }
}