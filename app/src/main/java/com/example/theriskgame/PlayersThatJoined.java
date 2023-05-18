package com.example.theriskgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.loader.content.AsyncTaskLoader;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.theriskgame.databinding.ActivityPlayersThatJoinedBinding;

import java.sql.Ref;
import java.util.ArrayList;

public class PlayersThatJoined extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPlayersThatJoinedBinding binding;
    static private RefreshTask refreshListTask;
    private static ListView players_that_joined_listview;

    Intent intent;
    Button startGameButton;
    static ArrayAdapter<String> adapter;

    public void refreshList() {
        if(ServerClass.getPlayerThatJoined() != null) {

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ServerClass.getPlayerThatJoined());
            if(players_that_joined_listview != null) {
                Log.d("refreshed", "refreshed");
                players_that_joined_listview.setAdapter(adapter);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_that_joined);

        players_that_joined_listview = findViewById(R.id.playerthatjoined);
        startGameButton = findViewById(R.id.startgamebutton);

        startGameButton.setBackgroundColor(Color.parseColor("#130f40"));
        startGameButton.setTextColor(Color.WHITE);

        if(!MultiplayerChoiceActivity.isIsAdmin()) {
            startGameButton.setVisibility(View.GONE);
        }

        binding = ActivityPlayersThatJoinedBinding.inflate(getLayoutInflater());

        refreshListTask = new RefreshTask(this);
        refreshListTask.start();

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationThread notificationThread = new NotificationThread();
                notificationThread.start();
                notificationThread.interrupt();
                Intent letsgo = new Intent(PlayersThatJoined.this, Pont.class);
                letsgo.putExtra("Points", 0);
                letsgo.putExtra("Text","\nVous incarnez un hacker qui doit s'inflitrer dans le système de la maison blanche. Vous devez commencer par trouver le meilleur signal pour établir la communication avec le QG.");
                letsgo.putExtra("Next", MainActivity.class);
                startActivity(letsgo);
            }
        });
    }
}







