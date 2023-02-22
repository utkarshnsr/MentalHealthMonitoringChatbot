package com.example.mentalhealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


/*
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
*/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static int questionIndex = 0;
    public static String[] questions = {"Tell me something interesting that happened to you today..", "How are you feeling?", "Are you excited about today?"};
    PreviewView previewView;
    Button recordButton;
    private VideoCapture videoCapture;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        previewView = findViewById(R.id.previewView);

        recordButton = findViewById(R.id.recordVideoButton);

        recordButton.setText("start recording");

        recordButton.setOnClickListener(this);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
                /*MainActivity m1 = new MainActivity();
                Thread connectToDB = new Thread(m1);
                connectToDB.start();*/
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());


    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        try {
            cameraProvider.unbindAll();
            CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
            Preview preview = new Preview.Builder()
                    .build();

            preview.setSurfaceProvider(previewView.getSurfaceProvider());


            // Video capture use case
            videoCapture = new VideoCapture.Builder()
                    .setVideoFrameRate(30)
                    .build();


            //bind to lifecycle:

            cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, videoCapture);
        } catch (Exception e) {
            System.out.println("IN START CAMERA X EXCEPTION");
        }
    }

    @SuppressLint("RestrictedApi")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recordVideoButton:
                if (recordButton.getText() == "start recording") {
                    System.out.println("IN RECORDING");
                    recordButton.setText("stop recording");
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    recordVideo();
                } else {
                    recordButton.setText("start recording");
                    videoCapture.stopRecording();
                }
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void recordVideo() {
        System.out.println("IN RECORD VIDEO");
        if (videoCapture != null) {


            long timestamp = System.currentTimeMillis();

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

            try {

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                videoCapture.startRecording(
                        new VideoCapture.OutputFileOptions.Builder(
                                getContentResolver(),
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                contentValues
                        ).build(),
                        getExecutor(),
                        new VideoCapture.OnVideoSavedCallback() {
                            @Override
                            public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                                System.out.println(outputFileResults);
                                System.out.println("IN VIDEO SAVED");
                                Toast.makeText(MainActivity.this, "Video has been saved successfully.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                                Toast.makeText(MainActivity.this, "Error saving video: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    /*
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

     */


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