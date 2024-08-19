package xyz.przemyk.propellerhats;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.przemyk.propellerhats.energy.ItemEnergyStorage;
import xyz.przemyk.propellerhats.items.PropellerHatItem;
import xyz.przemyk.propellerhats.network.NetworkHandler;
import xyz.przemyk.propellerhats.recipes.HatUpgradeRecipeSerializer;

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

    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MODID);
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MODID);

    public PropHatsMod(IEventBus bus, ModContainer modContainer) {
        RECIPE_SERIALIZERS.register(bus);
        ITEMS.register(bus);
        SOUND_EVENTS.register(bus);
        DATA_COMPONENT_TYPES.register(bus);
        bus.addListener(NetworkHandler::register);
        bus.addListener(PropHatsMod::addHatsToCreativeTab);
        bus.addListener(PropHatsMod::registerCapabilities);
    }

    public static final DeferredHolder<RecipeSerializer<?>, HatUpgradeRecipeSerializer> HAT_UPGRADE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("hat_upgrade", HatUpgradeRecipeSerializer::new);

    public static final DeferredHolder<Item, PropellerHatItem> IRON_HAT = ITEMS.register("iron_hat", () -> new PropellerHatItem(ArmorMaterials.IRON, new Item.Properties().stacksTo(1), 70_000, 30, 0.1f));
    public static final DeferredHolder<Item, PropellerHatItem> GOLDEN_HAT = ITEMS.register("golden_hat", () -> new PropellerHatItem(ArmorMaterials.GOLD, new Item.Properties().stacksTo(1), 100_000, 40, 0.16f));
    public static final DeferredHolder<Item, PropellerHatItem> DIAMOND_HAT = ITEMS.register("diamond_hat", () -> new PropellerHatItem(ArmorMaterials.DIAMOND, new Item.Properties().stacksTo(1), 500_000, 150, 0.22f));
    public static final DeferredHolder<Item, PropellerHatItem> NETHERITE_HAT = ITEMS.register("netherite_hat", () -> new PropellerHatItem(ArmorMaterials.NETHERITE, new Item.Properties().stacksTo(1), 1_000_000, 250, 0.3f));
    public static final DeferredHolder<Item, PropellerHatItem> CREATIVE_HAT = ITEMS.register("creative_hat", () -> new PropellerHatItem(ArmorMaterials.NETHERITE, new Item.Properties().stacksTo(1), 1, 0, 0.3f) {
        @Override
        public boolean isBarVisible(ItemStack stack) {
            return false;
        }
    });

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENERGY = DATA_COMPONENT_TYPES.register("energy", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());

    public static void addHatsToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(IRON_HAT.value());
            event.accept(GOLDEN_HAT.value());
            event.accept(DIAMOND_HAT.value());
            event.accept(NETHERITE_HAT.value());
            event.accept(CREATIVE_HAT.value());
        }
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (itemStack, context) -> new ItemEnergyStorage(itemStack, ((PropellerHatItem) itemStack.getItem()).energyCapacity),
                IRON_HAT.value(),
                GOLDEN_HAT.value(),
                DIAMOND_HAT.value(),
                NETHERITE_HAT.value()
        );
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> PROPELLER_SOUND_EVENT = SOUND_EVENTS.register("propeller", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "propeller")));

    public static boolean isFlying(Player player) {
        ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
        return !stack.isEmpty() && stack.getItem() instanceof PropellerHatItem && isFlyingIgnoreItemType(player, stack);
    }

    public static boolean hasEnergy(ItemStack stack) {
        if (stack.getItem() == CREATIVE_HAT.get()) {
            return true;
        }
        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage == null) {
            return false;
        }
        return energyStorage.getEnergyStored() > 0;
    }

    public static boolean isFlyingIgnoreItemType(Player player, ItemStack stack) {
        return isHoldingUp(player) && hasEnergy(stack);
    }
}
