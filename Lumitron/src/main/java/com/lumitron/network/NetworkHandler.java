package com.lumitron.network;

import java.util.HashMap;

public class NetworkHandler {
    public static HashMap<String, String> isIpReachable(HashMap<String, String> params) {
        // Get the parameters
        String ipAddress = params.get("ipAddress");
        int timeout = Integer.parseInt(params.get("timeout"));

        // Check whether the IP address is reachable
        boolean isIpReachable = Network.isIpReachable(ipAddress, timeout);

        // Return the formatted response
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("ipAddress", ipAddress);
        response.put("isAlive", isIpReachable ? "true" : "false");

        return response;
    }

    public static HashMap<String, String> getMacAddress(HashMap<String, String> params) {
        // Get the parameters
        String ipAddress = params.get("ipAddress");

        // Get the MAC address
        String macAddress = Network.getMacAddressFromArpTable(ipAddress);

        // Return the formatted response
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("ipAddress", ipAddress);
        response.put("macAddress", (macAddress != null) ? macAddress : "");

        return response;
    }
}
