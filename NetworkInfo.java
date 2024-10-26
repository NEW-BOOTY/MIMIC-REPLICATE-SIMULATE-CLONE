/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */

package com.devinbroyal.network;

public class NetworkInfo {
    private String ipAddress;
    private String macAddress;

    public NetworkInfo(String ipAddress, String macAddress) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public String toString() {
        return "IP Address: " + ipAddress + "\nMAC Address: " + macAddress;
    }
}

/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */
