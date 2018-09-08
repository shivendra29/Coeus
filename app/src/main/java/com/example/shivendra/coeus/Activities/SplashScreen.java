package com.example.shivendra.coeus.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shivendra.coeus.R;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        try {
            sleep(1250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();


    }
}
