package com.cursee.examplemod.mixin.client;

import com.cursee.examplemod.ExampleMod;
import com.cursee.examplemod.platform.Services;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

  @Inject(at = @At("TAIL"), method = "<init>")
  private void init(CallbackInfo info) {

    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      ExampleMod.LOG.info("This line is printed by an examplemod common mixin!");
      ExampleMod.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
  }
}