package com.example.arduino_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPower = findViewById(R.id.btn_power);
        imgBulb = findViewById(R.id.img_bulb);

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
                        }else {
                            STATUS = "ON";
                            btnPower.setText("MATIKAN");
                            imgBulb.setImageResource(R.drawable.ic_highlight_green_24dp);
                            updateStatus("ON");
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

                    }
                });
    }
}
