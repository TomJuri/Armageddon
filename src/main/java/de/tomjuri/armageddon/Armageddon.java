package de.tomjuri.armageddon;

import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import de.tomjuri.armageddon.command.ArmageddonCommand;
import de.tomjuri.armageddon.config.ArmageddonConfig;
import de.tomjuri.armageddon.feature.AbiphoneRefuel;
import de.tomjuri.armageddon.util.AuthUtil;
import de.tomjuri.armageddon.util.Ref;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Mod(modid = "armageddon", name = "Armageddon", version = "%%VERSION%%")
public class Armageddon {

    @Mod.Instance("armageddon")
    public static Armageddon INSTANCE;
    private ArmageddonConfig config;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        try {
            String realData = AuthUtil.getHWID() + "|" + System.currentTimeMillis() + "|" + Ref.mc().getSession().getPlayerID();
            RSAPublicKey publicKey = AuthUtil.getPublicKey("MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAtXyd0UpgM40+t5yLwYesXGHNx/QU/HF1sa2S5c0q2SbQ8YeCaLj1apO0wXd626Axtek+IKjeUMeYrjM8XYerQ9U2T+BszUq9A7C5OdmZFDMnvRnDl9KBlJccRYTFvZG6X1UW+RkLnJ3Mpf8WHOgiVSE27+rcWc1GwCEWVElDFajbRxZFYJvXJVrulB36rz7I6RLELagmUFA23R3/SUZZvVRfnjXDx0oR3kWo0ZJgzqbFG/dQEa9ASD6lzI2aT0cESDj8NJxCbH55AyIzauUDjq7yTLvYsF4xLC3afQblnIKjrklI7ZRwVaT0j6edehCjZtqj9ga7Hxp2JlkSn5PREPgq80APmCwelwK2UkdNRyyHereiiC1RahT1raS2F+OfrDYRi6FxF0mowRh/AsszcgQfSV7WC9b9vXwDCzotuuZ9t9xoHP6wy/zkeCMOX3J5cSuM+UnnO1DkNjvNN3BLs/PSSga3havwGTj5vefx17h0e22kHAnH3LMLcq9RFn2evlS9TziezeMPYASDOuxXWYUQk+JwPNAP817/yuLZinjBGFpjC3gFVhxi6xW8D8dYNH8eQZx/eqWdfGZLGsp4de2aLkP5sSDG3modrYtTh57zfAc508nKLdjg9scoian2NP0gD1ItQkgPb1rF7TNsJjZIvAthdea/6OKY3Mnzzn0CAwEAAQ==");
            RSAPrivateKey privateKey = AuthUtil.getPrivateKey("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");
            StringBuilder fakeAndRealData = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                if (i == 3) {
                    fakeAndRealData.append(AuthUtil.rsaEncrypt(realData.getBytes(), publicKey));
                } else {
                    fakeAndRealData.append(AuthUtil.rsaEncrypt(AuthUtil.generateRandomString(realData.length()).getBytes(), publicKey));
                }
                if (i < 4) {
                    fakeAndRealData.append("-");
                }
            }
            String url = "https://macrohq.dev/api/Q6W2ecZlZ3InKEEriO7onlQ1i1vAVCC9ipOF4qwsfNIK1xg6Z80NZTgbXbOJ?DvigquT6UAztO5IdMb0iIex6skfcHrPs8lDeTCxLfzG98n0AdNWeIN2VX3Kd=" + fakeAndRealData;
            Request request = new Request.Builder().url(url).addHeader("User-Agent", "TOM-CLIENT-JAVA8-Shipping-g1vJHlyarpLgba7Fu3SoUm5dGkoDHeI5f8QIFur7").build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                String decoded = AuthUtil.rsaDecrypt(response.body().string(), privateKey);
                String[] split = decoded.split("\\|");
                if ("success".equals(split[0]) &&
                        AuthUtil.getHWID().equals(split[1]) &&
                        Ref.mc().getSession().getPlayerID().equals(split[2]) &&
                        System.currentTimeMillis() - Long.parseLong(split[3]) < 20000) {
                    config = new ArmageddonConfig();
                    MinecraftForge.EVENT_BUS.register(new AbiphoneRefuel());
                /*        routeManager = new RouteManager();
                    macro = new Macro();
                    failsafe = new Failsafe();
                    movementRecorder = new MovementRecorder();
                    CommandManager.register(new ArmageddonCommand());
                    MinecraftForge.EVENT_BUS.register(routeManager);
                    MinecraftForge.EVENT_BUS.register();
                    MinecraftForge.EVENT_BUS.register(macro);
                    MinecraftForge.EVENT_BUS.register(failsafe);
                    MinecraftForge.EVENT_BUS.register(movementRecorder);*/
                    return;
                }
            }
        } catch (Exception ignored) { }

        throw new CustomModLoadingErrorDisplayException() {
            @Override
            public void initGui(GuiErrorScreen guiErrorScreen, FontRenderer fontRenderer) {
            }

            @Override
            public void drawScreen(GuiErrorScreen guiErrorScreen, FontRenderer fontRenderer, int i, int j, float f) {
                guiErrorScreen.drawCenteredString(fontRenderer, "Authentication for Armageddon failed!", guiErrorScreen.width / 2, 20, 16711680);
                guiErrorScreen.drawString(fontRenderer, "Possible solutions are:", 20, 40, 16777215);
                guiErrorScreen.drawString(fontRenderer, "- Make sure you are on the account you registered.", 60, 50, 16777215);
                guiErrorScreen.drawString(fontRenderer, "- Reset your HWID using /resethwid on the Discord Server", 60, 60, 16777215);
                guiErrorScreen.drawString(fontRenderer, "- Check that you have a working Internet connection", 60, 70, 16777215);
                guiErrorScreen.drawString(fontRenderer, "- Contact a staff member on the Discord Server", 60, 80, 16777215);
                guiErrorScreen.drawString(fontRenderer, "Press ALT+F4 to quit.", 20, 100, 16711680);
            }
        };
    }

    public void openGUI() { config.openGui(); }
}
