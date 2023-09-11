package com.example.bottomnavigationview.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavigationview.R;
import com.example.bottomnavigationview.adapter.recyclerAdapter;
import com.example.bottomnavigationview.profileData.OnDocumentFetchListener;
import com.example.bottomnavigationview.profileData.dbSingleton;
import com.example.bottomnavigationview.profileData.profile;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class homeFragment extends Fragment {


    public homeFragment() {
        // Required empty public constructor
    }

    public static homeFragment newInstance() {
        return new homeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

//             TableLayout tableLayout = rootView.findViewById(R.id.tableData);
        RecyclerView rView = rootView.findViewById(R.id.recyclerView);
        dbSingleton db = dbSingleton.getInstance();
        List<profile> profileList = new ArrayList<>();
        db.setDocumentFetchListener(new OnDocumentFetchListener() {
            @Override
            public void onDocumentsFetched(List<Map<String, Object>> documentList) {
                // Handle the retrieved data here

                for (Map<String, Object> doc : documentList) {
                        profile data = new profile(
                                doc.get("OPD_ID").toString(),
                                doc.get("Name").toString(),
                                doc.get("FatherName").toString(),
                                doc.get("Gender").toString(),
                                doc.get("Phone_number").toString(),
                                doc.get("Address").toString(),
                                doc.get("Email").toString(),
                                new Date()
                        );
                        profileList.add(data);

                }
                recyclerAdapter adptr = new recyclerAdapter(profileList,getActivity());
                rView.setAdapter(adptr);
                rView.setLayoutManager(new LinearLayoutManager(requireContext()));

            }
        });


        db.getDocuments();

        return rootView;
    }


}
