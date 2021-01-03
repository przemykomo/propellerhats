package xyz.przemyk.propellerhats;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.przemyk.propellerhats.items.PropArmorMaterial;
import xyz.przemyk.propellerhats.items.PropellerHatItem;
import xyz.przemyk.propellerhats.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Mod(PropHatsMod.MODID)
public class PropHatsMod {
    public static final String MODID = "propellerhats";
    private static final Map<PlayerEntity, Boolean> HOLDING_UP = new HashMap<>();

    public static boolean isHoldingUp(PlayerEntity playerEntity) {
        if (!HOLDING_UP.containsKey(playerEntity)) {
            HOLDING_UP.put(playerEntity, false);
        }

        return HOLDING_UP.get(playerEntity);
    }

    public static void setHoldingUp(PlayerEntity playerEntity, boolean value) {
        HOLDING_UP.put(playerEntity, value);
    }

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public PropHatsMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        NetworkHandler.registerMessages();
    }

    public static final RegistryObject<PropellerHatItem> BASIC_HAT = ITEMS.register("basic", () -> new PropellerHatItem(PropArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)));
}
