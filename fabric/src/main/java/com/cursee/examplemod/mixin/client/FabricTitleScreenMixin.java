package com.cursee.examplemod.mixin.client;

import com.cursee.examplemod.ExampleMod;
import com.cursee.examplemod.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class FabricTitleScreenMixin {

  @Inject(at = @At("HEAD"), method = "init()V")
  private void init(CallbackInfo info) {

    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      ExampleMod.LOG.info("This line is printed by an examplemod Fabric mixin!");
      ExampleMod.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
  }
}