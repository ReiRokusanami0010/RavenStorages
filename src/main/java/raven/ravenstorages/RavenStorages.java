package raven.ravenstorages;

import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raven.ravenstorages.blocks.RavenBlocks;
import raven.ravenstorages.containers.DebugContainer;
import raven.ravenstorages.containers.DebugContainerScreen;
import raven.ravenstorages.containers.RavenContainers;
import raven.ravenstorages.tiles.RavenTiles;
import raven.ravenstorages.capability.CapabilityDebugHandler;
import raven.ravenstorages.capability.CapabilityDebugHandler.DebugHandler;
import raven.ravenstorages.capability.SingleCapabilityProvider;
import raven.ravenstorages.item.RavenItems;

import javax.annotation.Nonnull;

import static raven.ravenstorages.RavenStorages.MOD_ID;
import static raven.ravenstorages.containers.RavenContainers.DEBUG_CONTAINER;

@Mod(MOD_ID)
public final class RavenStorages {
    public static final String MOD_ID = "raven_storages";
    public static final Logger LOGGER = LogManager.getLogger();

    public RavenStorages() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(Block.class, RavenBlocks::register);
        modEventBus.addGenericListener(TileEntityType.class, RavenTiles::register);
        modEventBus.addGenericListener(Item.class, RavenItems::register);
        modEventBus.addGenericListener(ContainerType.class, RavenContainers::register);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addGenericListener(ItemStack.class, this::attachItemStackCapability);
    }
    
    private void commonSetup(@Nonnull FMLCommonSetupEvent event) {
        CapabilityDebugHandler.register();
    }

    private void clientSetup(@Nonnull FMLClientSetupEvent event) {
        ScreenManager.registerFactory(DEBUG_CONTAINER, DebugContainerScreen::new);
    }

    private void attachItemStackCapability(@Nonnull AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if(!stack.getItem().equals(RavenItems.DEBUGGER))
            return;

        ResourceLocation name = new ResourceLocation(MOD_ID, "debug_handler");
        Capability<DebugHandler> capability = CapabilityDebugHandler.debugHandler();
        DebugHandler instance = new DebugHandler();
        ICapabilityProvider provider = new SingleCapabilityProvider<>(capability, instance);

        event.addCapability(name, provider);
    }
}
