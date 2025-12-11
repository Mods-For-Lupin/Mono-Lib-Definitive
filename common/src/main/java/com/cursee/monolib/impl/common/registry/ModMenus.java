package com.cursee.monolib.impl.common.registry;

import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {

//  public static MenuType<AnvilMenu> MOD_ANVIL;

  public static void register(BiConsumer<MenuType<?>, Identifier> consumer) {

//    MOD_ANVIL = Services.PLATFORM.createMenuType(AnvilMenu::new, FeatureFlags.VANILLA_SET);
//    consumer.accept(MOD_ANVIL, ExampleMod.identifier("mod_anvil"));
  }
}
