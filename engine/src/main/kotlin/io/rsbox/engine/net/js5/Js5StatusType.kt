package io.rsbox.engine.net.js5

import io.rsbox.engine.StatusType

enum class Js5StatusType(override val id: Int) : StatusType {

    SUCCESSFUL(0),
    OUT_OF_DATE(6);

    companion object {
        val values = enumValues<Js5StatusType>()
    }
}