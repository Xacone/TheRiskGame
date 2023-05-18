package com.example.theriskgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

// X 740 - Y 1700

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView text_hacker;
    TextView accuracy;
    ImageView eggou;
    String placeholder;
    String placeholder_accuracy;
    ConstraintLayout constraintLayout;

    boolean finished = false;

    int x = 0;
    int y = 0;
    int z = 0;

    private Thread chrono_thread;

    boolean found_x = false;
    boolean found_y = false;
    boolean found_z = false;

    String x_acc = "I";
    String y_acc = "I";
    String z_acc = "I";
    
    int minutes = 0;
    int seconds = 40;
    String chronometer_string = "";

    TextView chrono;

    Context curr_context = this;

    private Runnable chronometer = new Runnable() {
        @Override
        public void run() {
            while(true) {
                try {
                    chronometer_string = "0"+minutes+":"+seconds;
                    chrono.setText(chronometer_string);
                    Thread.sleep(1000);
                    Log.d("chrono : ", chronometer_string);
                } catch (InterruptedException e) {
                    Log.d("Interruption","Interruption de thread");
                }
                seconds -= 1;
                if(seconds == 0 && minutes > 0) {
                    minutes -= 1;
                    seconds = 59;
                }
                if(seconds == 0  && minutes == 0) {
                    finished = true;
                    Intent intent = new Intent(MainActivity.this, Pont.class);
                    intent.putExtra("Text", "\nVous n'avez pas trouvé le meilleur signal !! Vous ne gagnez rien. \n La prochaine mission consiste à répondre aux questions de sécurité pour dévérouiller l'accès au serveur central, je compte sur vous !");
                    intent.putExtra("Points", 0);
                    intent.putExtra("Next", QuizTemplate.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        constraintLayout = findViewById(R.id.cons);
        chrono = findViewById(R.id.chrono);

        accuracy = findViewById(R.id.indicateur);

        text_hacker = findViewById(R.id.text_hacker);
        text_hacker.setX(90);
        text_hacker.setY(390);

        accuracy.setX(90);
        accuracy.setY(700);

        chrono_thread = new Thread(chronometer);
        chrono_thread.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // register the accelerometer sensor listener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister the accelerometer sensor listener
        sensorManager.unregisterListener(this);
    }


    // Mettre les intervalles au hasard

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // get the X-axis value of the accelerometer
            float xAxis = event.values[0];
            float yAxis = event.values[1];
            float zAxis = event.values[2];
            //Log.d("Sensor", String.valueOf(yAxis));
            // update the x variable based on the X-axis value of the accelerometer
            x += (int)-xAxis;
            y += (int)yAxis;
            z += (int)-zAxis;

            placeholder = "Vous y êtes presque !" + "\n[" + x + "," + y + "]";
            text_hacker.setText(placeholder);

            /////// X
            if((x > 40 && x < 190))
            {
                if(x_acc.length() < 4) {
                    x_acc += "I";
                }
                placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                accuracy.setText(placeholder_accuracy);
                if((x > 60 && x < 170))
                {
                    if(x_acc.length() < 4) {
                        x_acc += "I";
                    }
                    placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                    accuracy.setText(placeholder_accuracy);
                    if((x > 80 && x < 150))
                    {
                        if(x_acc.length() < 4) {
                            x_acc += "I";
                        }
                        placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                        accuracy.setText(placeholder_accuracy);
                        if((x > 100 && x < 130))
                        {
                            if(x_acc.length() < 4) {
                                x_acc += "I";
                            }
                            placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                            accuracy.setText(placeholder_accuracy);
                            if((x > 110 && x < 130))
                            {
                                if(x_acc.length() < 4) {
                                    x_acc += "I";
                                }
                                found_x = true;
                                placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                                accuracy.setText(placeholder_accuracy);

                            } else {
                                x_acc = x_acc.substring(0, x_acc.length()-1);
                                placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                                accuracy.setText(placeholder_accuracy);
                                found_x = false;
                            }

                        } else {
                            x_acc = x_acc.substring(0, x_acc.length()-1);
                            placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                            accuracy.setText(placeholder_accuracy);
                            found_x = false;
                        }

                    } else {
                        x_acc = x_acc.substring(0, x_acc.length()-1);
                        placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc;
                        accuracy.setText(placeholder_accuracy);
                        found_x = false;
                    }

                } else {
                    x_acc = x_acc.substring(0, x_acc.length()-1);
                    placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc;
                    accuracy.setText(placeholder_accuracy);
                    found_x = false;
                }
            } else {
                if(x_acc.length() > 0 ) {
                    x_acc = x_acc.substring(0, x_acc.length()-1);
                }
                placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                accuracy.setText(placeholder_accuracy);
                found_x = false;
            }

            /* ------------------------------------------------------------------------------- */

            /////// Y
            if((y > 120 && y < 230))
            {
                if(y_acc.length() < 4) {
                    y_acc += "I";
                }
                placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                accuracy.setText(placeholder_accuracy);
                if((y > 140 && y < 220))
                {
                    if(y_acc.length() < 4) {
                        y_acc += "I";
                    }
                    placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                    accuracy.setText(placeholder_accuracy);
                    if((y > 150 && y < 210))
                    {
                        if(y_acc.length() < 4) {
                            y_acc += "I";
                        }
                        placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc ;
                        accuracy.setText(placeholder_accuracy);
                        if((y > 170 && y < 205))
                        {
                            if(y_acc.length() < 4) {
                                y_acc += "I";
                            }
                            placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc;
                            accuracy.setText(placeholder_accuracy);
                            if((y > 180 && y < 200))
                            {
                                if(y_acc.length() < 4) {
                                    y_acc += "I";
                                }
                                found_y = true;
                                placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc;
                                accuracy.setText(placeholder_accuracy);

                            } else {
                                y_acc = y_acc.substring(0, y_acc.length()-1);
                                placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc;
                                accuracy.setText(placeholder_accuracy);

                                found_y = false;
                            }

                        } else {
                            y_acc = y_acc.substring(0, y_acc.length()-1);
                            placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc;
                            accuracy.setText(placeholder_accuracy);
                            found_y = false;

                        }

                    } else {
                        y_acc = y_acc.substring(0, y_acc.length()-1);
                        placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc;
                        accuracy.setText(placeholder_accuracy);
                        found_y = false;

                    }

                } else {
                    y_acc = y_acc.substring(0, y_acc.length()-1);
                    placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc;
                    accuracy.setText(placeholder_accuracy);
                    found_y = false;

                }
            } else {
                if(y_acc.length() > 0 ) {
                    y_acc = y_acc.substring(0, y_acc.length()-1);
                }
                placeholder_accuracy = "X: " + x_acc + "\nY: " + y_acc;
                accuracy.setText(placeholder_accuracy);
            }


            if( (x > 80 && x < 130) && (y > 150 && y < 230)) {
                placeholder = "Vous y êtes presque !" + "\n[" + x + "," + y + "]";
                text_hacker.setText(placeholder);
                if (found_x && found_y) {
                    placeholder = " Connexion établie ! Bravo !" + "\n[" + x + "," + y + "]";;
                    Intent intent = new Intent(MainActivity.this, Pont.class);
                    intent.putExtra("Points", 30);
                    intent.putExtra("Text", "\nConnexion établie ! Bravo ! Vous avez gagné 30 points \n La prochaine mission consiste à répondre aux questions de sécurité pour dévérouiller l'accès au serveur central, je compte sur vous !\"");
                    intent.putExtra("Next", QuizTemplate.class);
                    chrono_thread.interrupt();
                    startActivity(intent);

                } else {
                    placeholder = "Agent ! Trouvez le meilleur réseau !" + "\n[" + x + "," + y + "]";
                    text_hacker.setText(placeholder);
                }
            }

            // eggou.setX(x*10);
            // eggou.setY(y*10);
            // do something with the updated x variable
            // ...
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}