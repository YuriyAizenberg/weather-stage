package com.aizenberg.wheather.model;

/**
 * Created by Server on 13.01.14.
 */
public class ServiceResponseObject<T> {


    private T entity;
    private String errorMessage;
    private ServerError serverServerError;

    public ServiceResponseObject() {
    }

    public ServiceResponseObject(T entity, String errorMessage, ServerError serverServerError) {
        this.entity = entity;
        this.errorMessage = errorMessage;
        this.serverServerError = serverServerError;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ServerError getServerError() {
        return serverServerError;
    }

    public void setServerError(ServerError serverServerError) {
        this.serverServerError = serverServerError;
    }

}
