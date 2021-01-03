package xyz.przemyk.propellerhats.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.network.FlyPacket;
import xyz.przemyk.propellerhats.network.NetworkHandler;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class KeybindingHandler {

    private static boolean lastFlyState = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                boolean flyState = mc.gameSettings.keyBindJump.isKeyDown();
                if (flyState != lastFlyState) {
                    PropHatsMod.setHoldingUp(mc.player, flyState);
                    lastFlyState = flyState;
                    NetworkHandler.INSTANCE.sendToServer(new FlyPacket(flyState));
                }
            }
        }
    }
}
