/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */

package com.devinbroyal.network;

import com.devinbroyal.error.NetworkInfoException;

public class NetworkInfoApp {
    public static void main(String[] args) {
        try {
            NetworkInfo networkInfo = OSUtils.getNetworkInfo();
            System.out.println(networkInfo);
        } catch (NetworkInfoException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

/** 
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */
