package com.cursee.monolib.impl.common.sailing;

import com.cursee.monolib.Constants;
import com.cursee.monolib.MonoLib;
import com.cursee.monolib.MonoLibConfiguration;
import com.cursee.monolib.api.common.sailing.SailingEntry;
import java.util.LinkedHashMap;

public class SailingImpl {

  /// Maps mod display names to their underlying {@link SailingEntry}
  public static final LinkedHashMap<String, SailingEntry> ENTRY_BY_NAME = new LinkedHashMap<>();

  /// Maps schemafied filenames to their respective mod display name.
  private static final LinkedHashMap<String, String> NAME_BY_FILENAME = new LinkedHashMap<>();

  /// Adds a mod to Sailing's internal maps of filename->mod_name and mod_name->sailing_entry
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

  public static LinkedHashMap<String, String> getNameByFilenameMap() {
    return NAME_BY_FILENAME;
  }

  public static LinkedHashMap<String, SailingEntry> getEntryByNameMap() {
    return ENTRY_BY_NAME;
  }
}
