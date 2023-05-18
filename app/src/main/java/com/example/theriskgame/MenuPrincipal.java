package com.example.theriskgame;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.ui.AppBarConfiguration;

import java.util.ArrayList;
import java.util.Random;


// X 740 - Y 1700


public class MenuPrincipal extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    Button btn_multijoueur;
    Button btn_solo;
    Button btn_parcours;
    Button btn_credit;

    static private int last_picked;
    /* ---------- SOLO GAME ---------- */
    private static boolean solo_game = false;
    private static ArrayList<Class<?>> gameActivities = new ArrayList<>();
    private static ArrayList<String> gameActivities_description = new ArrayList<>();
    private static int number_of_solo_games = 0;


    /* ---------- TRAINING ---------- */

    private static boolean training = false;
    private static int number_of_training = 0;

    private static MediaPlayer mediaPlayer;

    public static ArrayList<Class<?>> getGameActivities() {
        return gameActivities;
    }

    public static ArrayList<String> getGamesString() {
        return gameActivities_description;
    }

    public static boolean isTraining() {
        return training;
    }

    public static void setTraining(boolean value) {
        training = value;
    }

    public static boolean isSolo_game() {
        return solo_game;
    }

    public static void setNumber_of_training(int val) {
        number_of_training = val;
    }

    public static int getNumber_of_training() {
        return number_of_training;
    }

    public static void setNumber_of_solo_games(int number_of_solo_games) {
        MenuPrincipal.number_of_solo_games = number_of_solo_games;
    }

    public static int getNumber_of_solo_games() {
        return number_of_solo_games;
    }

    public static Class<?> pickAndRemoveOne() {
        Random random = new Random();
        int randomIndex = random.nextInt(gameActivities.size());
        last_picked = randomIndex;
        Class<?> picked = gameActivities.get(randomIndex);
        gameActivities.remove(picked);
        return picked;
    }

    public static String getLastPickedActivityDescription() {
        String picked = gameActivities_description.get(last_picked);
        gameActivities_description.remove(picked);
        return picked;
    }

    public static void freeAndFillActivitiesArrayList() {

        for(int i = 0 ; i < gameActivities.size() ; i++) {
            gameActivities.remove(i);
            gameActivities_description.remove(i);
        }

        gameActivities.add(MainActivity.class);
        gameActivities_description.add("\n\nObtenez les 4 barres en X et en Y pour avoir le meilleur réseau possible et obtenir les points !");

        gameActivities.add(GameCircle.class);
        gameActivities_description.add("\n\nDessinez le rond le plus parfait possible pour avoir le max de points !");

        gameActivities.add(SeptSeconde.class);
        gameActivities_description.add("\n\nVous devez appuyer le plus proche possible de 7.77 pour avoir le max de points !");

        gameActivities.add(QuizTemplate.class);
        gameActivities_description.add("\n\nRépondez aux questions de sécurité informatique !");

        gameActivities.add(SimonGame.class);
        gameActivities_description.add("\n\nIl est temps de faire chauffer votre mémoire !");

        gameActivities.add(BallonQuiExplose.class);
        gameActivities_description.add("\n\nSoufflez sur le ballon mais attention à ne pas le faire exploser !");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        freeAndFillActivitiesArrayList();
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.start();

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordlayout);

        btn_multijoueur = findViewById(R.id.btnmultijoueur);
        btn_solo = findViewById(R.id.btnsolo);
        btn_parcours = findViewById(R.id.btnparcours);
        btn_credit = findViewById(R.id.btncredit);

        btn_multijoueur.setBackgroundColor(Color.parseColor("#EA2027"));
        btn_parcours.setBackgroundColor(Color.parseColor("#EA2027"));
        btn_solo.setBackgroundColor(Color.parseColor("#EA2027"));
        btn_credit.setBackgroundColor(Color.parseColor("#EA2027"));

        coordinatorLayout.setBackgroundColor(Color.BLACK);

        btn_multijoueur.setX(240);
        btn_multijoueur.setY(50);
        btn_multijoueur.setAlpha(0.9f);
        btn_solo.setX(240);
        btn_solo.setY(150);
        btn_solo.setAlpha(0.9f);
        btn_parcours.setX(240);
        btn_parcours.setY(250);
        btn_parcours.setAlpha(0.9f);
        btn_credit.setX(240);
        btn_credit.setY(350);
        btn_credit.setAlpha(0.9f);

        btn_parcours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solo_game = true;
                training = false;
                Intent intent_parcours = new Intent(MenuPrincipal.this, Pont.class);
                intent_parcours.putExtra("Points", 0);
                intent_parcours.putExtra("Text", "C'est l'heure du jeu solo, dépasse toi !");
                intent_parcours.putExtra("Next", Pont.class);
                mediaPlayer.stop();
                startActivity(intent_parcours);
            }
        });

        btn_multijoueur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solo_game = false;
                training = false;
                Intent intent1 = new Intent(MenuPrincipal.this, MultiplayerChoiceActivity.class);
                mediaPlayer.stop();
                startActivity(intent1);
            }
        });

        btn_solo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solo_game = false;
                training = true;
                Intent intent2 = new Intent(MenuPrincipal.this, ModeEntrainement.class);
                intent2.putExtra("training", true);
                mediaPlayer.stop();
                startActivity(intent2);
            }
        });

        btn_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, Credits.class);
                startActivity(intent);
            }
        });

    }
}