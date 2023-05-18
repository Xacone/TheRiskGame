package com.example.theriskgame;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class RefreshLeaderBoard extends Thread {


    Context context;
    HashMap<String, Integer> hashmap;
    String received_leaderboard;

    public RefreshLeaderBoard(Context context, String received_text) {
        this.context = context;
        this.received_leaderboard = received_text;
    }

    @Override
    public void run() {
        while (true) {
            LeaderBoard.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    Log.d("coté client", "côté client pour le leaderboard: " + received_leaderboard);

                    String[] splited = received_leaderboard.split(";");
                    String to_append = "\n";
                    if(LeaderBoard.getLeaderListTextView() != null) {
                        for (int i = 1; i < splited.length; i++) {
                            to_append += splited[i] + "\n";
                        }
                        LeaderBoard.setLeaderListText(to_append);
                        to_append = "";
                    }
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
