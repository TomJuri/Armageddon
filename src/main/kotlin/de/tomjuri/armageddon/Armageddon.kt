package de.tomjuri.armageddon

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import de.tomjuri.armageddon.command.ArmageddonCommand
import de.tomjuri.armageddon.config.ArmageddonConfig
import de.tomjuri.armageddon.feature.Failsafe
import de.tomjuri.armageddon.macro.Macro
import de.tomjuri.armageddon.macro.RouteManager
import de.tomjuri.armageddon.util.AuthUtil
import de.tomjuri.armageddon.util.mc
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiErrorScreen
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

@Mod(modid = "armageddon", name = "Armageddon", version = "%%VERSION%%")
class Armageddon {

    companion object {
        lateinit var config: ArmageddonConfig
        lateinit var routeManager: RouteManager
        lateinit var macro: Macro
        lateinit var failsafe: Failsafe
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        Security.addProvider(BouncyCastleProvider())

        val realData = "${AuthUtil.getHWID()}|${System.currentTimeMillis()}|${mc.session.playerID}".toByteArray()
        val publicKey = AuthUtil.getPublicKey("MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAkVCUJutVsogy3E9MHLxKyRu/5U6b8uBwelJvdnxfqsxWqzu9pFygDAGQHJJCUeXc7g8Pna40h/qA0dcYMsZ2Jf0ajZv+GTus+xZh58mfGkRT8zHtvO6UkiuaBMysDE6z5PkyTHp1tdBkT+Ksp8qKbZA8klFa30eeIEiXIdIQ4Be3aMcAVjOQF0h0V6hh4V8/KdwstD7EKdvOm2KvPmyJQ0CeMn+OaT9+33gnxul4BrAjktxvVRkHBOnk9daKZJJQmEdYlZU+5Btb3dFJblJhIEgjqQ1CqHu3o24yL7WVHHRHIge4j1HXGzDHqDbEYpsSfAFovQi68kaq7KhlmL1SDG5Ty1+hnS78fda5ynRSl+K9rlYRiF/kEk+d/h1AQL6nKUs3LDdR6LucFocE/awA/kDEWGmTxbxjSw/2pVoIE9wRe5UwqStRTQuViuxNjRyOD45/SEbrOi6rn6FZwiwbwJAupmcUh7m4kcLkRaOhZ0lXliSHeYvA1V/OnwSzjgcBSkzrkjm6NJwV4tumTTmBlpGMpUA+Xl6+ngvF8OZ52xJ+fFZmSCsu4olLIlBdnZzWNv6hAWGgVOUMGthBPznCe7yYVFM5HlEA9c4YgW9K2m2nHN1zvkkujpo3Lc6HR3+LKAiiQ39+lXcombPDhcYDM7GU4ExTO49YKGGbhblhAmsCAwEAAQ==")
        val privateKey = AuthUtil.getPrivateKey("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")
        val fakeAndRealData = List(10) {
            if (it == 4) AuthUtil.rsaEncrypt(realData, publicKey)
            else AuthUtil.rsaEncrypt(AuthUtil.generateRandomString(realData.size).toByteArray(), publicKey)
        }
        val fakeAndRealDataJoined = fakeAndRealData.joinToString("-")
        val url = "http://127.0.0.1:5000/Q6W2ecZlZ3InKEEriO7onlQ1i1vAVCC9ipOF4qwsfNIK1xg6Z80NZTgbXbOJ?DvigquT6UAztO5IdMb0iIex6skfcHrPs8lDeTCxLfzG98n0AdNWeIN2VX3Kd=$fakeAndRealDataJoined"
        val request: Request = Request.Builder().url(url).addHeader("User-Agent", "TOM-CLIENT-JAVA8-Shipping-g1vJHlyarpLgba7Fu3SoUm5dGkoDHeI5f8QIFur7").build()

        try {
            OkHttpClient().newCall(request).execute().use { response ->
                val responseBody = response.body()
                if (responseBody != null) {
                    val decoded = AuthUtil.rsaDecrypt(responseBody.string(), privateKey)
                    val split = decoded.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (
                        "success" == split[0]
                        && AuthUtil.getHWID() == split[1]
                        && mc.session.playerID == split[3]
                        && System.currentTimeMillis() - split[4].toLong() < 10000) {
                        config = ArmageddonConfig()
                        routeManager = RouteManager()
                        macro = Macro()
                        failsafe = Failsafe()
                        CommandManager.register(ArmageddonCommand())
                        MinecraftForge.EVENT_BUS.register(routeManager)
                        MinecraftForge.EVENT_BUS.register(macro)
                        MinecraftForge.EVENT_BUS.register(failsafe)
                        return
                    }
                }
            }
        } catch (ignored: Exception) { }

        throw object : CustomModLoadingErrorDisplayException() {
            override fun initGui(guiErrorScreen: GuiErrorScreen?, fontRenderer: FontRenderer?) {}
            override fun drawScreen(guiErrorScreen: GuiErrorScreen, fontRenderer: FontRenderer, i: Int, j: Int, f: Float) {
                guiErrorScreen.drawCenteredString(fontRenderer, "Authentication for Armageddon failed!", guiErrorScreen.width / 2, 20, 16711680)
                guiErrorScreen.drawString(fontRenderer, "Possible solutions are:", 20, 40, 16777215)
                guiErrorScreen.drawString(fontRenderer, "   - Make sure you are on the account you registered.", 20, 50, 16777215)
                guiErrorScreen.drawString(fontRenderer, "   - Reset your HWID using /resethwid on the Discord Server", 20, 60, 16777215)
                guiErrorScreen.drawString(fontRenderer, "   - Check that you have a working Internet connection", 20, 70, 16777215)
                guiErrorScreen.drawString(fontRenderer, "   - Contact a staff member on the Discord Server", 20, 80, 16777215)
            }
        }
    }
}