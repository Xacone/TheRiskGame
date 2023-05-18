package com.example.theriskgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ClientClass extends AsyncTask<Void, Void, String> {

    private String player_name;
    private String ip_addr;
    private int port;
    private Socket socket_client;
    private static OutputStream outputStream;

    private Context context;

    public ClientClass(String ip_addr, int port, String player_name, Context context) {
        this.ip_addr = ip_addr;
        this.port = port;
        this.context = context;
        this.player_name = player_name;
    }

    public static void sendMessageToServer(String msg) {
        try {
            outputStream.write(msg.getBytes());
            Log.d("envoyé côté client", msg);
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Exception", "Erreur envoi côté client");
        }
    }

    void connectToServer(String ip_addr, int port) {
        try {
            socket_client = new Socket(ip_addr, port);
            outputStream = socket_client.getOutputStream();

            /*
            PlayersThatJoined playersThatJoined = new PlayersThatJoined();
            playersThatJoined.addPlayerToListAndRefresh(socket_client.getInetAddress().toString());
            */

        } catch (Exception e) {
            Log.d("Exception", "Erreur de connexion côté client");
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.d("CLIENT", "Client asynctask invoked and executed");
        connectToServer(this.ip_addr, this.port);
        sendMessageToServer("Bonjour Yazid comment vas-tu aujourd'hui ?");
        sendMessageToServer("J'espère que tu vas merveilleisement bien");

        InputStream inputStream = null;
        try {
            inputStream = socket_client.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] buffer = new byte[1024];
        int bytesRead;

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String received = new String(buffer, 0, bytesRead);
                Log.d("J'ai reçu ce message de la part du serveur : ", received);

                if(received.contains("leaderboard")) {
                    Log.d("received leaderboard", "received leaderboard");

                    RefreshLeaderBoard refreshLeaderBoard = new RefreshLeaderBoard(this.context, received);
                    refreshLeaderBoard.start();
                }

                if(received.contains("debutjeu")) {
                    Intent letsgo = new Intent(context, Pont.class);
                    letsgo.putExtra("Points", 0);
                    letsgo.putExtra("Text","\nYoupi c'est le début du jeu");
                    letsgo.putExtra("Next", MainActivity.class);
                    context.startActivity(letsgo);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket_client.close();
            } catch (IOException e) {
                Log.d("EXCEPTIONNNN", "ya eu exception");
            }
        }

        return null;
    }
}
