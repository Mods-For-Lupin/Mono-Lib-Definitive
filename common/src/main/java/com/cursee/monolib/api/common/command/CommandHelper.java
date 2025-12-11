package com.cursee.monolib.api.common.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.permissions.Permission.HasCommandLevel;
import net.minecraft.server.permissions.PermissionLevel;

/**
 * Adapted from Darkhax-Minecraft's <a href="https://github.com/Darkhax-Minecraft/Bookshelf">Bookshelf</a>
 */
public class CommandHelper {

  /**
   * Creates a command with branching paths that represent the values of an enum.
   *
   * @param parent    The name of the root parent command node.
   * @param enumClass The enum class to use.
   * @param <T>       The type of the enum.
   * @return The newly created command node.
   */
  public static <T extends Enum<T> & IEnumCommand> LiteralArgumentBuilder<CommandSourceStack> buildFromEnum(String parent, Class<T> enumClass) {
    final LiteralArgumentBuilder<CommandSourceStack> parentNode = LiteralArgumentBuilder.literal(parent);
    parentNode.requires(getLowestLevel(enumClass));
    buildFromEnum(parentNode, enumClass);
    return parentNode;
  }

  /**
   * Creates branching command paths that represent the values of an enum.
   *
   * @param parent    The parent node to branch off from.
   * @param enumClass The enum class to use.
   * @param <T>       The type of the enum.
   */
  public static <T extends Enum<T> & IEnumCommand> void buildFromEnum(ArgumentBuilder<CommandSourceStack, ?> parent, Class<T> enumClass) {
    if (!enumClass.isEnum()) {
      throw new IllegalStateException("Class '" + enumClass.getCanonicalName() + "' is not an enum!");
    }
    for (T enumEntry : enumClass.getEnumConstants()) {
      final LiteralArgumentBuilder<CommandSourceStack> literal = LiteralArgumentBuilder.literal(enumEntry.getCommandName());
      literal.requires(source -> source.permissions().hasPermission(new HasCommandLevel(enumEntry.requiredPermissionLevel()))).executes(enumEntry);
      parent.then(literal);
    }
  }

  /**
   * Gets the lowest required permission level for an enum command.
   *
   * @param enumClass The enum class to use.
   * @param <T>       The type of the enum.
   * @return The lowest required permission level for an enum command.
   */
  public static <T extends Enum<T> & IEnumCommand> Predicate<CommandSourceStack> getLowestLevel(Class<T> enumClass) {

    if (!enumClass.isEnum()) {
      throw new IllegalStateException("Class '" + enumClass.getCanonicalName() + "' is not an enum!");
    }

    AtomicReference<PermissionLevel> level = new AtomicReference<>(PermissionLevel.OWNERS);

    for (T enumEntry : enumClass.getEnumConstants()) {
      if (enumEntry.requiredPermissionLevel().id() < level.get().id()) {
        level.set(enumEntry.requiredPermissionLevel());
      }
    }

    return source -> source.permissions().hasPermission(new HasCommandLevel(level.get()));
  }
}
