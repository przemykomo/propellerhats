package xyz.przemyk.propellerhats;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.przemyk.propellerhats.items.PropellerHatItem;
import xyz.przemyk.propellerhats.network.NetworkHandler;
import xyz.przemyk.propellerhats.recipes.HatUpgradeRecipe;

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
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

    public PropHatsMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        SOUND_EVENTS.register(bus);
        NetworkHandler.registerMessages();
        bus.addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);
    }

    private void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(HatUpgradeRecipe.SERIALIZER);
    }

    public static final RegistryObject<PropellerHatItem> IRON_HAT = ITEMS.register("iron_hat", () -> new PropellerHatItem(ArmorMaterial.IRON, new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1), 70_000, 30, 0.1f));
    public static final RegistryObject<PropellerHatItem> GOLDEN_HAT = ITEMS.register("golden_hat", () -> new PropellerHatItem(ArmorMaterial.GOLD, new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1), 100_000, 40, 0.16f));
    public static final RegistryObject<PropellerHatItem> DIAMOND_HAT = ITEMS.register("diamond_hat", () -> new PropellerHatItem(ArmorMaterial.DIAMOND, new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1), 500_000, 150, 0.22f));
    public static final RegistryObject<PropellerHatItem> NETHERITE_HAT = ITEMS.register("netherite_hat", () -> new PropellerHatItem(ArmorMaterial.NETHERITE, new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1), 1_000_000, 250, 0.3f));

    public static final RegistryObject<SoundEvent> PROPELLER_SOUND_EVENT = SOUND_EVENTS.register("propeller", () -> new SoundEvent(new ResourceLocation(MODID, "propeller")));

    public static boolean isFlying(PlayerEntity player) {
        ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
        return !stack.isEmpty() && stack.getItem() instanceof PropellerHatItem && isFlyingIgnoreItemType(player, stack);
    }

    public static boolean isFlyingIgnoreItemType(PlayerEntity player, ItemStack stack) {
        return isHoldingUp(player) && stack.getCapability(CapabilityEnergy.ENERGY).map(energy -> energy.getEnergyStored() > 0).orElse(false);
    }
}
