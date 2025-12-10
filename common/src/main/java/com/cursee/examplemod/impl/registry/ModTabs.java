package com.cursee.examplemod.impl.registry;

import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;

public class ModTabs {

//  public static CreativeModeTab MOD_TAB;

  public static void register(BiConsumer<CreativeModeTab, Identifier> consumer) {

//    MOD_TAB = Services.PLATFORM.createCreativeTabBuilder()
//        .icon(() -> new ItemStack(Items.STICK))
//        .title(Component.literal(Constants.MOD_NAME))
//        .displayItems(((itemDisplayParameters, output) -> {
//          output.accept(Items.STICK);
//        })).build();
//    consumer.accept(MOD_TAB, ExampleMod.identifier(Constants.MOD_ID));
  }
}
