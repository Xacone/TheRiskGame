package com.example.theriskgame;

import android.app.Activity;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ServerClass extends AsyncTask<Void, Void, String> {

    private ServerSocket serverSocket;
    static ArrayList<OutputStream> outputStreams = new ArrayList<>();
    ArrayList<InputStream> inputStreams = new ArrayList<>();
    private int port;

    private ArrayList<Socket> sockets = new ArrayList<>();

    private Thread sending_thread = new Thread(new Runnable() {
        @Override
        public void run() {

        }
    });

    private static ArrayList<String> playersThatJoined = new ArrayList<>();

    public static void addPlayerToList(String ip_addr) {
        playersThatJoined.add(ip_addr);
    }

    public static ArrayList<String> getPlayerThatJoined() {
        return playersThatJoined;
    }

    public ServerClass(int port) {
        this.port = port;
    }

    public static void sendMessageToClients(String msg) {
        try {
            for(OutputStream outputStream : outputStreams) {
                outputStream.write(msg.getBytes());
            }
        } catch (IOException e) {
            Log.d("Exception", "Erreur envoi côté serveur");
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {

        Log.d("SERVER", "Server asynctask invoked and executed");

        try {
            serverSocket = new ServerSocket(port);
            addPlayerToList(serverSocket.getInetAddress().getHostAddress());

            Log.d("ECOUTE", "ECOUTE SUR 7777");
        } catch (IOException e) {
            Log.d("IOException", "Erreur de connexion côté serveur");
            e.printStackTrace();
        }

        while(true) {
            try {
                while (true) {
                    Log.d("Attente...", "Attente");
                    Socket socket = serverSocket.accept();
                    sockets.add(socket);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStreams.add(outputStream);
                    sendMessageToClients("Quelquu'un d'autre c'est connecté");

                    /*
                    PlayersThatJoined playersThatJoined1 = new PlayersThatJoined();
                    playersThatJoined1.refreshList();
                    */

                    String clientAddress = socket.getInetAddress().getHostAddress();
                    addPlayerToList(clientAddress);
                    Log.d("Client connected : ", clientAddress);
                    // Handle incoming connection here
                    sendMessageToClients("Bonjour Valère comment vas-tu aujourd'hui ? ");
                    sendMessageToClients("Merci de t'être connecté à moi !");
                    InputStream inputStream = socket.getInputStream();
                    inputStreams.add(inputStream);

                    Thread_connexion thread_connexion = new Thread_connexion(inputStream, outputStream);
                    thread_connexion.start();

                    thread_leaderbord t_leaderboard = new thread_leaderbord();
                    t_leaderboard.start();

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class Thread_connexion extends Thread {
        InputStream inputStream;
        OutputStream outputStream;
        public Thread_connexion(InputStream inputStream, OutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            while(true) {

                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while((bytesRead = inputStream.read(buffer)) != -1) {
                        String received = new String(buffer, 0, bytesRead);
                        Log.d("J'ai reçu ce message de la part du client : ", received);
                        if(received.contains("score")) {
                            String playername = received.split(";")[1];
                            int score = Integer.parseInt(received.split(";")[2]);
                            MultiplayerChoiceActivity.addPlayerAndScore(playername, score);
                            Log.d("SCORE", playername + " -- " + score);

                            for (Map.Entry<String, Integer> entry : MultiplayerChoiceActivity.getPlayerAndScore().entrySet()) {
                                String key = entry.getKey();
                                Integer value = entry.getValue();
                                String val = key + " : " + value;
                                System.out.println(val);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception", "Erreur lors de l'écoute sur le thrad Thread_connexion");
                }
            }
        }
    }

    private static class thread_leaderbord extends Thread {
        @Override
        public void run() {
            while(true) {
                if(outputStreams.size() >= 1) {
                    String constructed_leaderbord_to_send = "";
                    for (Map.Entry<String, Integer> entry : MultiplayerChoiceActivity.getPlayerAndScore().entrySet()) {
                        String key = entry.getKey();
                        Integer value = entry.getValue();
                        String val = key + " : " + value;
                        constructed_leaderbord_to_send += val+";";
                    }
                    //LeaderBoard.setLeaderListText(LeaderBoard.getLeaderListTextView().getText().toString() + "\n\n" + val);
                    sendMessageToClients("leaderboard;"+constructed_leaderbord_to_send);
                    //LeaderBoard.setLeaderListText(constructed_leaderbord_to_send);
                    constructed_leaderbord_to_send = "";

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
