package io.rsbox.engine

import io.rsbox.cache.CacheStore
import io.rsbox.common.Injectable
import io.rsbox.config.ServerConfig
import io.rsbox.engine.net.GameSocketServer
import org.koin.core.component.inject
import org.tinylog.kotlin.Logger
import java.net.InetSocketAddress
import java.nio.file.Paths

class Engine : Injectable {

    private val serverConfig: ServerConfig by inject()
    private val gameSocketServer: GameSocketServer by inject()

    lateinit var cacheStore: CacheStore
        private set

    private fun init() {
        Logger.info("Initializing game engine...")

        this.loadCacheStore()
    }

    fun start() {
        this.init()

        Logger.info("Starting game engine...")

        this.startEngineNetworking()
    }

    fun stop() {
        Logger.info("Shutting down game engine...")

        /*
         * Shutdown the networking
         */
        gameSocketServer.shutdownGracefully()
    }

    private fun loadCacheStore() {
        Logger.info("Loading cache file store.")
        cacheStore = CacheStore.open(Paths.get("data/" + serverConfig[ServerConfig.pathCacheDir]))

        Logger.info("Successfully loaded ${cacheStore.archiveCount} archive files.")
    }

    private fun startEngineNetworking() {
        Logger.info("Initializing engine network server.")

        val listenAddress = serverConfig[ServerConfig.listenAddress]
        val listenPort = serverConfig[ServerConfig.listenPort]

        val address = InetSocketAddress(listenAddress, listenPort)

        /*
         * Bind the game socket server to the [address]
         */
        gameSocketServer.bind(address)
    }
}