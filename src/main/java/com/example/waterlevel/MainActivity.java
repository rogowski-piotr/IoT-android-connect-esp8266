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

    EditText input;
    AlertDialog ad;
    static String IP = "192.168.1.17";
    static final int PORT = 50007;
    static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        Button buttonSetup = findViewById(R.id.button2);

        button.setOnClickListener(this);
        buttonSetup.setOnClickListener(this);

        TextView textView = findViewById(R.id.textView);
        textView.setText("Press the MEASURE button");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button: {
                TextView textView = findViewById(R.id.textView);
                TextView textViewResult1 = findViewById(R.id.textView2);

                ClientESP8266 client = new ClientESP8266(IP, PORT);
                Thread clientThread = new Thread(client);
                clientThread.start();

                // wait for response
                try { clientThread.join(4000);
                } catch (InterruptedException e) {
                    client.exceptionMessage = e.getMessage(); }

                // show the result
                if (client.responseServer != null) {
                    int levelPercent = waterLevelPercent(client.getServerResponse());
                    int levelLiters = waterLevelLiters(client.getServerResponse());
                    textView.setText("");
                    textViewResult1.setText("Left:\n\n" + levelPercent + "%\n" + levelLiters + " liters");
                    Toast.makeText(this, "measurement number: " + String.valueOf(counter++), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "measurement error", Toast.LENGTH_SHORT).show();
                    textViewResult1.setText("");
                    textView.setText("Connecting error, try again..\n" + client.exceptionMessage); }
                break;
            }

            case R.id.button2: {
                // creating alert to change IP address
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.ic_launcher_foreground);
                builder.setMessage("Setup IP address");

                input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(input);

                // set positive buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int a) {
                        IP = input.getText().toString();
                        Toast.makeText(getApplicationContext(), "address added successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);

                ad = builder.create();
                ad.show();
                break;
            }
        }
    }


    int waterLevelLiters(String response) {
        double h1 = Double.valueOf(response) / 10;
        double pp = 490.2;
        double h2 = 20.4 - h1;
        return (int) (h2 * pp);
    }

    int waterLevelPercent(String response) {
        int v2 = waterLevelLiters(response);
        return 100 * v2 / 10000;
    }

}
