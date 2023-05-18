package com.example.theriskgame;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SimonGame extends AppCompatActivity {
    Button[] but = new Button[10];
    int[] colorC;
    int[] color;


    int seconds = 10;
    int lvl = 0;
    int cpt = 0;
    int[] list_colors;
    int[] color_past;
    CyclicBarrier barr;
    CyclicBarrier barr2;
    Random random;
    Thread visu;
    Thread chrono;
    boolean notSleep =false;

    private Runnable chronometer = new Runnable() {

        @Override
        public void run() {
            TextView button10 = (TextView) findViewById(R.id.button10);
            while (seconds > 0) {
                try {
                    String chronometer_string = String.valueOf(seconds);

                    Thread.sleep(1000);
                    //Log.d("chrono : ", chronometer_string);
                    //button10.setText(chronometer_string);



                } catch (Exception e) {

                }
                if (notSleep) {
                    seconds -= 1;
                    if (seconds == -1) {
                        //todo perdu il faut renvoyer lvl
                        Log.d("lvl renvoie : ", String.valueOf(lvl));
                        break;

                    } else {
                        //button10.setText(String.valueOf(seconds));
                    }
                }
            }
            button10.setText("you loose");
        }

        ;
    };

    private Runnable init = new Runnable() {

        @Override
        public void run() {
            while(true){
                try {
                    barr.await();
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for (int i = 0; i <=lvl ; i++){
                    int index = list_colors[i];
                    Log.d("index", String.valueOf(index));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    but[index].setBackgroundColor(colorC[index]);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    but[index].setBackgroundColor(color[index]);
                }

                notSleep=true;

            }
        }
    };

    private void iteration(int i){
        if(i != list_colors[cpt]){
            //todo Fin de partie loose et renvoyé lvl
            Log.d("lvl renvoie : ", String.valueOf(lvl));
            seconds = -100;
            TextView button10 = (TextView) findViewById(R.id.button10);
            button10.setText("you loose");
            Intent intent_pont = new Intent(SimonGame.this, Pont.class);
            intent_pont.putExtra("Points", lvl*10);
            String concated = "\n\n T'as fais " + (int)lvl*10 + " points. \n Ce fut un plaisir de collaborer avec toi. À la prochaine !" ;
            intent_pont.putExtra("Text", concated);
            intent_pont.putExtra("Next", LeaderBoard.class);
            startActivity(intent_pont);
            

        } else {
            cpt++;
            if(cpt > lvl) {
                notSleep =false;
                seconds = 10;
                TextView button10 = (TextView) findViewById(R.id.button10);
                cpt =0;
                lvl++;
                button10.setText("lvl" + lvl);

                list_colors[lvl] = random.nextInt(9) +1;
                try {
                    barr.await();
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simon_game);

        but[1] = (Button) findViewById(R.id.button1);
        but[2] = (Button) findViewById(R.id.button2);
        but[3] = (Button) findViewById(R.id.button3);
        but[4] = (Button) findViewById(R.id.button4);
        but[5] = (Button) findViewById(R.id.button5);
        but[6] = (Button) findViewById(R.id.button6);
        but[7] = (Button) findViewById(R.id.button7);
        but[8] = (Button) findViewById(R.id.button8);
        but[9] = (Button) findViewById(R.id.button9);

        colorC = new int[] {getResources().getColor(R.color.simon1c),
                getResources().getColor(R.color.simon1c),
                getResources().getColor(R.color.simon2c),
                getResources().getColor(R.color.simon3c),
                getResources().getColor(R.color.simon4c),
                getResources().getColor(R.color.simon5c),
                getResources().getColor(R.color.simon6c),
                getResources().getColor(R.color.simon7c),
                getResources().getColor(R.color.simon8c),
                getResources().getColor(R.color.simon9c)};

        color = new int[] {getResources().getColor(R.color.simon1),
                getResources().getColor(R.color.simon1),
                getResources().getColor(R.color.simon2),
                getResources().getColor(R.color.simon3),
                getResources().getColor(R.color.simon4),
                getResources().getColor(R.color.simon5),
                getResources().getColor(R.color.simon6),
                getResources().getColor(R.color.simon7),
                getResources().getColor(R.color.simon8),
                getResources().getColor(R.color.simon9)};

        barr = new CyclicBarrier(2);
        barr2 = new CyclicBarrier(2);
        random = new Random();
        chrono = new Thread(chronometer);
        chrono.start();

        //list_colors = new int[] {1,2,3,4,5,6,7,8,9,5};
        list_colors = new int[100];
        color_past = new int[100];
        visu = new Thread(init);
        visu.start();


        but[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iteration(1);
            }
        });
        but[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iteration(2);
            }
        });
        but[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iteration(3);
            }
        });
        but[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iteration(4);
            }
        });
        but[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iteration(5);
            }
        });
        but[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iteration(6);
            }
        });
        but[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iteration(7);
            }
        });
        but[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iteration(8);
            }
        });
        but[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iteration(9);
            }
        });

        list_colors[lvl] = random.nextInt(9) +1;


        try {
            barr.await();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }










    };




}



