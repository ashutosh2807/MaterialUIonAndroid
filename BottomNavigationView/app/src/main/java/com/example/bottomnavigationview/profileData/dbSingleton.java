package com.example.bottomnavigationview.profileData;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bottomnavigationview.fragments.homeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dbSingleton {
    private OnDocumentFetchListener documentFetchListener;

    // Setter for the document fetch listener
    public void setDocumentFetchListener(OnDocumentFetchListener listener) {
        this.documentFetchListener = listener;
    }
    private static dbSingleton instance = null;

    private dbSingleton() {
        // Private constructor to prevent external instantiation
    }

    public static dbSingleton getInstance() {
        if (instance == null) {
            synchronized (dbSingleton.class) {
                if (instance == null) {
                    instance = new dbSingleton();
                }
            }
        }
        return instance;
    }


    public void saveToFirestore() {
        List<Timestamp> data_visited = new ArrayList<>();
        for(int i= 0; i<5;i++){
            data_visited.add(new Timestamp(new Date()));
        }
        Map<String,Object> profileData = new HashMap<>();
        profileData.put("OPD_ID", "12345test");
        profileData.put("Name", "test");
        profileData.put("FatherName", "testFather");
        profileData.put("Gender", "M");
        profileData.put("Phone_number", "7693932810");
        profileData.put("Address", "Address");
        profileData.put("Email", "Email");
        profileData.put("Dob", new Timestamp(new Date()));
        profileData.put("Time",  new Timestamp(new Date()));
        profileData.put("Visit_date", data_visited);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
        data.put("Test3",profileData);

        db.collection("UserDetails")
                .document("Test3")
                .set(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // message
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // no message
                    }
                });
    }


    public void getDocuments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userDetailsRef = db.collection("UserDetails");

        // Retrieve documents from the collection
        userDetailsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    List<Map<String, Object>> documentList = new ArrayList<>();

                    for (DocumentSnapshot document : documents) {
                        Map<String, Object> data = document.getData();
                        documentList.add(data);
                    }

                    // Trigger the listener with the fetched data
                    if (documentFetchListener != null) {
                        documentFetchListener.onDocumentsFetched(documentList);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure here
                    Log.e("TAG", "Error getting documents: ", e);
                });
    }
}
