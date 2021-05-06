package io.rsbox.engine.net.pipeline

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler
import io.netty.handler.traffic.GlobalTrafficShapingHandler
import io.rsbox.common.Injectable
import org.koin.core.component.inject
import java.util.concurrent.Executors

class GameChannelInitializer : ChannelInitializer<SocketChannel>(), Injectable {

    private val gameHandler: GameChannelHandler by inject()
    private val globalTraffic = GlobalTrafficShapingHandler(Executors.newSingleThreadScheduledExecutor(), 1000, 1000, 1000)

    override fun initChannel(ch: SocketChannel) {
        val timeoutHandler = IdleStateHandler(IDLE_READ_TIMEOUT, IDLE_WRITE_TIMEOUT, 0)

        /*
         * Build the initial connection pipeline
         */

        val p = ch.pipeline()

        p.addLast("global_traffic", globalTraffic)
        p.addLast("timeout", timeoutHandler)
        p.addLast("handler", gameHandler)
    }

    companion object {
        private const val IDLE_READ_TIMEOUT = 30
        private const val IDLE_WRITE_TIMEOUT = 30
    }
}