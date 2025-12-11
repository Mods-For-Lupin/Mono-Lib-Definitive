package com.cursee.monolib.api.common.util;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Adapted from Darkhax-Minecraft's <a href="https://github.com/Darkhax-Minecraft/Bookshelf">Bookshelf</a>
 */
public class TextHelper {

  /**
   * Creates a text component that will copy the value to the players clipboard when they click it.
   *
   * @param text The text to display and copy to the clipboard.
   * @return A component that displays text and copies that text to the clipboard when the player clicks on it.
   */
  public static MutableComponent copyText(String text) {
    return setCopyText(Component.literal(text), text);
  }

  /**
   * Adds a click event to a text component that will copy text to the players clipboard when they click on it.
   *
   * @param component The component to attack the click event to.
   * @param copy      The text to be copied to the clipboard.
   * @return A text component that will copy the text when the player clicks on it.
   */
  public static MutableComponent setCopyText(MutableComponent component, String copy) {
    return component.withStyle(style -> style.withClickEvent(new ClickEvent.CopyToClipboard(copy)));
  }
}
