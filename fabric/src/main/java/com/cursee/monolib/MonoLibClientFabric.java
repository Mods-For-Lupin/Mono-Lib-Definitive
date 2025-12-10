package com.cursee.monolib;

import net.fabricmc.api.ClientModInitializer;

public class MonoLibClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    MonoLibClient.init();
  }
}
