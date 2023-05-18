package com.example.theriskgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Pont extends AppCompatActivity {

    TextView earned;
    Button next_game;
    ConstraintLayout constraintLayout;

    int earned_points = 0;
    String text_to_show = "";
    Class<?> vers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pont);

        Intent intent_down = getIntent();

        constraintLayout = findViewById(R.id.cons);
        constraintLayout.setBackgroundColor(Color.BLACK);

        earned_points = intent_down.getIntExtra("Points", 0);
        text_to_show = intent_down.getStringExtra("Text");
        vers = (Class<?>)intent_down.getSerializableExtra("Next");

        earned = findViewById(R.id.earned_points);
        next_game = findViewById(R.id.nextgame);

        earned.setText(text_to_show);

        if(MenuPrincipal.isSolo_game() && !MenuPrincipal.isTraining()) {
            if(MenuPrincipal.getNumber_of_solo_games() < 3) {
                vers = MenuPrincipal.pickAndRemoveOne();
                MenuPrincipal.setNumber_of_solo_games(MenuPrincipal.getNumber_of_solo_games()+1);
                String to_show_text = "";
                if(MenuPrincipal.getNumber_of_solo_games() == 1) {
                    to_show_text = "\n\n" + MenuPrincipal.getLastPickedActivityDescription();
                } else {
                    to_show_text = "\n\nTu as gagné " + earned_points + " points ! \n\n" + MenuPrincipal.getLastPickedActivityDescription();
                }
                earned.setText(to_show_text);
            } else {
                earned.setText("\n\nTu as gagné " + earned_points + " points ! \n\n Fin du mode solo, bien joué à toi !");
                vers = MenuPrincipal.class;
                MenuPrincipal.freeAndFillActivitiesArrayList();
                MenuPrincipal.setNumber_of_solo_games(0);
            }
        } else if(!MenuPrincipal.isSolo_game() && MenuPrincipal.isTraining()) {
            if(MenuPrincipal.getNumber_of_training() >= 1) {
                earned.setText("\n\nTu as gagné " + earned_points + " points ! \n\n Fin du mode entrainement, bien joué à toi !");
                vers = MenuPrincipal.class;
                MenuPrincipal.setNumber_of_training(0);
            } else {
                MenuPrincipal.setNumber_of_training(MenuPrincipal.getNumber_of_solo_games()+1);
            }
        }

        if(!MenuPrincipal.isSolo_game() && !MenuPrincipal.isTraining()) {
            MultiplayerChoiceActivity.addEarnedPoints(earned_points);
            if(!MultiplayerChoiceActivity.isIsAdmin()) {
                NotificationThreadClient notificationThread = new NotificationThreadClient(MultiplayerChoiceActivity.getPlayerName(), earned_points);
                notificationThread.start();
                notificationThread.interrupt();
            } else {
                MultiplayerChoiceActivity.addPlayerAndScore("Admin", earned_points);
            }
        }



        Intent intent_up = new Intent(Pont.this, vers);

        next_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_up);
            }
        });

    }
}