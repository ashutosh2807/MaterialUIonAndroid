package com.example.bottomnavigationview.profileData;

import android.util.Log;

import com.example.bottomnavigationview.fragments.homeFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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
