package xyz.przemyk.propellerhats.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.items.PropellerHatItem;

public record FlyPacket(boolean start) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<FlyPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PropHatsMod.MODID, "fly_packet"));

    public static final StreamCodec<ByteBuf, FlyPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL,
        FlyPacket::start,
        FlyPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
            if (stack.getItem() instanceof PropellerHatItem) {
                PropHatsMod.setHoldingUp(player, start);
            }
        });
    }
}
