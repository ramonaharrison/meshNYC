package c4q.nyc.ramonaharrison.meshnyc;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Ramona Harrison
 * on 8/2/15.
 */
public class GroupMember extends GroupNode {

    private String groupLeaderAddress;
    private String groupMemberAddress;
    private long lastHello;

    private final int leaderPortIn = 8888;

    public GroupMember(String groupLeaderAddress) {
        this.groupMemberAddress = getDottedDecimalIP(getLocalIPAddress());
        this.groupLeaderAddress = groupLeaderAddress;
        this.lastHello = System.currentTimeMillis();
    }

    public GroupMember(String groupMemberAddress, String groupLeaderAddress) {
        this.groupMemberAddress = groupMemberAddress;
        this.groupLeaderAddress = groupLeaderAddress;
        this.lastHello = System.currentTimeMillis();
    }

    public String getGroupMemberAddress() {
        return groupMemberAddress;
    }


    public void sendHandshake() {
        (new Thread(new ClientSocket(groupLeaderAddress, leaderPortIn, getGroupMemberAddress()))).start();
    }

    private byte[] getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress instanceof Inet4Address) {
                            return inetAddress.getAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            //Log.d("AndroidNetworkAddressFactory", "getLocalIPAddress()", ex);
        } catch (NullPointerException ex) {
            //Log.d("AndroidNetworkAddressFactory", "getLocalIPAddress()", ex);
        }
        return null;
    }

    private String getDottedDecimalIP(byte[] ipAddr) {
        //convert to dotted decimal notation:
        String ipAddrStr = "";
        for (int i = 0; i<ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i]&0xFF;
        }
        return ipAddrStr;
    }

}
