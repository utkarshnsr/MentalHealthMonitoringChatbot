package com.example.mentalhealth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Runnable {
    public static int questionIndex = 0;
    public static String[] questions = {"Tell me something interesting that happened to you today..","How are you feeling?","Are you excited about today?"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity m1 = new MainActivity();
        Thread connectToDB = new Thread(m1);
        connectToDB.start();


        findViewById(R.id.recordVideoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void run() {
        System.out.println("RUNNING SECOND THREAD");
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"collection\":\"Questions\",\n    \"database\":\"videoRecords\",\n    \"dataSource\":\"MentalHealthCluster\",\n    \"projection\": {\"question\": 1}\n\n}");
            Request request = new Request.Builder()
                    .url("https://us-east-1.aws.data.mongodb-api.com/app/data-aibcf/endpoint/data/v1/action/find")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Access-Control-Request-Headers", "*")
                    .addHeader("api-key", "8qP30Gmkf0Dij4dweva71Zh1ZpFVRP1GtdqJ8XKoXUAEVdKMbWO9h1NgyjY0kBWI")
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            System.out.println("Hi");
        } catch (Exception e) {
            System.out.println("INSIDE CATCH");
            System.out.println(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(data.getData().getPath()).show();
            /*
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(data.getData());
            videoView.start();
            builder.setView(videoView).show();
            */
        }
    }

    public void getNextQuestion(View view) {
        int nextIndex;
        if (MainActivity.questionIndex == (MainActivity.questions.length - 1)) {
            nextIndex = MainActivity.questions.length - 1;
        } else {
            nextIndex = Math.abs((MainActivity.questionIndex + 1)) % (MainActivity.questions.length);
        }
        TextView tv1 = (TextView)findViewById(R.id.questionTextView);
        tv1.setText(questions[nextIndex]);
        MainActivity.questionIndex = nextIndex;
    }

    public void getPreviousQuestion(View view) {
        int previousIndex;
        if (MainActivity.questionIndex == 0) {
            previousIndex = 0;
        } else {
            previousIndex = Math.abs((MainActivity.questionIndex - 1)) % (MainActivity.questions.length);
        }
        TextView tv1 = (TextView)findViewById(R.id.questionTextView);
        tv1.setText(questions[previousIndex]);
        MainActivity.questionIndex = previousIndex;
    }


}