package io.rsbox.engine.net

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.rsbox.common.Injectable
import org.koin.core.component.inject
import org.tinylog.kotlin.Logger
import java.net.InetSocketAddress
import kotlin.system.exitProcess

class GameSocketServer : Injectable {

    private val bossGroup = NioEventLoopGroup(3)
    private val workerGroup = NioEventLoopGroup(1)
    private val bootstrap = ServerBootstrap()
    private lateinit var channel: Channel

    private val channelInitializer: GameChannelInitializer by inject()

    init {
        bootstrap
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(channelInitializer)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
    }

    fun bind(address: InetSocketAddress): ChannelFuture {
        Logger.info("Attempting to bind engine networking...")

        val future = bootstrap.bind(address).addListener { connectionResult ->
            if(connectionResult.isSuccess) {
                this.onBindSuccess(address)
            } else {
                this.onBindFailure(address, connectionResult.cause())
            }
        }

        this.channel = future.channel()
        return future
    }

    private fun onBindSuccess(address: InetSocketAddress) {
        Logger.info("Engine is now listening for connections on ${address.hostString}:${address.port}...")
    }

    private fun onBindFailure(address: InetSocketAddress, cause: Throwable) {
        Logger.error { "Failed to bind engine networking to ${address.hostString}:${address.port}. \nWould you like to try again? (y/n): " }
        promptRetry(address, cause)
    }

    private fun promptRetry(address: InetSocketAddress, cause: Throwable) {
        val retryResult = awaitRetryInput(address, cause)

        if(retryResult) {
            bind(address)
        } else {
            Logger.error(cause) { "Quitting process, failed to bind engine networking...\n" }
            exitProcess(-1)
        }
    }

    private fun awaitRetryInput(address: InetSocketAddress, cause: Throwable): Boolean {
        var input = ""
        while(input == "") {
            input = System.`in`.bufferedReader().readLine()
        }

        if(input == "y" || input == "yes") {
            return true
        }
        else if(input == "n" || input == "no") {
            return false
        } else {
            promptRetry(address, cause)
        }

        return false
    }

    fun shutdownGracefully() {
        Logger.info("Shutting down networking connections gracefully.")

        channel.close()

        bootstrap.config().group().shutdownGracefully()
        bootstrap.config().childGroup().shutdownGracefully()

        try {
            bootstrap.config().group().terminationFuture().sync()
            bootstrap.config().childGroup().terminationFuture().sync()
        } catch( e : InterruptedException) {
            Logger.error(e) { "Networking server shutdown was interrupted and forcefully killed." }
        }
    }
}