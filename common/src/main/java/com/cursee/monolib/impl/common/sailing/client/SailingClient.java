package com.cursee.monolib.impl.common.sailing.client;

import com.cursee.monolib.Constants;
import com.cursee.monolib.MonoLib;
import com.cursee.monolib.MonoLibConfiguration;
import com.cursee.monolib.api.common.sailing.Sailing;
import com.cursee.monolib.api.common.sailing.SailingEntry;
import com.cursee.monolib.impl.common.sailing.SailingHelper;
import com.cursee.monolib.impl.common.sailing.SailingImpl;
import com.cursee.monolib.impl.common.sailing.SailingServer;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class SailingClient {

  private static final AtomicBoolean verifiedClient = new AtomicBoolean(false);

  public static boolean shouldInspectJars() {
    boolean willInspect = MonoLibConfiguration.verifyMods;
    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("SailingClient#shouldInspectJars? {}", willInspect);
    }
    return willInspect;
  }

  public static void finishInspection() {
    verifiedClient.set(true);
    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Finished client-side JAR inspection!");
    }
  }

  public static void onPlayerJoinLevel(Player player) {

    if (!MonoLibConfiguration.verifyMods || !shouldInspectJars()) {
      return;
    }

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("A player has joined the level.");
    }

    // can't check against Minecraft instance player as entity joins on server before client
    if (player.getTags().contains(Sailing.CHECKED_TAG)) {
      return;
    }

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("The player is the same as our current reference.");
    }

    verifyModFiles(player);
    player.addTag(Sailing.CHECKED_TAG);

    finishInspection();

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Marking the client's logical side as inspected to avoid double verification.");
    }

    SailingServer.finishInspection();
  }

  private static void verifyModFiles(Player player) {

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Verifying registered mods...");
    }

    List<String> modNamesMissingJars = SailingHelper.getModNamesMissingJarFiles();

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Verifying registered mods...");
      MonoLib.LOG.info("Found registered mods missing files: {}", modNamesMissingJars.toString());
    }

    if (!modNamesMissingJars.isEmpty() && !SailingHelper.preVerifiedCheckFile(verifiedClient)) {

      MonoLib.LOG.info("Messaging client...");

      SailingMessage.sendMessage(player, "Mod(s) from incorrect sources:", ChatFormatting.RED);
      for (String modName : modNamesMissingJars) {
        SailingEntry entry = SailingImpl.getEntryByNameMap().get(modName);
        SailingMessage.sendMessage(player, modName + " by " + entry.modPublisher() + " (Click Here)", ChatFormatting.YELLOW, entry.modURL());
      }

      SailingMessage.sendMessage(player,
          "You a receiving this message because one or more of your mod files has been altered and possibly not downloaded from an original and safe source. Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.",
          ChatFormatting.RED);
      SailingMessage.sendMessage(player, "Click on the name of the mod above to find it's original posting.", ChatFormatting.DARK_GREEN);
      SailingMessage.sendMessage(player, "You won't see this message again in this instance. Thank you for reading.", ChatFormatting.DARK_GREEN);

      MonoLib.LOG.info(
          "You a receiving this message because one or more of your mod files has been altered and possibly not downloaded from an original and safe source. Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.");
      MonoLib.LOG.info("Click on the name of the mod above to find it's original posting.");
      MonoLib.LOG.info("You won't see this message again in this instance. Thank you for reading.");

      SailingHelper.postVerifiedCreateCheckFile(verifiedClient);

      MonoLib.LOG.info("Messaged client and created check file.");
    }
  }
}
