package c4q.nyc.ramonaharrison.meshnyc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramona Harrison
 * on 8/2/15.
 */
public class GroupLeader extends GroupNode {

    private String groupLeaderAddress;
    private List<GroupMember> groupMembers;

    public GroupLeader(String groupLeaderAddress) {
        this.groupLeaderAddress = groupLeaderAddress;
        this.groupMembers = new ArrayList<>();
    }

    public String getGroupLeaderAddress() {
        return groupLeaderAddress;
    }

    public void setGroupLeaderAddress(String groupLeaderAddress) {
        this.groupLeaderAddress = groupLeaderAddress;
    }

    public List<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public void addGroupMember(GroupMember groupMember) {
        this.groupMembers.add(groupMember);
    }
}
