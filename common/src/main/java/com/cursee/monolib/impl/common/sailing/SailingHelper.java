package com.cursee.monolib.impl.common.sailing;

import com.cursee.monolib.Constants;
import com.cursee.monolib.platform.Services;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.ArrayUtils;

public class SailingHelper {

  public static boolean preVerifiedCheckFile(AtomicBoolean atomicBoolean) {

    String checkFilePathString =
        Services.PLATFORM.getGameDirectoryString() + File.separator + "config" + File.separator + Constants.MOD_ID + File.separator + "checked.txt"; // game_directory/config/monolib
    File checkFile = new File(checkFilePathString);

    if (checkFile.exists()) {
      atomicBoolean.set(true);
    }

    return atomicBoolean.get();
  }

  public static void postVerifiedCreateCheckFile(AtomicBoolean atomicBoolean) {

    atomicBoolean.set(true);

    String monolibConfigDirPath = Services.PLATFORM.getConfigDirectory().resolve(Constants.MOD_ID).toString();

    if (new File(monolibConfigDirPath + File.separator + "checked.txt").isFile()) {
      return; // already exists
    }

    File monolibConfigDirectory = new File(monolibConfigDirPath);

    if (!monolibConfigDirectory.mkdirs()) {
      return;
    }

    try (PrintWriter writer = new PrintWriter(monolibConfigDirPath + File.separator + "checked.txt", StandardCharsets.UTF_8)) {
      writer.println("# Please check out https://stopmodreposts.org/ for more information on why this feature exists.");
      writer.println("checked=true");
    } catch (Exception ignored) {
    }
  }

  public static List<String> getModNamesMissingJarFiles() {

    final List<String> installedModFilenames = getInstalledModFilenames();
    List<String> modNamesMissingJarFiles = new ArrayList<String>();

    // iterate over filename keys
    for (String filename : SailingImpl.getNameByFilenameMap().keySet()) {

      final boolean hasMergedName = installedModFilenames.contains(filename);
      final boolean hasFabricName = installedModFilenames.contains(filename.replace("-merged-", "-fabric-"));
      final boolean hasForgeName = installedModFilenames.contains(filename.replace("-merged-", "-forge-"));
      final boolean hasNeoForgeName = installedModFilenames.contains(filename.replace("-merged-", "-neoforge-"));

      if (!installedModFilenames.isEmpty() && !(hasMergedName || hasFabricName || hasForgeName || hasNeoForgeName) && SailingImpl.getNameByFilenameMap().containsKey(filename)) {
        modNamesMissingJarFiles.add(SailingImpl.getNameByFilenameMap().get(filename));
      }
    }

    if (!modNamesMissingJarFiles.isEmpty()) {
      Collections.sort(modNamesMissingJarFiles);
    }

    return modNamesMissingJarFiles;
  }

  private static List<String> getInstalledModFilenames() {

    final List<String> modFilenames = new ArrayList<>();

    File modsDirectory = new File(Services.PLATFORM.getGameDirectory().resolve("mods").toUri());
    File[] discovered = modsDirectory.listFiles();

    File versionedModsDirectory = new File(Services.PLATFORM.getGameDirectory().resolve("mods").toUri());
    File[] versionedDiscovered = versionedModsDirectory.listFiles();

    if (discovered == null || versionedDiscovered == null) {
      return modFilenames;
    }

    for (File file : ArrayUtils.addAll(discovered)) {
      if (file.isFile()) {
        String filename = file.getName(); //.replaceAll(" +\\([0-9]+\\)", "");
        modFilenames.add(filename);
      }
    }

    return modFilenames;
  }
}
