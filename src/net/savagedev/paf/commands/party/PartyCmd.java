package net.savagedev.paf.commands.party;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.commands.SubCommand;
import net.savagedev.paf.commands.party.subcommands.*;

import java.util.HashMap;
import java.util.Map;

public class PartyCmd extends Command {
    private Map<String, SubCommand> subCommands;
    private PartyAndFriends plugin;

    public PartyCmd(PartyAndFriends plugin, String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
        this.loadSubCommands();
    }

    private void loadSubCommands() {
        this.subCommands = new HashMap<>();

        this.subCommands.put("accept", new AcceptCmd(this.plugin));
        this.subCommands.put("deny", new DenyCmd(this.plugin));
        this.subCommands.put("help", new HelpCmd(this.plugin));
        this.subCommands.put("invite", new InviteCmd(this.plugin));
        this.subCommands.put("kick", new KickCmd(this.plugin));
        this.subCommands.put("leave", new LeaveCmd(this.plugin));
        this.subCommands.put("list", new ListCmd(this.plugin));
        this.subCommands.put("toggle", new ToggleCmd(this.plugin));
    }

    @Override
    public void execute(CommandSender user, String[] args) {
        if (!(user instanceof ProxiedPlayer)) {
            this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.not-player"));
            return;
        }

        if (args.length == 0 || !this.subCommands.containsKey(args[0].toLowerCase())) {
            this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.invalid-arguments").replace("%command%", "party help"));
            return;
        }

        this.subCommands.get(args[0].toLowerCase()).execute(user, args);
    }
}
