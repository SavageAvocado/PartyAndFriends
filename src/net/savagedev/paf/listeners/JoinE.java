package net.savagedev.paf.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.savagedev.paf.PartyAndFriends;

import java.util.UUID;

public class JoinE implements Listener {
    private PartyAndFriends plugin;

    public JoinE(PartyAndFriends plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoinE(PostLoginEvent e) {
        ProxiedPlayer user = e.getPlayer();

        this.plugin.getUserManager().cacheUser(user.getUniqueId());

        for (UUID friendUuid : this.plugin.getUserManager().getUser(user.getUniqueId()).getFriends()) {
            if (this.plugin.getUserManager().playerIsOnline(friendUuid)) {
                this.plugin.getMessageUtil().message(this.plugin.getProxy().getPlayer(friendUuid), this.plugin.getConfig().getString("messages.friend.join").replace("%friend%", user.getName()));
            }
        }
    }
}
