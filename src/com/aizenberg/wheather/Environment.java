package com.aizenberg.wheather;

/**
 * Created by Юрий on 05.07.14.
 */
public class Environment {
    public enum Branch {
        DEV,
        STAGE,
        PRODUCTION
    }

    public static final Branch CURRENT_BRANCH = Branch.DEV;

}
