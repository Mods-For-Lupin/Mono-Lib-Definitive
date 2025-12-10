package com.cursee.examplemod;

import com.cursee.examplemod.impl.registry.ModBlockEntities;
import com.cursee.examplemod.impl.registry.ModBlocks;
import com.cursee.examplemod.impl.registry.ModEntities;
import com.cursee.examplemod.impl.registry.ModItems;
import com.cursee.examplemod.impl.registry.ModMenus;
import com.cursee.examplemod.impl.registry.ModTabs;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ExampleModFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    // bind before init
    bind(BuiltInRegistries.BLOCK, ModBlocks::register);
    bind(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModBlockEntities::register);
    bind(BuiltInRegistries.ITEM, ModItems::register);
    bind(BuiltInRegistries.ENTITY_TYPE, ModEntities::register);
    bind(BuiltInRegistries.CREATIVE_MODE_TAB, ModTabs::register);
    bind(BuiltInRegistries.MENU, ModMenus::register);

    ExampleMod.init();
  }

  public static <T> void bind(Registry<@NotNull T> registry, Consumer<BiConsumer<T, Identifier>> source) {
    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }
}
