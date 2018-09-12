package net.savagedev.paf.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party {
    private List<UUID> members;
    private UUID leader;

    public Party(UUID leader) {
        this.members = new ArrayList<>();
        this.leader = leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public void addMember(UUID member) {
        this.members.add(member);
    }

    public UUID getLeader() {
        return this.leader;
    }

    public List<UUID> getMembers() {
        return this.members;
    }
}
