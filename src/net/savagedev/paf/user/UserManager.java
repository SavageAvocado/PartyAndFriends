package net.savagedev.paf.user;

import net.md_5.bungee.config.Configuration;
import net.savagedev.paf.PartyAndFriends;
import net.savagedev.paf.party.Party;
import net.savagedev.paf.utils.KeyValueSet;

import java.io.File;
import java.util.*;

public class UserManager {
    private Map<UUID, PAFUser> users;
    private PartyAndFriends plugin;

    public UserManager(PartyAndFriends plugin) {
        this.plugin = plugin;
        this.init();
    }

    private void init() {
        this.users = new HashMap<>();
    }

    public void cacheUser(UUID uuid) {
        if (!this.plugin.getStorageUtil().fileExists(uuid)) {
            this.plugin.getStorageUtil().createFile(uuid);
            this.plugin.getStorageUtil().setDefaults(uuid,
                    new KeyValueSet<>("info.username", this.plugin.getProxy().getPlayer(uuid).getDisplayName()),
                    new KeyValueSet<>("options.allow-friend-requests", true),
                    new KeyValueSet<>("options.allow-party-requests", true),
                    new KeyValueSet<>("friend-requests", new ArrayList<>()),
                    new KeyValueSet<>("friends", new ArrayList<String>())
            );

            this.plugin.getStorageUtil().saveFile(uuid);
            this.plugin.getStorageUtil().reloadFile(uuid);

            this.users.put(uuid, new PAFUser(true, true, new ArrayList<>(), new ArrayList<>(), null));
            return;
        }

        Configuration storageFile = this.plugin.getStorageUtil().getStorageFile(uuid);

        boolean allowFriendRequests = storageFile.getBoolean("options.allow-friend-requests");
        boolean allowPartyRequests = storageFile.getBoolean("options.allow-party-requests");
        List<UUID> friendRequests = new ArrayList<>();
        List<UUID> friends = new ArrayList<>();
        Party party = null;

        if (storageFile.get("party") != null) {
            party = (Party) storageFile.get("party");
            storageFile.set("party", null);
        }

        for (String requesterUuid : storageFile.getStringList("friend-requests"))
            friendRequests.add(UUID.fromString(requesterUuid));

        for (String friendUuid : storageFile.getStringList("friends"))
            friends.add(UUID.fromString(friendUuid));

        this.users.put(uuid, new PAFUser(allowFriendRequests, allowPartyRequests, friendRequests, friends, party));
    }

    public void unCacheUser(UUID uuid) {
        Configuration storageFile = this.plugin.getStorageUtil().getStorageFile(uuid);
        List<String> friendRequests = new ArrayList<>();
        List<String> friends = new ArrayList<>();
        PAFUser user = this.getUser(uuid);

        for (UUID friendRequestUuid : user.getFriendRequests())
            friendRequests.add(friendRequestUuid.toString());

        for (UUID friendUuid : user.getFriends())
            friends.add(friendUuid.toString());

        storageFile.set("info.username", this.plugin.getProxy().getPlayer(uuid).getDisplayName());
        storageFile.set("options.allow-friend-requests", user.isAllowFriendRequests());
        storageFile.set("options.allow-party-requests", user.isAllowPartyRequests());
        storageFile.set("friend-requests", friendRequests);
        storageFile.set("friends", friends);

        if (user.isInParty())
            storageFile.set("party", user.getParty());

        this.plugin.getMessageUtil().unCahceLastMessager(uuid);
        this.plugin.getStorageUtil().unCacheFile(uuid);
        this.users.remove(uuid, this.users.get(uuid));
    }

    public UUID getUuid(String username) {
        File path = new File(this.plugin.getDataFolder(), "storage");
        File[] files = path.listFiles();

        assert files != null;
        for (File file : files)
            if (this.plugin.getStorageUtil().getStorageFile(UUID.fromString(file.getName().replace(".yml", ""))).getString("info.username").equalsIgnoreCase(username))
                return UUID.fromString(file.getName().replace(".yml", ""));

        return null;
    }

    public boolean playerHasPlayed(String username) {
        return this.playerIsOnline(username) || this.getUuid(username) != null;
    }

    public boolean playerIsOnline(UUID uuid) {
        return this.plugin.getProxy().getPlayer(uuid) != null;
    }

    public boolean playerIsOnline(String username) {
        return this.plugin.getProxy().getPlayer(username) != null;
    }

    public PAFUser getUser(UUID uuid) {
        return this.users.get(uuid);
    }
}
