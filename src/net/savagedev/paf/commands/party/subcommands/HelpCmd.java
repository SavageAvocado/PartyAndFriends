package net.savagedev.paf.commands.party.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.commands.SubCommand;

public class HelpCmd extends SubCommand {
    public HelpCmd(PartyAndFriends plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        for (String line : this.getPlugin().getConfig().getStringList("messages.party.help"))
            this.getPlugin().getMessageUtil().message(user, line);
    }
}
