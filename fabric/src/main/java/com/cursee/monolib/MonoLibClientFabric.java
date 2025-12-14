package com.cursee.monolib;

import com.cursee.monolib.impl.common.sailing.client.SailingClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.player.LocalPlayer;

public class MonoLibClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    MonoLibClient.init();

    ClientEntityEvents.ENTITY_LOAD.register((entity, clientLevel) -> {
      if (entity instanceof LocalPlayer player) {
        SailingClient.onClientJoinLevel(player);
      }
    });
  }
}
