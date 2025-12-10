package com.cursee.monolib;

import com.cursee.monolib.impl.registry.ModBlockEntities;
import com.cursee.monolib.impl.registry.ModBlocks;
import com.cursee.monolib.impl.registry.ModEntities;
import com.cursee.monolib.impl.registry.ModItems;
import com.cursee.monolib.impl.registry.ModMenus;
import com.cursee.monolib.impl.registry.ModTabs;
import com.mojang.brigadier.CommandDispatcher;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

@Mod(Constants.MOD_ID)
public class MonoLibNeoForge {

  public static IEventBus eventBus;

  public MonoLibNeoForge(IEventBus eventBus, ModContainer modContainer, FMLModContainer fmlModContainer, Dist dist) {

    MonoLibNeoForge.eventBus = eventBus;

    // bind before init
    bind(Registries.BLOCK, ModBlocks::register);
    bind(Registries.BLOCK_ENTITY_TYPE, ModBlockEntities::register);
    bind(Registries.ITEM, ModItems::register);
    bind(Registries.ENTITY_TYPE, ModEntities::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);
    bind(Registries.MENU, ModMenus::register);

    MonoLib.init();

    eventBus.addListener(MonoLibNeoForge::registerCommands);

    if (dist == Dist.CLIENT) {
      new MonoLibClientNeoForge();
    }
  }

  public static <T> void bind(ResourceKey<@NotNull Registry<@NotNull T>> registryKey, Consumer<BiConsumer<T, Identifier>> source) {
    eventBus.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }

  public static void registerCommands(RegisterCommandsEvent event) {
    CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
    CommandBuildContext commandBuildContext = event.getBuildContext();
    CommandSelection commandSelection = event.getCommandSelection();
  }
}