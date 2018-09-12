package net.savagedev.paf.commands.friend.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.commands.SubCommand;

import java.util.UUID;

public class RequestsCmd extends SubCommand {
    public RequestsCmd(PartyAndFriends plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.requests.header"));

        if (this.getPlugin().getUserManager().getUser(((ProxiedPlayer) user).getUniqueId()).getFriendRequests().isEmpty())
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.requests.no-requests"));
        else
            for (UUID uuid : this.getPlugin().getUserManager().getUser(((ProxiedPlayer) user).getUniqueId()).getFriendRequests())
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.requests.format").replace("%potential-friend%", this.getPlugin().getUserManager().playerIsOnline(uuid) ? this.getPlugin().getProxy().getPlayer(uuid).getDisplayName() : this.getPlugin().getStorageUtil().getStorageFile(uuid).getString("info.username")));

        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.requests.footer"));
    }
}
