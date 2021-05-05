package io.rsbox.config

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import com.uchuhimo.konf.Item
import com.uchuhimo.konf.source.yaml
import com.uchuhimo.konf.source.yaml.toYaml
import org.tinylog.kotlin.Logger
import java.io.File

class ServerConfig {

    private var config = Config { addSpec(Companion) }

    private val filePath = File("data/config/server.yml")

    fun load() {
        Logger.info("Loading configuration: server.yml")

        config = if(!filePath.exists()) {
            Config { addSpec(Companion) }
        } else {
            Config { addSpec(Companion) }
                .from.yaml.file(filePath)
        }

        /*
         * Save the currently load config to write the defaults.
         */
        this.save()
    }

    fun save() {
        Logger.info("Saving configuration: server.yml")
        config.toYaml.toFile(filePath)
    }

    operator fun <T> get(item: Item<T>): T {
        return config[item]
    }

    companion object : ConfigSpec("rsbox") {
        val serverName by optional("RSBox", "server-name")
        val revision by optional(195, "revision")
        val developerMode by optional(true, "developer-mode")
        val loginThreads by optional(3, "login-threads")

        val listenAddress by optional("127.0.0.1", "network.listen-address")
        val listenPort by optional(43594, "network.listen-port")

        val homeTileX by optional(100, "home-tile.x")
        val homeTileY by optional(0, "home-tile.y")
        val homeTileZ by optional(100, "home-tile.z")

        val pathCacheDir by optional("cache/", "paths.cache-dir")
        val pathXteasDir by optional("xteas/", "paths.xteas-dir")
    }
}