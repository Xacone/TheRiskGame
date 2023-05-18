package com.example.theriskgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeaderBoard extends AppCompatActivity {

    Button button_retour;
    static TextView leaderlist;

    private static Handler handler = new Handler(Looper.getMainLooper());


    public static TextView getLeaderListTextView() {
        return leaderlist;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static void setLeaderListText(String leaderboard) {
        leaderlist.setText(leaderboard);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        button_retour = findViewById(R.id.retourmenu);
        leaderlist = findViewById(R.id.leaderlist);

        button_retour.setText("Retour au menu principal");
        button_retour.setBackgroundColor(Color.parseColor("#44bd32"));

        String toshow = "Leaderboard\n";
        for(Map.Entry<String, Integer> entry : MultiplayerChoiceActivity.getPlayerAndScore().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            toshow += key + " : " + value + "\n";
        }
        leaderlist.setText(toshow);

        button_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoard.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });

    }
}