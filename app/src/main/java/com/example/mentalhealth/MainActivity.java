package com.example.mentalhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startRecording(View view) {
        Toast.makeText(this,"recording started!",Toast.LENGTH_SHORT).show();
    }

    public void stopRecording(View view) {
        Toast.makeText(this,"recording stopped!",Toast.LENGTH_SHORT).show();
    }
}