package com.cursee.monolib;

import eu.midnightdust.lib.config.MidnightConfig;

public class MonoLibConfiguration extends MidnightConfig {

  public static final String CLIENT = "CLIENT";
  public static final String COMMON = "COMMON";
  public static final String SERVER = "SERVER";

  @Comment(name = "Enables additional logs, may be used by other mods.")
  @Entry(category = COMMON) public static boolean debugging = false;

  @Comment(name = "Enables startup inspection of mod JAR files to verify they are not from a potentially malicious or unverified source.")
  @Entry(category = COMMON) public static boolean verifyMods = true;
}
