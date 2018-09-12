package net.savagedev.paf.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.savagedev.paf.PartyAndFriends;

public class ReplyCmd extends Command {
    private PartyAndFriends plugin;

    public ReplyCmd(PartyAndFriends plugin, String name, String permission, String... aliases) {
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

        if (!this.plugin.getUserManager().playerIsOnline(this.plugin.getMessageUtil().getLastMessager(((ProxiedPlayer) user).getUniqueId()))) {
            this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.player-not-found").replace("%username%", this.plugin.getStorageUtil().getStorageFile(this.plugin.getMessageUtil().getLastMessager(((ProxiedPlayer) user).getUniqueId())).getString("info.username")));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String word : args)
            message.append(word).append(" ");

        this.plugin.getMessageUtil().setLastMessager(this.plugin.getMessageUtil().getLastMessager(((ProxiedPlayer) user).getUniqueId()), ((ProxiedPlayer) user).getUniqueId());
        this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.private-message.format-sender").replace("%sender%", ((ProxiedPlayer) user).getDisplayName()).replace("%receiver%", this.plugin.getProxy().getPlayer(this.plugin.getMessageUtil().getLastMessager(((ProxiedPlayer) user).getUniqueId())).getDisplayName()).replace("%message%", message.toString().trim()));
        this.plugin.getMessageUtil().message(this.plugin.getProxy().getPlayer(this.plugin.getMessageUtil().getLastMessager(((ProxiedPlayer) user).getUniqueId())), this.plugin.getConfig().getString("messages.private-message.format-receiver").replace("%sender%", ((ProxiedPlayer) user).getDisplayName()).replace("%receiver%", this.plugin.getProxy().getPlayer(this.plugin.getMessageUtil().getLastMessager(((ProxiedPlayer) user).getUniqueId())).getDisplayName()).replace("%message%", message.toString().trim()));
    }
}
