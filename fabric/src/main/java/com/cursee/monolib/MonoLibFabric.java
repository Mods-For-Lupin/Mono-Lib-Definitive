package com.cursee.monolib;

import com.cursee.monolib.impl.registry.ModBlockEntities;
import com.cursee.monolib.impl.registry.ModBlocks;
import com.cursee.monolib.impl.registry.ModEntities;
import com.cursee.monolib.impl.registry.ModItems;
import com.cursee.monolib.impl.registry.ModMenus;
import com.cursee.monolib.impl.registry.ModTabs;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class MonoLibFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    // bind before init
    bind(BuiltInRegistries.BLOCK, ModBlocks::register);
    bind(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModBlockEntities::register);
    bind(BuiltInRegistries.ITEM, ModItems::register);
    bind(BuiltInRegistries.ENTITY_TYPE, ModEntities::register);
    bind(BuiltInRegistries.CREATIVE_MODE_TAB, ModTabs::register);
    bind(BuiltInRegistries.MENU, ModMenus::register);

    MonoLib.init();
  }

  public static <T> void bind(Registry<@NotNull T> registry, Consumer<BiConsumer<T, Identifier>> source) {
    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }
}
