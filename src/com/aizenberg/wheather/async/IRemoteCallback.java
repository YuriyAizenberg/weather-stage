package com.aizenberg.wheather.async;


public interface IRemoteCallback<T> {

    void onBegin();

    void onEnd();

    void onSuccess(T result);

    void onError(Throwable error);

}
