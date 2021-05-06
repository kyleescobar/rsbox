package io.rsbox.engine.net.handshake

import io.netty.buffer.ByteBuf
import io.rsbox.common.Injectable
import io.rsbox.config.ServerConfig
import io.rsbox.engine.StatusType
import io.rsbox.engine.net.Protocol
import io.rsbox.engine.net.Session
import io.rsbox.engine.net.js5.Js5StatusType
import org.koin.core.component.inject

class HandshakeProtocol(session: Session) : Protocol(session), Injectable {

    private val serverConfig: ServerConfig by inject()

    override fun disconnect() {}

    override fun encode(msg: Any, out: ByteBuf) {
        if(msg !is StatusType) {
            throw IllegalStateException("Received message of invalid type. Expecting 'StatusType'.")
        }

        out.writeByte(msg.id)
    }

    override fun decode(buf: ByteBuf, out: MutableList<Any>) {
        val opcode = buf.readUnsignedByte().toInt()
        when(HandshakeType.forOpcode(opcode)) {
            HandshakeType.LOGIN -> {
                // TODO
            }

            HandshakeType.JS5 -> {
                val revision = buf.readInt()
                val message = HandshakeMessage(revision)
                out.add(message)
            }
        }
    }

    override fun handle(msg: Any) {
        if(msg !is HandshakeMessage) {
            throw IllegalStateException("Handled message is of invalid type. Expecting 'HandshakeMessage'.")
        }

        val serverRevision = serverConfig[ServerConfig.revision]
        if(msg.revision != serverRevision) {
            session.write(Js5StatusType.OUT_OF_DATE)
        } else {
            session.write(Js5StatusType.SUCCESSFUL)
        }

        session.flush()
    }
}