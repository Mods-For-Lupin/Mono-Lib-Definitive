package com.cursee.monolib.impl.common.sailing;

import com.cursee.monolib.Constants;
import com.cursee.monolib.MonoLib;
import com.cursee.monolib.MonoLibConfiguration;
import com.cursee.monolib.api.common.sailing.SailingEntry;
import com.cursee.monolib.platform.Services;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class SailingImpl {

  /// Maps mod display names to their underlying {@link SailingEntry}
  public static final LinkedHashMap<String, SailingEntry> ENTRY_BY_NAME = new LinkedHashMap<>();
  /// Maps schemafied filenames to their respective mod display name.
  private static final LinkedHashMap<String, String> NAME_BY_FILENAME = new LinkedHashMap<>();
  /// The instance is assumed to not be inspected until the inspection completes
  private static boolean inspected = false;

  /// Adds a mod to Sailing's internal registry
  public static void register(String modID, String modName, String modVersion, String modPublisher, String modURL) {

    String schemafied = createMergedFilename(modID, modVersion);
    SailingEntry entry = SailingEntry.create(modID, modName, modVersion, modPublisher, modURL);

    NAME_BY_FILENAME.put(schemafied, modName);
    ENTRY_BY_NAME.put(modName, entry);

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Registered new SailingEntry to Sailing API: {}", entry);
      MonoLib.LOG.info("Current Entries: ");
      MonoLib.LOG.info("ENTRY_BY_NAME: {}", ENTRY_BY_NAME);
      MonoLib.LOG.info("NAME_BY_FILENAME: {}", NAME_BY_FILENAME);
    }
  }

  /// @return examples: `monolib-merged-1.21.11-1.0.0.jar`, `monolib-fabric-26-2.0.0.jar`, etc.
  private static String createMergedFilename(String modID, String modVersion) {
    return modID + "-merged-" + Constants.GAME_VERSION + "-" + modVersion + ".jar";
  }

  public static boolean shouldInspectJars() {

    boolean willInspect = MonoLibConfiguration.verifyMods && !inspected;

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("SailingImpl#shouldInspectJars? {}", willInspect);
    }

    return willInspect;
  }

  public static void finishInspection() {

    SailingImpl.inspected = true;

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Finished JAR inspection!");
    }
  }

  public static boolean preVerificationProcess() {

    String checkFilePathString =
        Services.PLATFORM.getGameDirectoryString() + File.separator + "config" + File.separator + Constants.MOD_ID + File.separator + "checked.txt"; // game_directory/config/monolib
    File checkFile = new File(checkFilePathString);

    if (checkFile.exists()) {
      SailingImpl.inspected = true;

      if (MonoLibConfiguration.debugging) {
        MonoLib.LOG.info("Check file found, skipping JAR inspection.");
      }
    }

    return SailingImpl.inspected;
  }

  public static void postVerificationProcess() {

    SailingImpl.inspected = true;

    // check root game directory config file
    String alternativePath = Services.PLATFORM.getGameDirectoryString();
    alternativePath = alternativePath + File.separator + "config" + File.separator + Constants.MOD_ID; // game_directory/config/monolib
    if (new File(alternativePath + File.separator + "checked.txt").isFile()) {
      return; // already exists
    }

    File alternativeDirectory = new File(alternativePath);
    if (!alternativeDirectory.mkdirs()) {
      return;
    }
    try (PrintWriter writer = new PrintWriter(alternativePath + File.separator + "checked.txt", StandardCharsets.UTF_8)) {
      writer.println("# Please check out https://stopmodreposts.org/ for more information on why this feature exists.");
      writer.println("checked=true");
    } catch (Exception ignored) {
    }
  }

  public static List<String> getModNamesMissingJarFile() {

    final List<String> INSTALLED_MOD_FILENAMES = getInstalledModFilenames();
    List<String> MOD_NAMES_MISSING_JAR_FILE = new ArrayList<String>();

    for (String filename : SailingImpl.NAME_BY_FILENAME.keySet()) {

      final boolean CONTAINS_MERGED = INSTALLED_MOD_FILENAMES.contains(filename);
      final boolean CONTAINS_FABRIC = INSTALLED_MOD_FILENAMES.contains(filename.replace("-merged-", "-fabric-"));
      final boolean CONTAINS_FORGE = INSTALLED_MOD_FILENAMES.contains(filename.replace("-merged-", "-forge-"));
      final boolean CONTAINS_NEOFORGE = INSTALLED_MOD_FILENAMES.contains(filename.replace("-merged-", "-neoforge-"));

      if (!INSTALLED_MOD_FILENAMES.isEmpty() && !(CONTAINS_MERGED || CONTAINS_FABRIC || CONTAINS_FORGE || CONTAINS_NEOFORGE) && SailingImpl.NAME_BY_FILENAME.containsKey(filename)) {
        MOD_NAMES_MISSING_JAR_FILE.add(SailingImpl.NAME_BY_FILENAME.get(filename));
      }
    }

    if (!MOD_NAMES_MISSING_JAR_FILE.isEmpty()) {
      Collections.sort(MOD_NAMES_MISSING_JAR_FILE);
    }

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Registered mod names with missing JAR files: {}", MOD_NAMES_MISSING_JAR_FILE);
    }

    return MOD_NAMES_MISSING_JAR_FILE;
  }

  private static List<String> getInstalledModFilenames() {

    List<String> INSTALLED_MOD_FILENAMES = new ArrayList<String>();

    File MOD_DIRECTORY = new File(Services.PLATFORM.getGameDirectoryString() + File.separator + "mods");
    File[] DISCOVERED_FILES = MOD_DIRECTORY.listFiles();
    File VERSIONED_MOD_DIRECTORY = new File(Services.PLATFORM.getGameDirectoryString() + File.separator + "mods" + File.separator + Constants.GAME_VERSION);
    File[] DISCOVERED_VERSIONED_FILES = VERSIONED_MOD_DIRECTORY.listFiles();

    if (DISCOVERED_FILES == null && DISCOVERED_VERSIONED_FILES == null) {
      return new ArrayList<String>();
    }

    for (File file : ArrayUtils.addAll(DISCOVERED_FILES, DISCOVERED_VERSIONED_FILES)) {
      if (file.isFile()) {
        String filename = file.getName().replaceAll(" +\\([0-9]+\\)", "");
        INSTALLED_MOD_FILENAMES.add(filename);
      }
    }

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Discovered JAR files: {}", INSTALLED_MOD_FILENAMES);
    }

    return INSTALLED_MOD_FILENAMES;
  }
}
