package net.savagedev.paf.commands.friend.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.commands.SubCommand;

import java.util.List;
import java.util.UUID;

public class ListCmd extends SubCommand {
    public ListCmd(PartyAndFriends plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        List<UUID> friends = this.getPlugin().getUserManager().getUser(((ProxiedPlayer) user).getUniqueId()).getFriends();

        int page = 1;

        if (args.length > 1)
            if (this.getPlugin().isInteger(args[1]))
                page = Integer.valueOf(args[1]);

        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.list.header"));

        if (friends.isEmpty()) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.list.no-friends"));
        } else {
            int pageSize = this.getPlugin().getConfig().getInt("messages.friend.list.size");
            for (int i = ((page - 1) * pageSize) > friends.size() ? 0 : (page - 1) * pageSize; i < ((pageSize * page) > friends.size() ? friends.size() : pageSize * page); i++)
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.list.format").replace("%friend%", this.getPlugin().getUserManager().playerIsOnline(friends.get(i)) ? this.getPlugin().getProxy().getPlayer(friends.get(i)).getDisplayName() : this.getPlugin().getStorageUtil().getStorageFile(friends.get(i)).getString("info.username")).replace("%status%", this.getPlugin().getUserManager().playerIsOnline(friends.get(i)) ? !this.getPlugin().getConfig().getSection("messages.friend.list.status").contains(this.getPlugin().getProxy().getPlayer(friends.get(i)).getServer().getInfo().getName()) ? this.getPlugin().getConfig().getString("messages.friend.list.status.unknown") : this.getPlugin().getConfig().getString("messages.friend.list.status." + this.getPlugin().getProxy().getPlayer(friends.get(i)).getServer().getInfo().getName()) : this.getPlugin().getConfig().getString("messages.friend.list.status.offline")));
        }

        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.friend.list.footer"));
    }
}
