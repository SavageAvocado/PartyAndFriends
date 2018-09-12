package net.savagedev.paf.user;

import net.savagedev.paf.party.Party;

import java.util.List;
import java.util.UUID;

public class PAFUser {
    private boolean allowFriendRequests;
    private boolean allowPartyRequests;
    private List<UUID> friendRequests;
    private List<UUID> friends;
    private Party party;

    PAFUser(boolean allowFriendRequests, boolean allowPartyRequests, List<UUID> friendRequests, List<UUID> friends, Party party) {
        this.allowFriendRequests = allowFriendRequests;
        this.allowPartyRequests = allowPartyRequests;
        this.friendRequests = friendRequests;
        this.friends = friends;
        this.party = party;
    }

    public void setAllowFriendRequests(boolean allowFriendRequests) {
        this.allowFriendRequests = allowFriendRequests;
    }

    public void setAllowPartyRequests(boolean allowPartyRequests) {
        this.allowPartyRequests = allowPartyRequests;
    }

    public void removeFriendRequest(UUID requester) {
        this.friendRequests.remove(requester);
    }

    public void addFriendRequest(UUID requester) {
        this.friendRequests.add(requester);
    }

    public void removeFriend(UUID oldFriend) {
        this.friends.remove(oldFriend);
    }

    public void addFriend(UUID friend) {
        this.friends.add(friend);
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public boolean isAllowFriendRequests() {
        return this.allowFriendRequests;
    }

    public boolean isAllowPartyRequests() {
        return this.allowPartyRequests;
    }

    public List<UUID> getFriendRequests() {
        return this.friendRequests;
    }

    public boolean isInParty() {
        return this.party != null;
    }

    public List<UUID> getFriends() {
        return this.friends;
    }

    public Party getParty() {
        return this.party;
    }
}
