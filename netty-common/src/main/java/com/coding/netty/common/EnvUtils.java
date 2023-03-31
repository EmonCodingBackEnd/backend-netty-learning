package com.coding.netty.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EnvUtils {

    public static boolean isWin = true;

    static {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            isWin = true;
        } else {
            isWin = false;
        }
    }

}
