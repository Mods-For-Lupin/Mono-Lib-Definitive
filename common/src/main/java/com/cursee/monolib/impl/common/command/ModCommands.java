package com.cursee.monolib.impl.common.command;

import com.cursee.monolib.MonoLib;
import com.cursee.monolib.MonoLibConfiguration;
import com.cursee.monolib.impl.common.command.data.MonoLibDataCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;

public class ModCommands {

  public static final int FAILURE = 0;

  public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext, CommandSelection commandSelection) {

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Registering MonoLib commands...");
    }

    MonoLibDataCommand.register(commandDispatcher);

    if (MonoLibConfiguration.debugging) {
      MonoLib.LOG.info("Registered all MonoLib commands.");
    }
  }
}
