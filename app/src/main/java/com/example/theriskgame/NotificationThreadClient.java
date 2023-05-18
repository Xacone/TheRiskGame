package com.example.theriskgame;

public class NotificationThreadClient extends Thread {
    String playername;
    int score;
    public NotificationThreadClient(String playername, int score) {
        this.playername = playername;
        this.score = score;
    }
    @Override
    public void run() {
        ClientClass.sendMessageToServer("score;"+ playername + ";" + String.valueOf(score));
    }
}
