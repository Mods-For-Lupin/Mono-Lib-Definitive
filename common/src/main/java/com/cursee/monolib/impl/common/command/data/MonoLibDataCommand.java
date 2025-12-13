package com.cursee.monolib.impl.common.command.data;

import com.cursee.monolib.Constants;
import com.cursee.monolib.MonoLib;
import com.cursee.monolib.impl.common.command.ModCommands;
import com.cursee.monolib.impl.common.command.data.arg.FormatArgument;
import com.cursee.monolib.impl.common.command.data.arg.SlotArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import java.util.Arrays;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Adapted from Darkhax-Minecraft's <a href="https://github.com/Darkhax-Minecraft/Bookshelf">Bookshelf</a>
 */
public class MonoLibDataCommand {

  private static final String DATA = "data";
  private static final String SLOT = "slot";
  private static final String FORMAT = "format";

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

    if (MonoLib.debugging) {
      MonoLib.LOG.info("Registering \"/monolib data <slot> <format>\" command...");
    }

    var monolib = Commands.literal(Constants.MOD_ID);
    var data = monolib.then(Commands.literal(DATA));

    var slotArg = Commands.argument(SLOT, StringArgumentType.word()).suggests((context, builder) -> {
      Arrays.stream(SlotArgument.values()).forEach(slot -> builder.suggest(slot.getCommandName()));
      return builder.buildFuture();
    });

    var formatArg = Commands.argument(FORMAT, StringArgumentType.word()).suggests((context, builder) -> {
      Arrays.stream(FormatArgument.values()).forEach(format -> builder.suggest(format.getCommandName()));
      return builder.buildFuture();
    });

    var withArgs = data.then(slotArg.then(formatArg.executes(MonoLibDataCommand::execute)));

    dispatcher.register(withArgs);
  }

  public static int execute(CommandContext<CommandSourceStack> context) {

    final CommandSourceStack source = context.getSource();

    if (!(source.getEntity() instanceof Player player)) {
      return ModCommands.FAILURE;
    }

    try {

      // gather supplied arguments as strings
      String slotName = StringArgumentType.getString(context, SLOT);
      String formatName = StringArgumentType.getString(context, FORMAT);

      // get corresponding value of each string
      SlotArgument slotArgument = SlotArgument.valueOf(slotName.toUpperCase());
      FormatArgument formatArgument = FormatArgument.valueOf(formatName.toUpperCase());

      // get selected stack from player
      ItemStack itemStack = slotArgument.getItemFromEntity(player);

      // send formatted text component on success
      source.sendSuccess(() -> formatArgument.getFormat().formatItem(itemStack, source.getLevel()), false);

    } catch (IllegalArgumentException e) {

      if (source.getEntity() instanceof Player erroredPlayer) {
        erroredPlayer.displayClientMessage(Component.literal("Illegal arguments for 'monolib data' command: " + context.getInput()), false);
      }

      return ModCommands.FAILURE;
    }

    return Command.SINGLE_SUCCESS;
  }

  public interface ItemFormat {

    Component formatItem(ItemStack stack, ServerLevel level);
  }
}
