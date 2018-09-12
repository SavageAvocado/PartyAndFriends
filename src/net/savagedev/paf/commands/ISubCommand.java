package net.savagedev.paf.commands;

import net.md_5.bungee.api.CommandSender;

public interface ISubCommand {
    void execute(CommandSender user, String... args);
}
