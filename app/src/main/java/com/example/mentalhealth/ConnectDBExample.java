package com.example.mentalhealth;



import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class ConnectDBExample implements Runnable {

    public static void main(String[] args) {
        ConnectDBExample obj = new ConnectDBExample();
        Thread connecToDB = new Thread(obj);
        connecToDB.start();
    }

    public void run() {
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
}



