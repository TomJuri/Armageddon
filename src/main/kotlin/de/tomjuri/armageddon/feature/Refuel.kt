package de.tomjuri.armageddon.feature

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.text.NumberFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class Refuel {

    private var fuel = 0
    private var maxFuel = 0
    private var enabled = false

   @SubscribeEvent
   fun onTick() {
   }

    fun start(fuel: Int, maxFuel: Int) {
        enabled = true

    }

    private enum class State {

    }
}