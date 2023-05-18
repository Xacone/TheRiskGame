package com.example.theriskgame;

public class NotificationThread extends Thread {

    @Override
    public void run() {
        ServerClass.sendMessageToClients("debutjeu");
    }
}
