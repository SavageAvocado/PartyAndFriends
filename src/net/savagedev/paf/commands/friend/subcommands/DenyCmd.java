package net.savagedev.paf.commands.friend.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.commands.SubCommand;
import net.savagedev.paf.user.PAFUser;

import java.util.List;
import java.util.UUID;

public class DenyCmd extends SubCommand {
    public DenyCmd(PartyAndFriends plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        PAFUser pafUser = this.getPlugin().getUserManager().getUser(((ProxiedPlayer) user).getUniqueId());

        if (!this.getPlugin().getUserManager().playerHasPlayed(args[1])) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.player-not-found").replace("%username%", args[1]));
            return;
        }

        if (this.getPlugin().getUserManager().playerIsOnline(args[1])) {
            PAFUser pafUserFriend = this.getPlugin().getUserManager().getUser(this.getPlugin().getProxy().getPlayer(args[1]).getUniqueId());

            if (!pafUser.getFriendRequests().contains(this.getPlugin().getProxy().getPlayer(args[1]).getUniqueId())) {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.no-request").replace("%username%", this.getPlugin().getProxy().getPlayer(args[1]).getName()));
                return;
            }

            pafUserFriend.removeFriendRequest(((ProxiedPlayer) user).getUniqueId());
            this.getPlugin().getMessageUtil().message(this.getPlugin().getProxy().getPlayer(args[1]), this.getPlugin().getConfig().getString("messages.friend.deny-requester").replace("%not-friend%", ((ProxiedPlayer) user).getDisplayName()));

            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.deny-requested").replace("%not-friend%", this.getPlugin().getProxy().getPlayer(args[1]).getName()));
            return;
        }

        UUID potentialFriendUuid = this.getPlugin().getUserManager().getUuid(args[1]);
        Configuration storageFile = this.getPlugin().getStorageUtil().getStorageFile(potentialFriendUuid);

        if (!pafUser.getFriendRequests().contains(potentialFriendUuid)) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.no-request").replace("%username%", storageFile.getString("info.username")));
            return;
        }

        List<String> friendRequests = storageFile.getStringList("friend-requests");
        friendRequests.remove(((ProxiedPlayer) user).getUniqueId().toString());

        storageFile.set("friend-requests", friendRequests);
        this.getPlugin().getStorageUtil().saveFile(potentialFriendUuid);

        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.deny-requested").replace("%not-friend%", storageFile.getString("info.username")));
    }
}
