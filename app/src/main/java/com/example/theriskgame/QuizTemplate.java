package com.example.theriskgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class QuizTemplate extends AppCompatActivity {

    private static int nombre_de_question_passées = 0;
    int poinds_cumules = 0;

    String question = "";
    String rep1 = "";
    String rep2 = "";
    String rep3 = "";
    String rep4 = "";
    int index_bonne_rep = 0;


    TextView q;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    private static ArrayList<String> questions = new ArrayList<>();
    private static ArrayList<ArrayList<String>> univers_des_reponses = new ArrayList<>();
    ArrayList<Integer> bonnes_reponses;

    private void remplissageDesQuestions() {

        // 1
        questions.add("Quel ransomware a fait des ravages en 2017 ?");
        ArrayList<String> reps_1 = new ArrayList<>(Arrays.asList("NotPetya", "Wannacry", "Stuxnet", "Conficker"));
        univers_des_reponses.add(reps_1);

        // 2
        questions.add("Sur quel protocole de sécurité se base HTTPS ?");
        ArrayList<String> reps_2 = new ArrayList<>(Arrays.asList("SSL/TLS", "SNMP", "NTP", "IMAP"));
        univers_des_reponses.add(reps_2);

        // 3
        questions.add("Quel algorithme de chiffrement est le plus sécurisé ?");
        ArrayList<String> reps_3 = new ArrayList<>(Arrays.asList("ROT13", "WPA2", "AES-256", "RSA"));
        univers_des_reponses.add(reps_3);

        // 4
        questions.add("Quel est le nom donné à un malware qui se propage sur un réseau ?");
        ArrayList<String> reps_4 = new ArrayList<>(Arrays.asList("Ver", "Ransomware", "Trojan", "Spyware"));
        univers_des_reponses.add(reps_4);

        // 4
        questions.add("Quel outil permet d'écouter les connexions entrantes sur un port ?");
        ArrayList<String> reps_5 = new ArrayList<>(Arrays.asList("Netcat", "Nmap", "Armitage", "Netstat"));
        univers_des_reponses.add(reps_5);

        bonnes_reponses = new ArrayList<>(Arrays.asList(1, 0, 2, 0, 0));
    }

    private void next_question(int idx) {
        Intent next_question = new Intent(QuizTemplate.this, QuizTemplate.class);
        next_question.putExtra("question", questions.get(idx));
        next_question.putExtra("rep1", univers_des_reponses.get(idx).get(0));
        next_question.putExtra("rep2", univers_des_reponses.get(idx).get(1));
        next_question.putExtra("rep3", univers_des_reponses.get(idx).get(2));
        next_question.putExtra("rep4", univers_des_reponses.get(idx).get(3));
        next_question.putExtra("index_bonne_rep", bonnes_reponses.get(idx));
        next_question.putExtra("questions_passees", nombre_de_question_passées+1);
        next_question.putExtra("points_cumules", poinds_cumules);
        startActivity(next_question);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        remplissageDesQuestions();
        if(nombre_de_question_passées == 0) {
            next_question(nombre_de_question_passées);
        }

        Log.d("questions chargées" , String.valueOf(questions.size()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_template);

        Intent intent = getIntent();
        question = intent.getStringExtra("question");
        rep1 = intent.getStringExtra("rep1");
        rep2 = intent.getStringExtra("rep2");
        rep3 = intent.getStringExtra("rep3");
        rep4 = intent.getStringExtra("rep4");
        index_bonne_rep = intent.getIntExtra("index_bonne_rep", 0);
        nombre_de_question_passées = intent.getIntExtra("questions_passees", 0);
        poinds_cumules += intent.getIntExtra("points_cumules", 0);

        q = findViewById(R.id.question);
        btn1 = findViewById(R.id.rep1);
        btn2 = findViewById(R.id.rep2);
        btn3 = findViewById(R.id.rep3);
        btn4 = findViewById(R.id.rep4);

        q.setText(question);
        btn1.setText(rep1);
        btn2.setText(rep2);
        btn3.setText(rep3);
        btn4.setText(rep4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 0;
                if(index_bonne_rep == index) {
                    poinds_cumules+=10;
                }
                checkQuestions();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 1;
                if(index_bonne_rep == index) {
                    poinds_cumules+=10;
                }
                checkQuestions();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 2;
                if(index_bonne_rep == index) {
                    poinds_cumules+=10;
                }
                checkQuestions();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 3;
                if(index_bonne_rep == index) {
                    poinds_cumules+=10;
                }
                checkQuestions();
            }
        });

    }

    private void checkQuestions() {
        if(nombre_de_question_passées == 5) {
            Intent intent_pont = new Intent(QuizTemplate.this, Pont.class);
            intent_pont.putExtra("Points", poinds_cumules);
            String concated = "\n\nVous avez gagné " + poinds_cumules + " points. \n Petite pause, c'est l'anniversaire de Patrice, il faut que vous l'aidiez à gonfler les ballons sans les éclater, j'espère que vous en êtes capable..? Tu as le choix entre arrêter ou continuer de gonfler.";
            intent_pont.putExtra("Text", concated);
            intent_pont.putExtra("Next", BallonQuiExplose.class);
            if(!MenuPrincipal.isSolo_game()) {
                MultiplayerChoiceActivity.addPlayerAndScore(MultiplayerChoiceActivity.getPlayerName(), poinds_cumules);
            }
            startActivity(intent_pont);
        } else {
            next_question(nombre_de_question_passées);
        }
    }

}