package com.lumitron.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

        try {
            if (InetAddress.getByName(ipAddress).isReachable(timeout)) {
                isIpReachable = true;
            }
        } catch (UnknownHostException e) {
            throw new LumitronException(NetworkService.class.getSimpleName(), "0001", "Unknown host");
        } catch (IOException e) {
            throw new LumitronException(NetworkService.class.getSimpleName(), "0002", "A network error occurred");
        }

        return isIpReachable;
    }
}
