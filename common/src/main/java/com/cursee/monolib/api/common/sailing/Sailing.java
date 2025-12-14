package com.cursee.monolib.api.common.sailing;

import com.cursee.monolib.Constants;
import com.cursee.monolib.impl.common.sailing.SailingImpl;

/// Provides a method for registering mods to MonoLib's internal `Sailing` (anti-piracy) registry, for
/// later inspection during single-player or multi-player server startup. Checks against mods located in
/// the root `mods` folder, ensuring that their filenames match the expected schema.
/// <br />
/// <br />
/// Your release JAR files should follow the schema: <br />
/// `mod_id`-`merged`-`minecraft_version`-`mod_version`.jar <br /><br />
/// Note: `merged` in the filename schema can also be `fabric`, `forge`, or `neoforge`
public class Sailing {

  /**
   * Players tagged with this String will bypass the check made by Sailing
   */
  public static final String CHECKED_TAG = Constants.MOD_ID + ".sailing";

  /**
   * @param modID        the namespace of the mod used in the jar filename
   * @param modName      the display name of the mod for messages
   * @param modVersion   the mod version used in the jar filename
   * @param modPublisher the publisher of the mod for messages
   * @param modURL       the URL to give to the player to download the correct mod file
   * @see SailingImpl
   */
  public static void register(String modID, String modName, String modVersion, String modPublisher,
      String modURL) {
    SailingImpl.register(modID, modName, modVersion, modPublisher, modURL);
  }
}
