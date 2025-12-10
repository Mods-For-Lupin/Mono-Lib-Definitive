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
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class MonoLibFabric implements ModInitializer {

  public static <T> void bind(Registry<@NotNull T> registry, Consumer<BiConsumer<T, Identifier>> source) {

    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }

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

    CommandRegistrationCallback.EVENT.register(MonoLibFabric::registerCommands);
  }

  public static void registerCommands(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext, CommandSelection commandSelection) {}
}
