package com.cursee.monolib.impl.common.registry;

import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class ModEntities {

//  public static EntityType<Armadillo> MOD_ARMADILLO;

  public static void register(BiConsumer<EntityType<?>, Identifier> consumer) {

//    MOD_ARMADILLO = Services.PLATFORM.createEntityType(Armadillo::new, MobCategory.MISC, ResourceKey.create(Registries.ENTITY_TYPE, ExampleMod.identifier("mod_armadillo")));
//    consumer.accept(MOD_ARMADILLO, ExampleMod.identifier("mod_armadillo"));
  }
}
