package com.aizenberg.wheather.utils;

import android.util.Log;
import com.aizenberg.wheather.Environment;

/**
 * Class-provider logging utils
 */
public final class Logger {

    private String tag;

    Logger(String tag) {
        this.tag = tag;
    }

    public void debug(String message) {
        if (!isDev()) return;
        Log.d(tag, message);
    }

    public void verbose(String message) {
        if (!isDev()) return;
        Log.v(tag, message);

    }

    public void error(String message) {
        if (!isDev()) return;
        Log.e(tag, message);
    }


    public void debug(String message, Throwable e) {
        if (!isDev()) return;
        Log.d(tag, message, e);
    }

    public void verbose(String message, Throwable e) {
        if (!isDev()) return;
        Log.v(tag, message, e);

    }

    public void error(String message, Throwable e) {
        if (!isDev()) return;
        Log.e(tag, message, e);
    }


    private boolean isDev() {
        return Environment.CURRENT_BRANCH == Environment.Branch.DEV;
    }
}
