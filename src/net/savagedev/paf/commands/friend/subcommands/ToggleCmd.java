package net.savagedev.paf.commands.friend.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.commands.SubCommand;
import net.savagedev.paf.user.PAFUser;

public class ToggleCmd extends SubCommand {
    public ToggleCmd(PartyAndFriends plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        PAFUser pafUser = this.getPlugin().getUserManager().getUser(((ProxiedPlayer) user).getUniqueId());

        pafUser.setAllowFriendRequests(!pafUser.isAllowFriendRequests());
        this.getPlugin().getMessageUtil().message(user, pafUser.isAllowFriendRequests() ? this.getPlugin().getConfig().getString("messages.friend.toggle-enable") : this.getPlugin().getConfig().getString("messages.friend.toggle-disable"));
    }
}
