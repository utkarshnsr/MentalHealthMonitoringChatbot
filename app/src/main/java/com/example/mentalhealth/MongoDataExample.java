package com.example.mentalhealth;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class MongoDataExample {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"collection\":\"Questions\",\n    \"database\":\"videoRecords\",\n    \"dataSource\":\"MentalHealthCluster\",\n    \"projection\": {\"question\": 1}\n\n}");
        Request request = new Request.Builder()
                .url("https://us-east-1.aws.data.mongodb-api.com/app/data-aibcf/endpoint/data/v1/action/find")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Control-Request-Headers", "*")
                .addHeader("api-key", "FlALV8Uy8CjoZTNGMSRJevh7leeBWMfSmDGqsZzfrR83gUCgCUELAjxCNdN6PDO1")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println("hi");
        System.out.println(response.body().string());
    }
}
