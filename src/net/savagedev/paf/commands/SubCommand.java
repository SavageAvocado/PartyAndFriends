package net.savagedev.paf.commands;

import net.savagedev.paf.PartyAndFriends;

public abstract class SubCommand implements ISubCommand {
    private PartyAndFriends plugin;

    public SubCommand(PartyAndFriends plugin) {
        this.plugin = plugin;
    }

    public PartyAndFriends getPlugin() {
        return this.plugin;
    }
}
