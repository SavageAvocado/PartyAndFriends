package net.savagedev.paf;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.savagedev.paf.commands.MsgCmd;
import net.savagedev.paf.commands.ReplyCmd;
import net.savagedev.paf.commands.friend.FriendCmd;
import net.savagedev.paf.commands.party.PartyCmd;
import net.savagedev.paf.listeners.JoinE;
import net.savagedev.paf.listeners.QuitE;
import net.savagedev.paf.user.UserManager;
import net.savagedev.paf.utils.MessageUtil;
import net.savagedev.paf.utils.StorageUtil;

public class PartyAndFriends extends Plugin {
    private PluginManager pluginManager;
    private UserManager userManager;
    private MessageUtil messageUtil;
    private StorageUtil storageUtil;

    @Override
    public void onEnable() {
        this.loadUtils();
        this.loadConfig();
        this.loadCommands();
        this.loadListeners();
    }

    private void loadUtils() {
        this.pluginManager = this.getProxy().getPluginManager();
        this.storageUtil = new StorageUtil(this);
        this.userManager = new UserManager(this);
        this.messageUtil = new MessageUtil(this);
    }

    private void loadConfig() {
        this.storageUtil.saveDefaultConfig();
    }

    private void loadCommands() {
        this.pluginManager.registerCommand(this, new MsgCmd(this, "message", "", "whisper", "tell", "msg", "pm", "t", "m", "w"));
        this.pluginManager.registerCommand(this, new FriendCmd(this, "friend", "", "fr", "f"));
        this.pluginManager.registerCommand(this, new PartyCmd(this, "party", "", "p"));
        this.pluginManager.registerCommand(this, new ReplyCmd(this, "reply", "", "r"));
    }

    private void loadListeners() {
        this.pluginManager.registerListener(this, new JoinE(this));
        this.pluginManager.registerListener(this, new QuitE(this));
    }

    public void reloadConfig() {
        this.storageUtil.reload();
    }

    public void saveConfig() {
        this.storageUtil.save();
    }

    public Configuration getConfig() {
        return this.storageUtil.getConfiguration();
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public StorageUtil getStorageUtil() {
        return this.storageUtil;
    }

    public MessageUtil getMessageUtil() {
        return this.messageUtil;
    }

    public boolean isInteger(String potentialInteger) {
        try {
            Integer.parseInt(potentialInteger);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
