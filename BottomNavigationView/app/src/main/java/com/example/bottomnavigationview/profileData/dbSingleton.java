package com.example.bottomnavigationview.profileData;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bottomnavigationview.fragments.homeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class dbSingleton {
    private OnDocumentFetchListener documentFetchListener;


    private IServices serviceInterface;
    public void setServiceInterface(IServices serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

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

    public  void getServices(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userDetailsRef = db.collection("ServicesWithPrice");
        DocumentReference doc = db.collection("ServicesWithPrice").document("DentalServices");
        // Retrieve documents from the collection
        doc.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();

                        if (serviceInterface != null) {
                            serviceInterface.onServiceFetched(data);
                        }
                    } else {
                        if (serviceInterface != null) {
                            serviceInterface.onServiceNotFetched();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure here
                    Log.e("TAG", "Error getting document: ", e);
                });
    }

    public void deletePatientData(String OPDID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collectionName = "UserDetails";
        String documentId = OPDID;
        DocumentReference documentRef = db.collection(collectionName).document(documentId);
        documentRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully deleted
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors here
                    }
                });
    }


    public boolean markVisitDate(profile data,String DateVisited,Map<String,List<String>> services,String amount,String Note){
        List<Timestamp> data_visited = data.getVisit_dates();
        String dateString = DateVisited;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Map<String,List<String>> services_get = services;
        for(String keys : data.getServices().keySet()){
                services_get.put(keys,data.getServices().get(keys));
        }

        try {
            Date date = dateFormat.parse(dateString);
            data_visited.add(new Timestamp(date));
            Map<String,Object> profileData = new HashMap<>();
            profileData.put("OPD_ID", data.getOPD_ID());
            profileData.put("Name", data.getName());
            profileData.put("FatherName", data.getFatherName());
            profileData.put("Gender", data.getGender());
            profileData.put("Phone_number", data.getPhone_number());
            profileData.put("Address", data.getAddress());
            profileData.put("Age", data.getAge());
            profileData.put("Services", services_get);
            profileData.put("Amount",amount);
            profileData.put("Note",Note);
            profileData.put("Registration_Time",  new Timestamp(new Date()));
            profileData.put("Visit_date", data_visited);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Map<String, Object>> pdata = new HashMap<String, Map<String, Object>>();
            pdata.put(data.getOPD_ID(),profileData);

            db.collection("UserDetails")
                    .document(data.getOPD_ID())
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
            return  true;
        } catch (ParseException e) {
            return false;
        }


    }

    public void saveToFirestore(String Name,String FatherName, String OPD_ID, String Phone_number,
                                String Gender,
                                String Age,Map<String,List<String>>  services,
                                String amount,String note, String Address) {
        List<Timestamp> data_visited = new ArrayList<>();
        data_visited.add(new Timestamp(new Date()));

        Map<String,Object> profileData = new HashMap<>();
        profileData.put("OPD_ID", OPD_ID);
        profileData.put("Name", Name);
        profileData.put("FatherName", FatherName);
        profileData.put("Gender", Gender);
        profileData.put("Phone_number", Phone_number);
        profileData.put("Address", Address);
        profileData.put("Age", Age);
        profileData.put("Services", services);
        profileData.put("Amount",amount);
        profileData.put("Note",note);
        profileData.put("Time",  new Timestamp(new Date()));
        profileData.put("Visit_date", data_visited);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
        data.put(OPD_ID,profileData);

        db.collection("UserDetails")
                .document(OPD_ID)
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
