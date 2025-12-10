package com.cursee.examplemod;

import com.cursee.examplemod.impl.registry.ModBlockEntities;
import com.cursee.examplemod.impl.registry.ModBlocks;
import com.cursee.examplemod.impl.registry.ModEntities;
import com.cursee.examplemod.impl.registry.ModItems;
import com.cursee.examplemod.impl.registry.ModMenus;
import com.cursee.examplemod.impl.registry.ModTabs;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

@Mod(Constants.MOD_ID)
public class ExampleModNeoForge {

  public static IEventBus eventBus;

  public ExampleModNeoForge(IEventBus eventBus, ModContainer modContainer, FMLModContainer fmlModContainer, Dist dist) {

    ExampleModNeoForge.eventBus = eventBus;

    // bind before init
    bind(Registries.BLOCK, ModBlocks::register);
    bind(Registries.BLOCK_ENTITY_TYPE, ModBlockEntities::register);
    bind(Registries.ITEM, ModItems::register);
    bind(Registries.ENTITY_TYPE, ModEntities::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);
    bind(Registries.MENU, ModMenus::register);

    ExampleMod.init();

    if (dist == Dist.CLIENT) {
      new ExampleModClientNeoForge();
    }
  }

  public static <T> void bind(ResourceKey<@NotNull Registry<@NotNull T>> registryKey, Consumer<BiConsumer<T, Identifier>> source) {
    eventBus.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }
}