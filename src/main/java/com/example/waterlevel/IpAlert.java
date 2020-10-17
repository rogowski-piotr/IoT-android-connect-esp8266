package com.example.waterlevel;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class IpAlert {

    static AlertDialog create(final Activity activity) {
        // creating alert to change ip address
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setIcon(R.drawable.ic_launcher_foreground);
        builder.setMessage("Setup ip address");

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        // set positive buttons
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int a) {
                String ip = input.getText().toString();
                MainActivity.setIp(ip);
                Toast.makeText(activity, "address added successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }

}
