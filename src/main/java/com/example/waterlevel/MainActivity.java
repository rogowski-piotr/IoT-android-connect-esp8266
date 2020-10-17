package com.example.waterlevel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String ip = "192.168.1.17";
    private static final int PORT = 50007;
    private static int measutmentCounter = 0;
    private TextView textViewInfo;
    private TextView textViewResult;
    private Button button;
    private Button buttonSetup;
    private EditText input;
    private AlertDialog ad;

    public static void setIp(String ip) {
        MainActivity.ip = ip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewInfo = findViewById(R.id.textViewInfo);
        textViewResult = findViewById(R.id.textViewResult);
        button = findViewById(R.id.button);
        buttonSetup = findViewById(R.id.button2);

        button.setOnClickListener(this);
        buttonSetup.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button:
                Toast.makeText(this, "measurement number: " + String.valueOf(++measutmentCounter), Toast.LENGTH_SHORT).show();
                ClientESP8266 client = new ClientESP8266(this, ip, PORT);
                client.execute();
                break;

            case R.id.button2: {
                IpAlert.create(this).show();
                SensorPosAlert.create(this).show();
                break;
            }
        }
    }

    public void displayWaitInfo() {
        textViewInfo.setText("Please wait..\nthe "  + measutmentCounter +" measurement is performing");
        textViewResult.setVisibility(View.INVISIBLE);
        textViewInfo.setVisibility(View.VISIBLE);
    }

    public void displayErrorInfo(String info) {
        textViewInfo.setText(info);
        textViewResult.setVisibility(View.INVISIBLE);
        textViewInfo.setVisibility(View.VISIBLE);
    }

    public void displayResult(String response) {
        int levelPercent = ResponseConverter.waterLevelPercent(response);
        int levelLiters = ResponseConverter.waterLevelLiters(response);
        int levelFillCm = ResponseConverter.waterLevelFillCm(response);

        textViewResult.setText("Left:\n\n" + levelPercent + "%\n" + levelLiters + " liters\n" + levelFillCm + " cm" + " / " + ResponseConverter.getSensorPos() + " cm");
        textViewInfo.setVisibility(View.INVISIBLE);
        textViewResult.setVisibility(View.VISIBLE);
    }

}
