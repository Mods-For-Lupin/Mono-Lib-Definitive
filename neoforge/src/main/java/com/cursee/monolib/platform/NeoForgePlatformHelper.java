package com.cursee.monolib.platform;

import com.cursee.monolib.platform.services.IPlatformHelper;
import java.nio.file.Path;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {

    return "NeoForge";
  }

  @Override
  public boolean isModLoaded(String modId) {

    return ModList.get().isLoaded(modId);
  }

  @Override
  public boolean isDevelopmentEnvironment() {

    return !FMLLoader.getCurrent().isProduction();
  }

  @Override
  public Path getGameDirectory() {

    return FMLLoader.getCurrent().getGameDir();
  }

  @Override
  public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> constructor, Block... validBlocks) {

    return new BlockEntityType<T>(constructor::apply, validBlocks);
  }

  @Override
  public <T extends Entity> EntityType<T> createEntityType(BiFunction<EntityType<T>, Level, T> constructor, MobCategory mobCategory, ResourceKey<EntityType<?>> resourceKey) {

    return EntityType.Builder.<T>of(constructor::apply, mobCategory).build(resourceKey);
  }

  @Override
  public <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> constructor, FeatureFlagSet requiredFeatures) {

    return new MenuType<T>(constructor::apply, requiredFeatures);
  }

  @Override
  public Builder createCreativeTabBuilder() {

    return CreativeModeTab.builder();
  }

  @Override
  public boolean isClientSide() {
    return FMLLoader.getCurrent().getDist() == Dist.CLIENT;
  }
}