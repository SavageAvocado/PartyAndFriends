package net.savagedev.paf.commands.party.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.commands.SubCommand;
import net.savagedev.paf.user.PAFUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListCmd extends SubCommand {
    public ListCmd(PartyAndFriends plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        PAFUser pafUser = this.getPlugin().getUserManager().getUser(((ProxiedPlayer) user).getUniqueId());

        if (!pafUser.isInParty()) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.party.not-in-a-party"));
            return;
        }

        List<String> members = new ArrayList<>();
        for (UUID uuid : pafUser.getParty().getMembers())
            members.add(this.getPlugin().getUserManager().playerIsOnline(uuid) ? "&6" + this.getPlugin().getProxy().getPlayer(uuid).getDisplayName() : "&c" + this.getPlugin().getStorageUtil().getStorageFile(uuid).getString("info.username"));

        for (String line : this.getPlugin().getConfig().getStringList("messages.party.list"))
            this.getPlugin().getMessageUtil().message(user, line.replace("%party-leader%", this.getPlugin().getProxy().getPlayer(pafUser.getParty().getLeader()).getDisplayName()).replace("%party-members%", this.getPlugin().getMessageUtil().listToString(members, "&7, ")));
    }
}
