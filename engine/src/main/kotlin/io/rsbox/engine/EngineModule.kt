package io.rsbox.engine

import io.rsbox.engine.net.GameChannelInitializer
import io.rsbox.engine.net.GameSocketServer
import org.koin.dsl.module

val ENGINE_MODULE = module {
    single { Engine() }
    single { GameSocketServer() }
    factory { GameChannelInitializer() }
}