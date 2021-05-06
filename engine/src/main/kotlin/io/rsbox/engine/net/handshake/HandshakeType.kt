package io.rsbox.engine.net.handshake

enum class HandshakeType(val opcode: Int) {

    LOGIN(14),
    JS5(15);

    companion object {
        val values = enumValues<HandshakeType>()

        fun forOpcode(opcode: Int): HandshakeType {
            return values.firstOrNull { it.opcode == opcode } ?: throw RuntimeException("Unknown handshake opcode: $opcode.")
        }
    }
}