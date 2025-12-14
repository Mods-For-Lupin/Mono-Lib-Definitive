package com.cursee.monolib;

import com.cursee.monolib.api.common.sailing.Sailing;
import com.cursee.monolib.impl.common.sailing.warden.SailingWarden;
import com.cursee.monolib.platform.Services;
import eu.midnightdust.lib.config.MidnightConfig;
import java.io.File;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonoLib {

  public static final Logger LOG = LoggerFactory.getLogger(Constants.MOD_NAME);

  public static void init() {

    LOG.info("Common mod initialization occurring. MidnightLib by Motschen/TeamMidnightDust is Jar-in-Jarred for configuration and may appear in your mod menu.");

    // initialize our config first before attempting verification
    MidnightConfig.init("monolib", MonoLibConfiguration.class);

    if (MonoLibConfiguration.debugging) {
      LOG.info("Debugging logs enabled.");
    }

    // disable warden inspection base on config
    if (MonoLibConfiguration.verifyMods) {
      LOG.info("MonoLib's common config value 'verifyMods' is set to 'true'. If you are on Windows, MonoLib will inspect your JAR files using ZoneIdentifier lookups.");
      LOG.info("This is a non-invasive process and does not modify any of your files; it is simply a warning in case you downloaded a potentially unsafe file.");
      LOG.info("The JAR inspect will occur now. Check out https://stopmodreposts.org/ for more info on why this feature exists.");
      SailingWarden.process(Services.PLATFORM.getGameDirectoryString() + File.separator + "mods");
    }

    // register our mod to our Sailing API
    Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
    Sailing.register("non_existent_mod", "Non-Existent", "90.0.1", "Genghis", "https://www.youtube.com/@jason13gaming");
  }

  public static Identifier identifier(String path) {

    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}