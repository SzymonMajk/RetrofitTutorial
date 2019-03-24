package com.tuts.prakash.retrofittutorial.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tuts.prakash.retrofittutorial.R;

public class ReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        String name = getIntent().getStringExtra("NAME");
        TextView view = findViewById(R.id.title);
        view.setText("Reserving: " + name);
    }

    public void reserve(View view){
        Toast.makeText(getApplicationContext(),
                "Reserved!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
