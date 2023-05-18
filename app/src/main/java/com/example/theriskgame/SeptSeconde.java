package com.example.theriskgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SeptSeconde extends AppCompatActivity {

    String chronometer_string = "";
    int mili = 0;
    int seconds = 0;

    String result = "99:99";

    public int score(String result){
        String[] tmp = result.split(":");
        int msec =  - 100 * Integer.parseInt(tmp[0]) - Integer.parseInt(tmp[1])  + 777;
        // msec = abs(msec); // si on veux être moins punitif et autorisé a dépasser 7.77 secondes
        if (msec < 0) {
            return 0;
        }
        if (msec == 0){
            return 50;
        }
        if (msec < 10) {
            return 40;
        }
        if (msec < 20) {
            return 35;
        }
        if (msec < 30) {
            return 30;
        }
        if (msec < 40) {
            return 25;
        }
        if (msec < 50) {
            return 20;
        }
        if (msec < 60) {
            return 15;
        }
        if (msec < 70) {
            return 10;
        }
        if (msec < 100) {
            return 5;
        }
        return 0;
    }

    private Runnable chronometer = new Runnable() {

        @Override
        public void run() {
            Button button1 = (Button) findViewById(R.id.button1);
            while(true) {
                try {
                    if (mili < 10) {
                        chronometer_string = seconds+":0"+mili;
                    } else {
                        chronometer_string = seconds+":"+mili;
                    }

                    Thread.sleep(10);
                    Log.d("chrono : ", chronometer_string);
                    button1.setText(chronometer_string);
                } catch (Exception e) {

                }
                mili += 1;
                if( mili == 100) {
                    seconds += 1;
                    mili = 0 ;
                }
                if((seconds >= 10 && mili >= 1)) {
                    boolean finished = true;
                    break;
                }
            }
            if (seconds == 10) {
                Intent intent_pont = new Intent(SeptSeconde.this, Pont.class);
                intent_pont.putExtra("Points", 0);
                String concated = "\n\n T'es rien gagné !! Pfff... \n Bon t'es pas notre meilleur élément c'est sûr mais j'ai besoin que tu fasses une dernière mission pour moi, je vais avoir besoin de ta mémoire, accroche toi... Tu vas comprendre ..." ;
                intent_pont.putExtra("Text", concated);
                intent_pont.putExtra("Next", SimonGame.class);
                startActivity(intent_pont);
            } else {
                Intent intent_pont = new Intent(SeptSeconde.this, Pont.class);
                Log.d("result sept seconde : ----> ", result);
                intent_pont.putExtra("Points", score(result));
                String concated = "\n\n T'as fais " + score(result) + " points, pas mal.. \n J'ai besoin de toi pour une dernière mission, tu vas devoir user de ta mémoire.. Prêt ? Accroche toi.. Tu vas comprendre.." ;
                intent_pont.putExtra("Text", concated);
                intent_pont.putExtra("Next", SimonGame.class);
                startActivity(intent_pont);
            }

        }
    };




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sept_seconde);

        Intent intent_down = getIntent();
        intent_down.getBooleanExtra("solo", false);


        Button button1 = (Button) findViewById(R.id.button1);
        Thread chrono = new Thread(chronometer);
        chrono.start();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("J'essai de stop : ", button1.getText().toString());
                result = button1.getText().toString();
                mili = 10099;
                seconds = 10902;
                try {
                    chrono.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                button1.setText(result);

            }
        });
        }

    }

