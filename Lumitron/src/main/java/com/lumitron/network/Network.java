package com.lumitron.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.lumitron.util.LumitronException;

public class Network {
    /**
     * Test whether the IP address is reachable.
     * @param ipAddress The IP address
     * @param timeout The time, in milliseconds, before the call aborts
     * @return A boolean: true in case of success, false in case of failure
     * @throws LumitronException
     */
    public static boolean isIpReachable(String ipAddress, int timeout) throws LumitronException {
        boolean isIpReachable = false;

        if (ipAddress == null || ipAddress.length() == 0) {
            throw new LumitronException(Network.class.getSimpleName(), "0001", "The IP address parameter cannot be null");
        }

        try {
            if (InetAddress.getByName(ipAddress).isReachable(timeout)) {
                isIpReachable = true;
            }
        } catch (UnknownHostException e) {
            throw new LumitronException(Network.class.getSimpleName(), "0002", "Unknown host");
        } catch (IOException e) {
            throw new LumitronException(Network.class.getSimpleName(), "0003", "A network error occurred");
        }

        return isIpReachable;
    }

    /**
     * Retrieve the MAC Address by parsing the result of the Windows 'arp' command.
     * It is advisable to ping the device before getting the MAC address to refresh the table content.
     * Keep in mind that the returned MAC address corresponds to the last registered device in the table:
     * the device could change, but if no packet is received from that new device, the MAC address is not updated.
     * @param ipAddress The IP address
     * @return A String representing the MAC address in the xx-xx-xx-xx-xx-xx format, or null if not found
     * @throws LumitronException
     */
    public static String getMacAddressFromArpTable(String ipAddress) throws LumitronException {
        String macAddress = null;

        // Retrieve the Mac Address by parsing the result of the Windows 'arp' command
        try {
            // Execute the arp command
            Process arpProcess = Runtime.getRuntime().exec("arp -a " + ipAddress);

            // Parse the output of the command, looking for the MAC address if exists
            Scanner s = new Scanner(arpProcess.getInputStream());
            macAddress =  s.findWithinHorizon("([0-9a-fA-F]{2}-){5}([0-9a-fA-F]{2})", 0);
            s.close();
        } catch (IOException e) {
            throw new LumitronException(Network.class.getSimpleName(), "0004", "An error occurred while retrieving the MAC Address");
        }

        return macAddress;
    }
}
