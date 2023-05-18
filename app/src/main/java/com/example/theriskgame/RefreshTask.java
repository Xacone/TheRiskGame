package com.example.theriskgame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

public class RefreshTask extends Thread {

    private static final long REFRESH_INTERVAL = 1000; // 1 seconde

    private boolean isRunning;
    ListView listView;
    Context context;

    public RefreshTask(Context context) {
        this.listView = listView;
        this.context = context;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                Thread.sleep(REFRESH_INTERVAL);
                Log.d("logg", "bouh");
                if (context instanceof PlayersThatJoined) {
                    PlayersThatJoined playersThatJoinedActivity = (PlayersThatJoined) context;
                    playersThatJoinedActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playersThatJoinedActivity.refreshList();
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

        Log.d("logg", "bouh");

    }*/

    public void stopRefreshing() {
        isRunning = false;
    }
}
