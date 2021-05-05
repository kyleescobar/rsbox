package io.rsbox.launcher

import io.rsbox.common.Injectable
import io.rsbox.config.CONFIG_MODULE
import io.rsbox.config.ServerConfig
import io.rsbox.engine.ENGINE_MODULE
import io.rsbox.engine.Engine
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.tinylog.kotlin.Logger

class Launcher : Injectable {

    private val serverConfig: ServerConfig by inject()
    private val engine: Engine by inject()

    private fun init() {
        Logger.info("Initializing...")

        DirectoryChecker.checkExistsOrCreateDirs()
        this.loadConfigurations()
    }

    fun start() {
        Logger.info("Launching RSBox server: '${serverConfig[ServerConfig.serverName]}'...")

        /*
         * Init the launcher.
         */
        this.init()

        /*
         * Start the game engine.
         */
        engine.start()
    }

    private fun loadConfigurations() {
        Logger.info("Loading configuration files...")

        /*
         * Load the server.yml configuration file.
         */
        serverConfig.load()
    }

    companion object : Injectable {

        private val modules = listOf(
            LAUNCHER_MODULE,
            CONFIG_MODULE,
            ENGINE_MODULE
        )

        lateinit var launcher: Launcher
            private set

        @JvmStatic
        fun main(args: Array<String>) {
            startKoin { modules(modules) }
            launcher = get()

            /*
             * Start the launcher.
             */
            launcher.start()
        }
    }
}