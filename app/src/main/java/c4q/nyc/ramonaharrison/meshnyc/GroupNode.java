package c4q.nyc.ramonaharrison.meshnyc;

import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Created by Ramona Harrison
 * on 8/2/15.
 */
public abstract class GroupNode {

    private WifiP2pDevice device;
    private SQLiteDatabase database;

    private final int memberPortIn = 8889;
    private final int leaderPortIn = 8888;


    public void sendMessage(GroupNode node, String message) {

        if (node instanceof GroupLeader) {
            // sending a message to the group leader
            (new Thread(new ClientSocket(((GroupLeader) node).getGroupLeaderAddress(), leaderPortIn, message))).start();
        } else {
            // sending a message
            (new Thread(new ClientSocket(((GroupMember) node).getGroupMemberAddress(), memberPortIn, message))).start();
        }

    }
}
