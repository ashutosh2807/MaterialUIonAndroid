package com.example.bottomnavigationview.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.bottomnavigationview.R;
import com.example.bottomnavigationview.profileData.OnDocumentFetchListener;
import com.example.bottomnavigationview.profileData.dbSingleton;
import com.example.bottomnavigationview.profileData.fetchData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public  class homeFragment extends Fragment  {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TableLayout tableLayout = rootView.findViewById(R.id.tableData);

        dbSingleton db = dbSingleton.getInstance();
        db.setDocumentFetchListener(new OnDocumentFetchListener() {
            @Override
            public void onDocumentsFetched(List<Map<String, Object>> documentList) {
                // Handle the retrieved data here
                for (Map<String, Object> doc : documentList) {
                    TableRow tableRow = new TableRow(getContext());
                    for (Map.Entry<String, Object> entry : doc.entrySet()) {
                        TextView textView = new TextView(getContext());
                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT, 1f);

                        textView.setLayoutParams(params);
                        textView.setText(doc.get(entry.getKey()).toString());
                        tableRow.addView(textView);
                    }
                    tableLayout.addView(tableRow);
                }

            }
        });

        // Call getDocuments
        db.getDocuments();
        return rootView;
    }


}