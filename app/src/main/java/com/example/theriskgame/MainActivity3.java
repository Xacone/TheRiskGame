package com.example.theriskgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.util.Random;


// X 740 - Y 1700

public class MainActivity3 extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor orientationSensor;
    private SensorEventListener orientationListener;

    int max_width;
    int max_height;

    ImageView balle_de_ping_pong;
    ImageView raquette_joueur;
    ImageView raquette_adversaire;

    float balle_x = 0;
    float balle_y = 0;
    boolean balle_touche_haut = true;
    boolean balle_touche_bas = false;
    int vitesse_balle = 10;
    float balle_angle_x = 0;

    float cons_rot = 1;

    DisplayMetrics displayMetrics = new DisplayMetrics();

    float raquette_adversaire_x = 0;
    float raquette_adversaire_y = 0;

    float raquette_player_x = 0;
    float last_x_player = 0;


    float raquette_player_y = 0;

    float padding_raquettes = 500;

    double start_x;
    double start_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        balle_de_ping_pong = findViewById(R.id.balle_de_ping_pong);

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        max_width = 740;
        max_height = 1700;

        start_x = 370; //max_width/2.0f;
        start_y = 850; //max_height/2.0f;

        ConstraintLayout constraintLayout = findViewById(R.id.le_cons_layout);
        constraintLayout.setBackgroundColor(Color.WHITE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        raquette_joueur = findViewById(R.id.raquette_joueur);
        raquette_adversaire = findViewById(R.id.raquette_adversaire);

        raquette_joueur.setX(0);
        raquette_joueur.setY(1800);

        raquette_adversaire.setX(0);
        raquette_adversaire.setY(100);

        balle_x = (float) start_x;
        balle_y = (float) start_y;

        balle_de_ping_pong.setX(balle_x);
        balle_de_ping_pong.setY(balle_y);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration((long) 0.5); //You can manage the blinking time with this parameter
        anim.setStartOffset(0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(Animation.INFINITE);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                if(balle_touche_haut) {
                    balle_y = balle_y + vitesse_balle;
                }

                if(balle_touche_bas) {
                    balle_y = balle_y - vitesse_balle;
                }

                balle_de_ping_pong.setX(balle_x);
                balle_de_ping_pong.setY(balle_y);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(balle_touche_haut) {
                    balle_y = balle_y + vitesse_balle;
                }

                if(balle_touche_bas) {
                    balle_y = balle_y - vitesse_balle;
                }

                balle_de_ping_pong.setX(balle_x);
                balle_de_ping_pong.setY(balle_y);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

                if(balle_touche_haut) {
                    balle_x = balle_x + balle_angle_x;
                    balle_y = balle_y + vitesse_balle;
                }

                if(balle_touche_bas) {
                    balle_x = balle_x + balle_angle_x;
                    balle_y = balle_y - vitesse_balle;
                }

                if(balle_de_ping_pong.getX() == 0) {
                    balle_angle_x = -balle_angle_x*cons_rot*2;
                }

                if(balle_de_ping_pong.getX() == 600) {
                    balle_angle_x = -balle_angle_x*cons_rot*4;
                }

                balle_de_ping_pong.setX(balle_x);
                balle_de_ping_pong.setY(balle_y);

                //Log.d("log : ", raquette_joueur.getY() + " --- " + raquette_adversaire.getY());

                if (balle_y == raquette_joueur.getY()-50) {
                    balle_touche_bas = true;
                    balle_touche_haut = false;

                    if(balle_x >= raquette_joueur.getX()-40  && balle_x <= raquette_joueur.getX()+40) {
                        balle_angle_x = -5;
                        Log.d("t", "touché !");
                    }

                } else {
                    if (balle_y == raquette_adversaire.getY()) {
                        balle_touche_haut = true;
                        balle_touche_bas = false;
                    }
                }

            }

                /*
                if(balle_y == 100 || balle_y == 1700) {
                    balle_y = -balle_y;
                } */
        });

        balle_de_ping_pong.setAnimation(anim);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
        float[] orientationAngles = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientationAngles);


        float x = rotationMatrix[0];;
        float y = orientationAngles[1];
        float z = orientationAngles[2];

        if(x < 0.9) {
            if(raquette_player_x >= 20 ) {
                raquette_player_x = raquette_player_x - 35;
                raquette_joueur.setX(raquette_player_x);
                }
            }

        if(x > 0.9) {
            if(raquette_player_x <= 740) {
                raquette_player_x = raquette_player_x + 35;
                raquette_joueur.setX(raquette_player_x);
            }
        }

        last_x_player = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("bouuh", "bouuh");
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}