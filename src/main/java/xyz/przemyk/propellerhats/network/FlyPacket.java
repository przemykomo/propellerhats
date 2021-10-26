package xyz.przemyk.propellerhats.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.items.PropellerHatItem;

import java.util.function.Supplier;

public class FlyPacket {

    private final boolean start; // true -> player starts holding a button, false -> player stops holding a button

    public FlyPacket(boolean start) {
        this.start = start;
    }

    public FlyPacket(FriendlyByteBuf buffer) {
        this.start = buffer.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(start);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
                if (stack.getItem() instanceof PropellerHatItem) {
                    PropHatsMod.setHoldingUp(player, start);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
