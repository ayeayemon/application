package com.application.android.bgservicecamera;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class Timer extends AsyncTask<Void, Void, Void> {

    Context mContext;
    private Handler threadHandler;

    public Timer(Context context, Handler threadHandler) {
        super();
        this.threadHandler = threadHandler;
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message.obtain(threadHandler, 100, "").sendToTarget();
        return null;
    }

}
