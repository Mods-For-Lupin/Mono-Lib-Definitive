package com.cursee.monolib.impl.command.data.hand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public class MonoLibDataHandCommand implements Command<CommandSourceStack> {

  @Override
  public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    return Command.SINGLE_SUCCESS;
  }
}
