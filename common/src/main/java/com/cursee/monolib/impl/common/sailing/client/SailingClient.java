package com.cursee.monolib.impl.common.sailing.client;

import com.cursee.monolib.MonoLib;
import com.cursee.monolib.api.common.sailing.Sailing;
import com.cursee.monolib.api.common.sailing.SailingEntry;
import com.cursee.monolib.impl.common.sailing.SailingImpl;
import com.cursee.monolib.impl.common.sailing.SailingMessage;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class SailingClient {

  public static void onClientJoinLevel(LocalPlayer player) {

    // ignore other players synchronized to the client level, and ensure we should continue
    if (player != Minecraft.getInstance().player || !SailingImpl.shouldInspectJars()) {
      return;
    }

    // ignore already checked player (player has previously joined this level, tag restored from save data)
    if (player.getTags().contains(Sailing.CHECKED_TAG)) {
      return;
    }

    verifyModFilesAndNotify(player);
  }

  private static void verifyModFilesAndNotify(LocalPlayer player) {

    List<String> MOD_NAMES_MISSING_JAR_FILE = SailingImpl.getModNamesMissingJarFile();

    if (!MOD_NAMES_MISSING_JAR_FILE.isEmpty() && !SailingImpl.preVerificationProcess()) {

      SailingMessage.sendMessage(player, "Mod(s) from incorrect sources:", ChatFormatting.RED);
      for (String modName : MOD_NAMES_MISSING_JAR_FILE) {
        SailingEntry entry = SailingImpl.ENTRY_BY_NAME.get(modName);
        SailingMessage.sendMessage(player,
            modName + " by " + entry.modPublisher() + " (Click Here)", ChatFormatting.YELLOW,
            entry.modURL());
      }

      SailingMessage.sendMessage(player,
          "You a receiving this message because one or more of your mod files has been altered and possibly not downloaded from an original and safe source. Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.",
          ChatFormatting.RED);
      SailingMessage.sendMessage(player,
          "Click on the name of the mod above to find it's original posting.",
          ChatFormatting.DARK_GREEN);
      SailingMessage.sendMessage(player,
          "You won't see this message again in this instance. Thank you for reading.",
          ChatFormatting.DARK_GREEN);

      MonoLib.LOG.info(
          "You a receiving this message because one or more of your mod files has been altered and possibly not downloaded from an original and safe source. Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.");
      MonoLib.LOG.info("Click on the name of the mod above to find it's original posting.");
      MonoLib.LOG.info(
          "You won't see this message again in this instance. Thank you for reading.");

      SailingImpl.postVerificationProcess();
    }

    player.addTag(Sailing.CHECKED_TAG);
    SailingImpl.finishInspection();
  }
}
