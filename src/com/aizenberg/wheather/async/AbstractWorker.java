package com.aizenberg.wheather.async;

import android.os.AsyncTask;
import com.aizenberg.wheather.model.ServerException;
import com.aizenberg.wheather.model.ServiceResponseObject;

/**
 * Created by Юрий on 05.07.14.
 */
public abstract class AbstractWorker<T> extends AsyncTask<Void, Void, ServiceResponseObject<T>> {

    private Throwable t;
    private IRemoteCallback<T> callback;

    protected AbstractWorker(IRemoteCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callback != null) {
            callback.onBegin();
        }
    }


    @Override
    protected ServiceResponseObject<T> doInBackground(Void... params) {
        try {
            return doAction();
        } catch (Throwable throwable) {
            this.t = throwable;
            return null;
        }

    }

    @Override
    protected void onPostExecute(ServiceResponseObject<T> t) {
        super.onPostExecute(t);
        if (callback != null) {
            callback.onEnd();
            generateAnswer(t);
        }
    }


    private void generateAnswer(ServiceResponseObject<T> result) {
        if (result != null) {
            if (result.getEntity() != null) {
                callback.onSuccess(result.getEntity());
            } else if (result.getErrorMessage() != null) {
                callback.onError(new ServerException(result.getErrorMessage()));
            } else if (result.getServerError() != null) {
                callback.onError(new ServerException(result.getServerError().getErrorMessage()));
            }
        } else if (t != null) {
            callback.onError(t);
        } else {
            callback.onError(new IllegalStateException("Background process doesn't return any result"));
        }
    }

    protected abstract ServiceResponseObject<T> doAction() throws Throwable;

}
