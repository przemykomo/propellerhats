package xyz.przemyk.propellerhats.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.network.FlyPacket;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

    private static boolean lastFlyState = false;

    @SubscribeEvent
    public static void preClientTick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            boolean flyState = mc.options.keyJump.isDown();
            if (flyState != lastFlyState) {
                PropHatsMod.setHoldingUp(mc.player, flyState);
                lastFlyState = flyState;
                PacketDistributor.sendToServer(new FlyPacket(flyState));
            }
        }
    }

    @SubscribeEvent
    public static void postClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && PropHatsMod.isFlying(mc.player) && !PropellerSound.isPlaying(mc.player.getId())) {
            mc.getSoundManager().play(new PropellerSound(mc.player));
        }
    }
}
