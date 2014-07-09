package com.aizenberg.wheather.utils;

/**
 * Created by Юрий on 05.07.14.
 */
public class LoggerFactory {

    public static Logger getLogger(String tag) {
        return new Logger(tag);
    }
}
