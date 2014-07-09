package com.aizenberg.wheather.model;

/**
 * Created by Юрий on 06.07.14.
 */
public class ServerException extends RuntimeException {

    public ServerException() {
    }

    public ServerException(String detailMessage) {
        super(detailMessage);
    }

    public ServerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ServerException(Throwable throwable) {
        super(throwable);
    }
}
