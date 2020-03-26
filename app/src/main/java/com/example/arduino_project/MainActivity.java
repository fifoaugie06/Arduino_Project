package com.example.arduino_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button btnPower;
    ImageView imgBulb;
    String STATUS;
    DatabaseReference database;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPower = findViewById(R.id.btn_power);
        imgBulb = findViewById(R.id.img_bulb);

        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Waiting Respons...");

        database = FirebaseDatabase.getInstance().getReference();
        database.child("LED_STATUS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                STATUS = dataSnapshot.getValue(String.class);

                // First set status
                switch (STATUS){
                    case "OFF":
                        btnPower.setText("NYALAKAN");
                        imgBulb.setImageResource(R.drawable.ic_highlight_red_24dp);
                        break;
                    case "ON":
                        btnPower.setText("MATIKAN");
                        imgBulb.setImageResource(R.drawable.ic_highlight_green_24dp);
                        break;
                }

                // Set when click
                btnPower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (STATUS == "ON"){
                            STATUS = "OFF";
                            updateStatus("OFF");
                            imgBulb.setImageResource(R.drawable.ic_highlight_red_24dp);
                            btnPower.setText("NYALAKAN");
                            pd.show();
                        }else {
                            STATUS = "ON";
                            btnPower.setText("MATIKAN");
                            imgBulb.setImageResource(R.drawable.ic_highlight_green_24dp);
                            updateStatus("ON");
                            pd.show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateStatus(String status) {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("LED_STATUS")
                .setValue(status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                            }
                        }, 1500);
                    }
                });
    }
}
