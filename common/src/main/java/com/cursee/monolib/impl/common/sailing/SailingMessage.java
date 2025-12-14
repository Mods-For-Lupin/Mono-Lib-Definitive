package com.cursee.monolib.impl.common.sailing;

import java.net.URI;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

public class SailingMessage {

  public static void sendMessage(Player player, String message, ChatFormatting colour,
      boolean insertNewLineBeforeMessage, String url) {

    if (message.isEmpty()) {
      return;
    }
    if (insertNewLineBeforeMessage) {
      player.displayClientMessage(Component.literal(""), false);
    }

    MutableComponent mutableMessage = Component.literal(message);
    mutableMessage.withStyle(colour);

    if (message.contains("http") || (url != null && !url.isEmpty())) {
      if (url != null && url.isEmpty()) {
        for (String word : message.split(" ")) {
          if (word.contains("http")) {
            url = word;
            break;
          }
        }
      }

      if (url != null && !url.isEmpty()) {
        try {
          Style clickstyle = mutableMessage.getStyle()
              .withClickEvent(new ClickEvent.OpenUrl(new URI(url)));
          mutableMessage.withStyle(clickstyle);
        } catch (Exception ignored) {
        }
      }
    }

    player.displayClientMessage(mutableMessage, false);
  }

  public static void sendMessage(Player player, String message, ChatFormatting color, String url) {
    sendMessage(player, message, color, false, url);
  }

  public static void sendMessage(Player player, String message, ChatFormatting color) {
    sendMessage(player, message, color, null);
  }
}
