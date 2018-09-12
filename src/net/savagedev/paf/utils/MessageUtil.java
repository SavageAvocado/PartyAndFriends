package net.savagedev.paf.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.user.PAFUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageUtil {
    private Map<UUID, UUID> lastMessager;
    private PartyAndFriends plugin;

    public MessageUtil(PartyAndFriends plugin) {
        this.lastMessager = new HashMap<>();
        this.plugin = plugin;
    }

    public void message(CommandSender user, String message) {
        user.sendMessage(new TextComponent(this.color(message)));
    }

    public void message(CommandSender user, TextComponent... textComponents) {
        user.sendMessage(textComponents);
    }

    public void messageParty(ProxiedPlayer user, String message) {
        PAFUser pafUser = this.plugin.getUserManager().getUser(user.getUniqueId());
        for (UUID uuid : pafUser.getParty().getMembers()) {
            if (this.plugin.getProxy().getPlayer(uuid) == null)
                continue;

            if (uuid.equals(user.getUniqueId()))
                continue;

            this.message(this.plugin.getProxy().getPlayer(uuid), message);
        }
    }

    public void unCahceLastMessager(UUID user) {
        this.lastMessager.remove(user, this.lastMessager.get(user));
    }

    public void setLastMessager(UUID user, UUID messager) {
        if (this.lastMessager.containsKey(user)) {
            this.lastMessager.replace(user, this.lastMessager.get(user), messager);
            return;
        }

        this.lastMessager.put(user, messager);
    }

    public UUID getLastMessager(UUID user) {
        return this.lastMessager.get(user);
    }

    public String listToString(List<String> messages, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            if (!this.listHasNext(messages, i)) {
                stringBuilder.append(messages.get(i));
                return stringBuilder.toString().trim();
            }

            stringBuilder.append(messages.get(i)).append(separator);
        }

        return stringBuilder.toString().trim();
    }

    private boolean listHasNext(List<String> list, int currentIndex) {
        try {
            list.get(currentIndex + 1);
            return true;
        } catch (IndexOutOfBoundsException ignored) {
            return false;
        }
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
