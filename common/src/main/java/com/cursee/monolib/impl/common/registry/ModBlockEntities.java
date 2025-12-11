package com.cursee.monolib.impl.common.registry;

import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

//  public static BlockEntityType<FurnaceBlockEntity> MOD_FURNACE;

  public static void register(BiConsumer<BlockEntityType<?>, Identifier> consumer) {

//    MOD_FURNACE = Services.PLATFORM.createBlockEntityType(FurnaceBlockEntity::new, Blocks.FURNACE);
//    consumer.accept(MOD_FURNACE, ExampleMod.identifier("mod_furnace"));
  }
}
