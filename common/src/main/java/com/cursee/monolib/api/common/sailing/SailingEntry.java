package com.cursee.monolib.api.common.sailing;

import com.cursee.monolib.impl.common.sailing.SailingImpl;

/**
 * Utilized by {@link SailingImpl} to store data for the registered filename, and the final message to display to the player.
 *
 * @param modID        the identifier of the mod.
 * @param modName      the display name of the mod.
 * @param modVersion   the current version of the mod.
 * @param modPublisher the publisher of the mod
 * @param modURL       the link to be provided in the final message.
 */
public record SailingEntry(String modID, String modName, String modVersion, String modPublisher, String modURL) {

  public static SailingEntry create(String modID, String modName, String modVersion, String modPublisher, String modURL) {
    return new SailingEntry(modID, modName, modVersion, modPublisher, modURL);
  }
}
