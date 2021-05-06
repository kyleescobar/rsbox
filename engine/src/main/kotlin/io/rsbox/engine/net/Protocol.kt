package io.rsbox.engine.net

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelPipeline

abstract class Protocol(val session: Session) {

    abstract fun encode(msg: Any, out: ByteBuf)

    abstract fun decode(buf: ByteBuf, out: MutableList<Any>)

    abstract fun handle(msg: Any)

    abstract fun disconnect()

}