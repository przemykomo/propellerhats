package xyz.przemyk.propellerhats.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.przemyk.propellerhats.PropHatsMod;
import xyz.przemyk.propellerhats.items.PropellerHatItem;

import java.util.function.Supplier;

public class FlyPacket {

    private final boolean start; // true -> player starts holding a button, false -> player stops holding a button

    public FlyPacket(boolean start) {
        this.start = start;
    }

    public FlyPacket(PacketBuffer buffer) {
        this.start = buffer.readBoolean();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeBoolean(start);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity player = ctx.getSender();
            if (player != null) {
                ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
                if (stack.getItem() instanceof PropellerHatItem) {
                    PropHatsMod.setHoldingUp(player, start);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
