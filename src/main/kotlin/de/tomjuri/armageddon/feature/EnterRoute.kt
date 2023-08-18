package de.tomjuri.armageddon.feature

import de.tomjuri.armageddon.util.*
import java.util.concurrent.CompletableFuture

object EnterRoute {
    fun enterRoute(): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        var result = false
        val rotation = AngleUtil.getRotationForBlock(routeManager.getClosest())
        if(rotation != null) {
            RotationUtil.ease(rotation, config.lookAtBlockTime.toLong())
            Thread.sleep(config.lookAtBlockTime.toLong())
            KeyBindUtil.rightClick()
            Thread.sleep(350)
            if(routeManager.getStandingOn() != -1)
                result = true
        }
        if(result) {
            Logger.info("Entered route.")
        } else {
            Logger.error("Failed to enter route.")
        }
        future.complete(result)
        return future
    }
}