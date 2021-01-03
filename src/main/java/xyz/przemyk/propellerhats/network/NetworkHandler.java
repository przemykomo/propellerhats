package xyz.przemyk.propellerhats.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import xyz.przemyk.propellerhats.PropHatsMod;

import java.util.Optional;

public class NetworkHandler {

    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1";
    private static int ID = 0;

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(PropHatsMod.MODID, "main"), () -> VERSION, VERSION::equals, VERSION::equals);

        INSTANCE.registerMessage(ID++,
                FlyPacket.class,
                FlyPacket::toBytes,
                FlyPacket::new,
                FlyPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
