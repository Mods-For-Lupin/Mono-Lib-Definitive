package com.cursee.monolib.impl.common.sailing;

import com.cursee.monolib.MonoLib;
import com.cursee.monolib.api.common.sailing.SailingEntry;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public class SailingServer {

  public static void onServerStarted(MinecraftServer server) {

    if (!SailingImpl.shouldInspectJars()) {
      return;
    }

    verifyModFiles();
  }

  private static void verifyModFiles() {

    List<String> MOD_NAMES_MISSING_JAR_FILE = SailingImpl.getModNamesMissingJarFile();

    // if a registered mod is missing it's expected jar file, and pre-verification fails
    if (!MOD_NAMES_MISSING_JAR_FILE.isEmpty() && !SailingImpl.preVerificationProcess()) {

      MonoLib.LOG.info("Mod(s) from incorrect sources:");
      for (String modName : MOD_NAMES_MISSING_JAR_FILE) {
        SailingEntry entry = SailingImpl.ENTRY_BY_NAME.get(modName);
        MonoLib.LOG.info("{} by {} {}", modName, entry.modPublisher(), entry.modURL());
      }

      MonoLib.LOG.info(
          "You a receiving this message because one or more of your mod files has been altered and possibly not downloaded from an original and safe source. Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.");
      MonoLib.LOG.info("Click on the URL of the mod above to find it's original posting.");
      MonoLib.LOG.info(
          "You won't see this message again in this instance. Thank you for reading.");

      SailingImpl.postVerificationProcess();
    }

    SailingImpl.finishInspection();
  }
}
