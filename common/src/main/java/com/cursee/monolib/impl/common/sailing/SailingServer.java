package com.cursee.monolib.impl.common.sailing;

import com.cursee.monolib.MonoLib;
import com.cursee.monolib.MonoLibConfiguration;
import com.cursee.monolib.api.common.sailing.SailingEntry;
import com.cursee.monolib.platform.Services;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.server.MinecraftServer;

public class SailingServer {

  public static AtomicBoolean verifiedServer = new AtomicBoolean(false);

  public static boolean shouldInspectJars() {
    boolean willInspect = MonoLibConfiguration.verifyMods;
    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("SailingServer#shouldInspectJars? {}", willInspect);
    }
    return willInspect;
  }

  public static void finishInspection() {
    verifiedServer.set(true);
    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Finished logical-side JAR inspection!");
    }
  }

  public static void onServerStarted(MinecraftServer server) {

    // bypass this check on clients, as the same check occurs later as well.
    if (Services.PLATFORM.isClientSide()) {
      return;
    }

    if (!MonoLibConfiguration.verifyMods || !shouldInspectJars()) {
      return;
    }

    verifyModFiles();
    finishInspection();
  }

  private static void verifyModFiles() {
    List<String> MOD_NAMES_MISSING_JAR_FILE = SailingHelper.getModNamesMissingJarFiles();

    if (!MOD_NAMES_MISSING_JAR_FILE.isEmpty() && !SailingHelper.preVerifiedCheckFile(verifiedServer)) {

      MonoLib.LOG.info("Mod(s) from incorrect sources:");
      for (String modName : MOD_NAMES_MISSING_JAR_FILE) {
        SailingEntry entry = SailingImpl.getEntryByNameMap().get(modName);
        MonoLib.LOG.info("{} by {} {}", modName, entry.modPublisher(), entry.modURL());
      }

      MonoLib.LOG.info(
          "You a receiving this message because one or more of your mod files has been altered and possibly not downloaded from an original and safe source. Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.");
      MonoLib.LOG.info("Click on the name of the mod above to find it's original posting.");
      MonoLib.LOG.info("You won't see this message again in this instance. Thank you for reading.");

      SailingHelper.postVerifiedCreateCheckFile(verifiedServer);
    }
  }
}
