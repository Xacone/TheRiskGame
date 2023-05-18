package com.example.theriskgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MultiplayerChoiceActivity extends AppCompatActivity {

    static private boolean isAdmin = false;
    private Button joinServer;
    private Button createServer;
    private EditText serverIpAddress;
    private static EditText playerName;
    private Button returnToMenu;
    private static ServerClass serverClass;
    private static ClientClass clientClass;

    public static ServerClass getServerClass() {
        return serverClass;
    }

    public static ClientClass getClientClass() {
        return clientClass;
    }

    private static HashMap<String, Integer> playerAndScore = new HashMap<>();
    private static int EARNED_POINTS = 0;

    public static int getEarnedPoints() {
        return EARNED_POINTS;
    }

    public static void addEarnedPoints(int Points) {
        EARNED_POINTS += Points;
    }

    public static HashMap<String, Integer> getPlayerAndScore() {
        return playerAndScore;
    }

    public static void addPlayerAndScore(String player, int score) {
        if(playerAndScore.containsKey(player)) {
            playerAndScore.put(player, playerAndScore.get(player)+score);
        } else {
            playerAndScore.put(player, 0);
        }
    }

    public static String getPlayerName() {
        return playerName.getText().toString();
    }

    public static boolean isIsAdmin() {
        return isAdmin;
    }

    public static void setIsAdmin(boolean value) {
        isAdmin = value;
    }

    private String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // remove suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("IP Address", "Error getting IP address: " + e.getMessage());
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multipliyermodechoosing);

        playerName = findViewById(R.id.playername);
        serverIpAddress = findViewById(R.id.ipaddrtext);
        joinServer = findViewById(R.id.joinServerButton);
        createServer = findViewById(R.id.createServerButton);
        returnToMenu = findViewById(R.id.btnquitter);

        CoordinatorLayout constraintLayout = findViewById(R.id.coordlayoutmult);
        constraintLayout.setBackgroundColor(Color.BLACK);

        String text_w_ip_addr = createServer.getText() + "\n(@ip: " + getIPAddress(true) + ")";
        createServer.setText(text_w_ip_addr);

        Context thiscontext = this;

        joinServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiplayerChoiceActivity.this, PlayersThatJoined.class);
                intent.putExtra("ip_address", serverIpAddress.getText().toString());
                if(!serverIpAddress.getText().toString().isEmpty()) {
                    clientClass = new ClientClass(serverIpAddress.getText().toString(), 7777, playerName.getText().toString(), thiscontext);
                    clientClass.execute();
                    isAdmin = false;
                }
                startActivity(intent);
            }
        });

        createServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MultiplayerChoiceActivity.this, PlayersThatJoined.class);
                serverClass = new ServerClass(7777);
                serverClass.execute();
                startActivity(intent1);
                isAdmin = true;
            }
        });

        returnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MultiplayerChoiceActivity.this, MenuPrincipal.class);
                startActivity(intent2);
            }
        });
    }
}
