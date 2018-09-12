package net.savagedev.paf.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.savagedev.paf.PartyAndFriends;

import java.util.UUID;

public class QuitE implements Listener {
    private PartyAndFriends plugin;

    public QuitE(PartyAndFriends plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuitE(PlayerDisconnectEvent e) {
        ProxiedPlayer user = e.getPlayer();

        for (UUID friendUuid : this.plugin.getUserManager().getUser(user.getUniqueId()).getFriends()) {
            if (this.plugin.getUserManager().playerIsOnline(friendUuid)) {
                this.plugin.getMessageUtil().message(this.plugin.getProxy().getPlayer(friendUuid), this.plugin.getConfig().getString("messages.friend.quit").replace("%friend%", user.getName()));
            }
        }

        this.plugin.getUserManager().unCacheUser(user.getUniqueId());
    }
}
