package io.rsbox.engine

import io.rsbox.engine.net.pipeline.GameChannelInitializer
import io.rsbox.engine.net.GameSocketServer
import io.rsbox.engine.net.pipeline.GameChannelHandler
import org.koin.dsl.module

val ENGINE_MODULE = module {
    single { Engine() }
    single { GameSocketServer() }
    factory { GameChannelInitializer() }
    factory { GameChannelHandler() }
}