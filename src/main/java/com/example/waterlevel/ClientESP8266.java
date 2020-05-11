package com.example.waterlevel;

import android.os.AsyncTask;

import java.io.*;
import java.net.Socket;


class ClientESP8266 implements Runnable {

    Socket socket;
    String ip, exceptionMessage = "Timeout";
    String responseServer = null;
    int port;

    ClientESP8266(String ip, int port){
        this.ip = ip;
        this.port=port;
    }


    @Override
    public void run() {

        try {
            socket = new Socket(ip, port);

            // get response
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            responseServer = br.readLine();

            br.close();
            socket.close();
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
            responseServer = null; }
    }

    public String getServerResponse(){
        return this.responseServer;
    }
}