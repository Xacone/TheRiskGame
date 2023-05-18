package com.example.theriskgame;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.BitSet;
import java.util.Random;

public class BallonQuiExplose extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 1;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    ImageView ballon;
    Button passer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ballon_qui_explose);

        // Vérifier et demander la permission d'accès au microphone
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{RECORD_AUDIO},
                    PERMISSION_REQUEST_CODE);
        } else {
            startRecording();
        }
    }



    private void startRecording() {
        Log.d("Startrecording", "Enregistrement audio en cours...");

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/recording.3gp");

        ballon = findViewById(R.id.leballon);
        passer = findViewById(R.id.passer);

        passer.setBackgroundColor(Color.parseColor("#d35400"));
        passer.setText("Garder et passer à la tâche suivante");

        Random random = new Random();
        int limite_explosion = random.nextInt(721) + 761;
        Log.d("limite explosion", String.valueOf(limite_explosion));

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            Log.d("MicrophoneBlowDetector", "Enregistrement audio en cours...");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MediaRecorder", "Error starting recording: " + e.getMessage());

        }

        passer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_pont = new Intent(BallonQuiExplose.this, Pont.class);
                intent_pont.putExtra("Points", (int)ballon.getHeight() / 10);
                String concated = "\n\n T'as fais " + (int)ballon.getHeight() / 10 + " points. \n Pour la prochaine mission tu devras tracer le cercle le plus parfait possible pour dévérouiller le coffre de Joe Biden, t'en es capable ?" ;
                intent_pont.putExtra("Text", concated);
                intent_pont.putExtra("Next", GameCircle.class);
                startActivity(intent_pont);
            }
        });

        Thread detectionThread = null;
        // Démarrer un thread pour détecter le souffle dans le microphone
        Thread finalDetectionThread = detectionThread;
        boolean[] isRunning = {true};
        detectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRecording) {
                    try {
                        double amplitude = mediaRecorder.getMaxAmplitude();
                        if (amplitude > 10000) {
                            Log.d("MicrophoneBlowDetector", "Souffle détecté !");

                            BallonQuiExplose.super.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isRunning[0]) {
                                        ViewGroup.LayoutParams layoutParams = ballon.getLayoutParams();
                                        layoutParams.width = layoutParams.width+30;
                                        layoutParams.height = layoutParams.height+30;
                                        Log.d("height : ", String.valueOf(layoutParams.height));
                                        ballon.setLayoutParams(layoutParams);
                                        if(layoutParams.height >= limite_explosion) {
                                            ballon.setImageResource(R.mipmap.boub);
                                            Intent intent_pont = new Intent(BallonQuiExplose.this, Pont.class);
                                            intent_pont.putExtra("Points", 0);
                                            String concated = "\n\nT'as fais explosé le ballon! T'as rien gagné et Patrice t'a jeté dehors... \n Bon pour la prochaine mission tu devras tracer le cercle le plus parfait possible pour dévérouiller le coffre de Joe Biden, t'en es capable ?" ;
                                            intent_pont.putExtra("Text", concated);
                                            intent_pont.putExtra("Next", GameCircle.class);
                                            startActivity(intent_pont);
                                            isRunning[0] = false;
                                        }
                                    }
                                }
                            });

                        }
                        Thread.sleep(100); // Délai entre les détections
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        detectionThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                // Permission refusée
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
    }
}