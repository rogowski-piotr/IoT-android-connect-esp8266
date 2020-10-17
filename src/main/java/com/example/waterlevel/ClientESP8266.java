package com.example.waterlevel;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.*;
import java.net.Socket;


class ClientESP8266 extends AsyncTask<Void, Void, Void> {

    private String ip;
    private int port;
    private String exceptionMessage = "Timeout";
    private String responseServer = null;
    private MainActivity activity;
    private boolean isNumber = true;

    ClientESP8266(Activity activity, String ip, int port){
        this.activity = (MainActivity) activity;
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.displayWaitInfo();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (exceptionMessage == "Timeout" && responseServer != null) {
            activity.displayResult(responseServer);
        } else {
            activity.displayErrorInfo(exceptionMessage);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Socket socket = new Socket(ip, port);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            responseServer = br.readLine();

            br.close();
            socket.close();

            Integer.valueOf(responseServer);
        } catch (NumberFormatException ex) {
            exceptionMessage = "Incorrect response form sensor";
            responseServer = null;
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
            responseServer = null;
        }
        return null;
    }
}