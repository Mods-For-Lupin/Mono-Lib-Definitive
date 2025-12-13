package com.cursee.monolib;

import com.cursee.monolib.impl.common.config.FileBackedSimpleConfig;
import com.cursee.monolib.platform.Services;
import java.io.File;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonoLib {

  public static final Logger LOG = LoggerFactory.getLogger(Constants.MOD_NAME);

  public static boolean debugging = false;

  public static void init() {

    // create a file-backed silent config object with our class loader, the absolute location of the final file,
    // and reference to our internal resource
    FileBackedSimpleConfig config = new FileBackedSimpleConfig(MonoLib.class.getClassLoader(),
        Services.PLATFORM.getConfigDirectory() + File.separator + "monolib-common.toml",
        "assets/config/monolib-common.toml");

    debugging = config.getBoolean("debugging", debugging);

    if (debugging) {
      LOG.info("MonoLib debugging is enabled; additional logs will be written.");
    }
  }

  public static Identifier identifier(String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}