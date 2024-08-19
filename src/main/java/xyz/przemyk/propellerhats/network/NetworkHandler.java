package xyz.przemyk.propellerhats.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("2");
        registrar.playToServer(
            FlyPacket.TYPE,
            FlyPacket.STREAM_CODEC,
            FlyPacket::handle
        );
    }
}
