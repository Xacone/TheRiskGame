package com.example.theriskgame;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.theriskgame.databinding.ActivityScoreBoardBinding;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ScoreBoard extends AppCompatActivity {
    TextView affichage;
    EditText textToSend;
    Button sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_score_board);

        affichage = findViewById(R.id.affichage);
        textToSend = findViewById(R.id.textToSend);
        sendButton = findViewById(R.id.sendButton);

        Intent intent = getIntent();
        String ip_addr = intent.getStringExtra("ip_address");

        Log.d("Received intent", ip_addr);
    }

}