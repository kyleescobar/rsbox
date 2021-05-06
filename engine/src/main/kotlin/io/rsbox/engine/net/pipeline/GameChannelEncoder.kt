package io.rsbox.engine.net.pipeline

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import io.rsbox.engine.net.Session

class GameChannelEncoder(private val session: Session) : MessageToByteEncoder<Any>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Any, out: ByteBuf) {
        session.protocol.encode(msg, out)
    }

}