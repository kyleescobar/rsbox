package io.rsbox.launcher

import org.tinylog.kotlin.Logger
import java.io.File

object DirectoryChecker {

    private val dirs = listOf(
        "data/",
        "data/config/",
        "data/cache/",
        "data/xteas/",
        "data/saves/",
        "data/extensions/",
        "data/logs/",
        "data/rsa/"
    )

    fun checkExistsOrCreateDirs() {
        Logger.info("Checking directory structure...")

        dirs.map { File(it) }.forEach { dir ->
            if(!dir.exists()) {
                Logger.info("Creating empty directory '${dir.path}'.")
                dir.mkdirs()
            }
        }

        Logger.info("Directory structure verified successfully.")
    }
}