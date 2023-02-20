package com.example.mentalhealth;
import static com.mongodb.client.model.Filters.eq;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.util.Iterator;
import java.util.logging.Logger;

public class MongoClientConnectionExample {
    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String connectionString = "mongodb+srv://urajkumar3:" +"Uk110901" + "@mentalhealthcluster.fgc7br2.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("videoRecords");
        MongoCollection<Document> collection = database.getCollection("Questions");
        FindIterable<Document> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }


}