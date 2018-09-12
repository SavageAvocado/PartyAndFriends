package net.savagedev.paf.commands.friend.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.commands.SubCommand;
import net.savagedev.paf.user.PAFUser;

import java.util.List;
import java.util.UUID;

public class AddCmd extends SubCommand {
    public AddCmd(PartyAndFriends plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        PAFUser pafUser = this.getPlugin().getUserManager().getUser(((ProxiedPlayer) user).getUniqueId());
        if (args.length < 2) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.invalid-arguments").replace("%command%", "friend add <player>"));
            return;
        }

        if (!this.getPlugin().getUserManager().playerHasPlayed(args[1])) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.player-not-found").replace("%username%", args[1]));
            return;
        }

        if (this.getPlugin().getUserManager().playerIsOnline(args[1])) {
            PAFUser pafUserFriend = this.getPlugin().getUserManager().getUser(this.getPlugin().getProxy().getPlayer(args[1]).getUniqueId());

            if (this.getPlugin().getProxy().getPlayer(args[1]).getUniqueId().equals(((ProxiedPlayer) user).getUniqueId())) {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.cannot-friend-self"));
                return;
            }

            if (pafUser.getFriendRequests().contains(this.getPlugin().getProxy().getPlayer(args[1]).getUniqueId())) {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.add-request-already-received").replace("%potential-friend%", this.getPlugin().getProxy().getPlayer(args[1]).getDisplayName()));
                return;
            }

            if (!pafUserFriend.isAllowFriendRequests()) {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.not-allowing-requests").replace("%player%", this.getPlugin().getProxy().getPlayer(args[1]).getDisplayName()));
                return;
            }

            if (pafUserFriend.getFriends().contains(((ProxiedPlayer) user).getUniqueId())) {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.already-friends").replace("%friend%", this.getPlugin().getProxy().getPlayer(args[1]).getDisplayName()));
                return;
            }

            if (pafUserFriend.getFriendRequests().contains(((ProxiedPlayer) user).getUniqueId())) {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.add-request-already-sent"));
                return;
            }

            pafUserFriend.addFriendRequest(((ProxiedPlayer) user).getUniqueId());
            this.getPlugin().getMessageUtil().message(this.getPlugin().getProxy().getPlayer(args[1]), this.getPlugin().getConfig().getString("messages.friend.add-requested").replace("%potential-friend%", ((ProxiedPlayer) user).getDisplayName()));

            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.add-requester").replace("%potential-friend%", this.getPlugin().getProxy().getPlayer(args[1]).getDisplayName()));
            return;
        }

        UUID potentialFriendUuid = this.getPlugin().getUserManager().getUuid(args[1]);
        Configuration storageFile = this.getPlugin().getStorageUtil().getStorageFile(potentialFriendUuid);

        if (potentialFriendUuid.equals(((ProxiedPlayer) user).getUniqueId())) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.cannot-friend-self"));
            return;
        }

        if (pafUser.getFriendRequests().contains(potentialFriendUuid)) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.add-request-already-received").replace("%potential-friend%", storageFile.getString("info.username")));
            return;
        }

        if (!storageFile.getBoolean("options.allow-friend-requests")) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.not-allowing-requests").replace("%player%", storageFile.getString("info.username")));
            return;
        }

        List<String> friendRequests = storageFile.getStringList("friend-requests");
        List<String> friends = storageFile.getStringList("friends");

        if (friends.contains(((ProxiedPlayer) user).getUniqueId().toString())) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.already-friends").replace("%friend%", storageFile.getString("info.username")));
            return;
        }

        if (friendRequests.contains(((ProxiedPlayer) user).getUniqueId().toString())) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.add-request-already-sent"));
            return;
        }

        friendRequests.add(((ProxiedPlayer) user).getUniqueId().toString());

        storageFile.set("friend-requests", friendRequests);
        this.getPlugin().getStorageUtil().saveFile(potentialFriendUuid);

        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.add-requester").replace("%potential-friend%", storageFile.getString("info.username")));
    }
}
