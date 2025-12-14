package com.cursee.monolib;

import com.cursee.monolib.impl.common.sailing.client.SailingClient;
import java.util.function.Consumer;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class MonoLibClientNeoForge {

  public MonoLibClientNeoForge() {
    MonoLibClient.init();

    NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
      if (event.getEntity() instanceof LocalPlayer player) {
        SailingClient.onClientJoinLevel(player);
      }
    });
  }
}
