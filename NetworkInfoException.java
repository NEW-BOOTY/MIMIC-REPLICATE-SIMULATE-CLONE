/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */

package com.devinbroyal.network;

import com.devinbroyal.error.NetworkInfoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OSUtils {
    public static NetworkInfo getNetworkInfo() throws NetworkInfoException {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                return getWindowsNetworkInfo();
            } else if (os.contains("mac")) {
                return getMacNetworkInfo();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                return getLinuxNetworkInfo();
            } else {
                throw new NetworkInfoException("Unsupported operating system: " + os);
            }
        } catch (IOException e) {
            throw new NetworkInfoException("Failed to retrieve network information", e);
        }
    }

    private static NetworkInfo getWindowsNetworkInfo() throws IOException {
        String ipAddress = executeCommand("ipconfig");
        String macAddress = executeCommand("getmac");
        return new NetworkInfo(ipAddress, macAddress);
    }

    private static NetworkInfo getMacNetworkInfo() throws IOException {
        String ipAddress = executeCommand("ifconfig");
        String macAddress = executeCommand("ifconfig");
        return new NetworkInfo(ipAddress, macAddress);
    }

    private static NetworkInfo getLinuxNetworkInfo() throws IOException {
        String ipAddress = executeCommand("hostname -I");
        String macAddress = executeCommand("cat /sys/class/net/eth0/address");
        return new NetworkInfo(ipAddress, macAddress);
    }

    private static String executeCommand(String command) throws IOException {
        StringBuilder output = new StringBuilder();
        Process process = Runtime.getRuntime().exec(command);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString().trim();
    }
}

/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */
