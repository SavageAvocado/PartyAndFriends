package net.savagedev.paf.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.savagedev.paf.PartyAndFriends;

public class MsgCmd extends Command {
    private PartyAndFriends plugin;

    public MsgCmd(PartyAndFriends plugin, String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender user, String[] args) {
        if (!(user instanceof ProxiedPlayer)) {
            this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.not-player"));
            return;
        }

        if (args.length == 0) {
            this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.invalid-arguments").replace("%command%", "msg <player> <message>"));
            return;
        }

        String sendTo = args[0];
        if (!this.plugin.getUserManager().playerIsOnline(sendTo)) {
            this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.player-not-found").replace("%username%", args[0]));
            return;
        }

        if (args.length < 2) {
            this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.invalid-arguments").replace("%command%", "message " + this.plugin.getProxy().getPlayer(sendTo).getDisplayName() + " <message>"));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++)
            message.append(args[i]).append(" ");

        this.plugin.getMessageUtil().setLastMessager(this.plugin.getProxy().getPlayer(sendTo).getUniqueId(), ((ProxiedPlayer) user).getUniqueId());
        this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.private-message.format-sender").replace("%sender%", ((ProxiedPlayer) user).getDisplayName()).replace("%receiver%", this.plugin.getProxy().getPlayer(sendTo).getDisplayName()).replace("%message%", message.toString().trim()));
        this.plugin.getMessageUtil().message(this.plugin.getProxy().getPlayer(sendTo), this.plugin.getConfig().getString("messages.private-message.format-receiver").replace("%sender%", ((ProxiedPlayer) user).getDisplayName()).replace("%receiver%", this.plugin.getProxy().getPlayer(sendTo).getDisplayName()).replace("%message%", message.toString().trim()));
    }
}
