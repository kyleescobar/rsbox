package io.rsbox.engine.net

import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.rsbox.common.Injectable
import io.rsbox.config.ServerConfig
import io.rsbox.engine.net.handshake.HandshakeProtocol
import io.rsbox.engine.net.pipeline.GameChannelDecoder
import io.rsbox.engine.net.pipeline.GameChannelEncoder
import org.koin.core.component.inject
import org.tinylog.kotlin.Logger
import java.util.concurrent.atomic.AtomicReference

class Session(val ctx: ChannelHandlerContext) : Injectable {

    private val serverConfig: ServerConfig by inject()

    val sessionId = (Math.random() * Int.MAX_VALUE)

    val channel: Channel get() = ctx.channel()

    val address = channel.remoteAddress()

    var seed: Long = 0L

    var protocol = HandshakeProtocol(this)

    fun onConnect() {
        val p = ctx.pipeline()
        p.addBefore("handler", "decoder", GameChannelDecoder(this))
        p.addAfter("decoder", "encoder", GameChannelEncoder(this))
    }

    fun onDisconnect() {
        println("Session Disconnected.")
    }

    fun onMessage(msg: Any) {
        protocol.handle(msg)
    }

    fun onError(cause: Throwable) {
        if(serverConfig[ServerConfig.developerMode]) {
            if(cause.stackTrace.isEmpty() || cause.stackTrace[0].methodName != "read0") {
                Logger.error(cause) { "An exception occurred for [session: $sessionId]." }
            }
        }
    }

    fun write(msg: Any): ChannelFuture {
        return ctx.write(msg)
    }

    fun flush() {
        ctx.flush()
    }
}