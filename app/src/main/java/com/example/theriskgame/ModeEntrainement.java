package com.example.theriskgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ModeEntrainement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_entrainement);

        ConstraintLayout cons_layout_training = findViewById(R.id.conslayout_training);
        cons_layout_training.setBackgroundColor(Color.BLACK);

        LinearLayout linearLayout_training = findViewById(R.id.linearlayout_training);

        Button btnRetour = new Button(this);
        btnRetour.setText("Retour au Menu");
        btnRetour.setBackgroundColor(Color.parseColor("#EA2027"));
        btnRetour.setWidth(100);
        btnRetour.setHeight(100);
        btnRetour.setAlpha(0.7f);

        ArrayList<String> texts = new ArrayList<>();
        texts.add("Jeu 1");
        texts.add("Jeu 2");
        texts.add("Jeu 3");
        texts.add("Jeu 4");
        texts.add("Jeu 5");
        texts.add("Jeu 6");

        int x = 20;
        int y = 50;
        for(int i = 0 ; i < 6 ; i++) {
            y += 1;
            Button btn_game_training = new Button(this);
            btn_game_training.setX(x);
            btn_game_training.setY(y);
            btn_game_training.setBackgroundColor(Color.BLACK);
            String toshow = String.valueOf(i+1) + ". " + texts.get(i);
            btn_game_training.setText(toshow);
            btn_game_training.setWidth(300);
            btn_game_training.setHeight(200);
            btn_game_training.setAlpha(0.8f);
            btn_game_training.setTextColor(Color.WHITE);
            linearLayout_training.addView(btn_game_training);
            int finalI = i;
            btn_game_training.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class<?> picked = MenuPrincipal.getGameActivities().get(finalI);
                    Intent intent_parcours = new Intent(ModeEntrainement.this, Pont.class);
                    intent_parcours.putExtra("Points", 0);
                    intent_parcours.putExtra("Text", MenuPrincipal.getGamesString().get(finalI));
                    intent_parcours.putExtra("Next", picked);
                    startActivity(intent_parcours);
                }
            });
        }

        btnRetour.setX(x);
        btnRetour.setY(y);
        linearLayout_training.addView(btnRetour);

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModeEntrainement.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });
        btnRetour.setTextColor(Color.WHITE);

    }
}