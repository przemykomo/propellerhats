package xyz.przemyk.propellerhats;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
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
    private static final Map<Player, Boolean> HOLDING_UP = new HashMap<>();

    public static boolean isHoldingUp(Player playerEntity) {
        if (!HOLDING_UP.containsKey(playerEntity)) {
            HOLDING_UP.put(playerEntity, false);
        }

        return HOLDING_UP.get(playerEntity);
    }

    public static void setHoldingUp(Player playerEntity, boolean value) {
        HOLDING_UP.put(playerEntity, value);
    }

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

    public PropHatsMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        SOUND_EVENTS.register(bus);
        NetworkHandler.registerMessages();
        bus.addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);
    }

    private void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        event.getRegistry().register(HatUpgradeRecipe.SERIALIZER);
    }

    public static final RegistryObject<PropellerHatItem> IRON_HAT = ITEMS.register("iron_hat", () -> new PropellerHatItem(ArmorMaterials.IRON, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1), 70_000, 30, 0.1f));
    public static final RegistryObject<PropellerHatItem> GOLDEN_HAT = ITEMS.register("golden_hat", () -> new PropellerHatItem(ArmorMaterials.GOLD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1), 100_000, 40, 0.16f));
    public static final RegistryObject<PropellerHatItem> DIAMOND_HAT = ITEMS.register("diamond_hat", () -> new PropellerHatItem(ArmorMaterials.DIAMOND, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1), 500_000, 150, 0.22f));
    public static final RegistryObject<PropellerHatItem> NETHERITE_HAT = ITEMS.register("netherite_hat", () -> new PropellerHatItem(ArmorMaterials.NETHERITE, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1), 1_000_000, 250, 0.3f));
    public static final RegistryObject<PropellerHatItem> CREATIVE_HAT = ITEMS.register("creative_hat", () -> new PropellerHatItem(ArmorMaterials.NETHERITE, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1), 1, 0, 0.3f) {
        @Override
        public boolean showDurabilityBar(ItemStack stack) {
            return false;
        }
    });

    public static final RegistryObject<SoundEvent> PROPELLER_SOUND_EVENT = SOUND_EVENTS.register("propeller", () -> new SoundEvent(new ResourceLocation(MODID, "propeller")));

    public static boolean isFlying(Player player) {
        ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
        return !stack.isEmpty() && stack.getItem() instanceof PropellerHatItem && isFlyingIgnoreItemType(player, stack);
    }

    public static boolean isFlyingIgnoreItemType(Player player, ItemStack stack) {
        return isHoldingUp(player) && (stack.getItem() == CREATIVE_HAT.get() || stack.getCapability(CapabilityEnergy.ENERGY).map(energy -> energy.getEnergyStored() > 0).orElse(false));
    }
}
