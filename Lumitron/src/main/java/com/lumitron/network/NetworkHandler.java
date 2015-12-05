package com.lumitron.network;

import java.util.HashMap;

import com.lumitron.util.LumitronException;

public class NetworkHandler {
    public static HashMap<String, Object> isIpReachable(HashMap<String, String> params) throws LumitronException {
        // Get the parameters
        String ipAddress = params.get("ipAddress");
        int timeout = Integer.parseInt(params.get("timeout"));

        // Check whether the IP address is reachable
        boolean isIpReachable = Network.isIpReachable(ipAddress, timeout);

        // Return the formatted response
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("ipAddress", ipAddress);
        response.put("isAlive", isIpReachable);

        return response;
    }

    public static HashMap<String, Object> getMacAddress(HashMap<String, String> params) throws LumitronException {
        // Get the parameters
        String ipAddress = params.get("ipAddress");

        // Get the MAC address
        String macAddress = Network.getMacAddressFromArpTable(ipAddress);

        // Return the formatted response
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("ipAddress", ipAddress);
        response.put("macAddress", (macAddress != null) ? macAddress : "");

        return response;
    }
}
