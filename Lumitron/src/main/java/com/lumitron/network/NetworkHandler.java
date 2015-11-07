package com.lumitron.network;

import java.util.HashMap;

public class NetworkHandler {
    public static HashMap<String, String> isIpReachable(HashMap<String, String> params) {
        // Get the parameters
        String ipAddress = params.get("ip_address");
        int timeout = Integer.parseInt(params.get("timeout"));

        // Check whether the IP address is reachable
        boolean isIpReachable = Network.isIpReachable(ipAddress, timeout);

        // Return the formatted response
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("ip_address", ipAddress);
        response.put("is_reachable", isIpReachable ? "true" : "false");

        return response;
    }
}
