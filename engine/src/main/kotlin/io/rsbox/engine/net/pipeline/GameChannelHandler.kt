package io.rsbox.engine.net.pipeline

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.rsbox.engine.net.Session
import java.util.concurrent.atomic.AtomicReference

@ChannelHandler.Sharable
class GameChannelHandler : ChannelInboundHandlerAdapter() {

    private val session = AtomicReference<Session>(null)

    override fun channelActive(ctx: ChannelHandlerContext) {
        val newSession = Session(ctx)
        if(!session.compareAndSet(null, newSession)) {
            throw IllegalStateException("Session for active channel has already been set.")
        }

        newSession.onConnect()
    }

    override fun channelInactive(ctx: ChannelHandlerContext) = session.get().onDisconnect()

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) = session.get().onMessage(msg)

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = session.get().onError(cause)

}