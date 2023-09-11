package com.example.bottomnavigationview.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavigationview.R;
import com.example.bottomnavigationview.adapter.recyclerAdapter;
import com.example.bottomnavigationview.profileData.OnDocumentFetchListener;
import com.example.bottomnavigationview.profileData.dbSingleton;
import com.example.bottomnavigationview.profileData.profile;
import com.example.bottomnavigationview.viewModels.HomeViewModel;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class homeFragment extends Fragment {
    private recyclerAdapter adptr;
    private HomeViewModel viewModel;
    private dbSingleton db;
    private boolean dataFetched = false;

    public homeFragment() {
        // Required empty public constructor
    }

    public static homeFragment newInstance() {
        return new homeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = dbSingleton.getInstance();
        // Initialize the ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView rView = rootView.findViewById(R.id.recyclerView);

        // Initialize the adapter here
        adptr = new recyclerAdapter(viewModel.getProfileList(), requireActivity());
        rView.setAdapter(adptr);
        rView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Fetch data only if it hasn't been fetched yet
        if (viewModel.getProfileList().isEmpty()) {
            fetchData();
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Refresh the data if needed
        if (!dataFetched) {
            fetchData();
            dataFetched = true;
        }

        // Notify the adapter that the data has changed
        if (adptr != null) {
            adptr.notifyDataSetChanged();
        }
    }

    private void fetchData() {
        // Fetch new data from Firebase
        db.setDocumentFetchListener(new OnDocumentFetchListener() {
            @Override
            public void onDocumentsFetched(List<Map<String, Object>> documentList) {
                // Handle the retrieved data here
                List<profile> newData = new ArrayList<>();

                for (Map<String, Object> doc : documentList) {
                    profile data = new profile(
                            doc.get("OPD_ID").toString(),
                            doc.get("Name").toString(),
                            doc.get("FatherName").toString(),
                            doc.get("Gender").toString(),
                            doc.get("Phone_number").toString(),
                            doc.get("Address").toString(),
                            doc.get("Email").toString(),
                            ((Timestamp) doc.get("Dob")).toDate()
                    );
                    newData.add(data);
                }

                // Update the ViewModel with the new data
                viewModel.setProfileList(newData);

                // Notify the adapter that the data has changed
                if (adptr != null) {
                    adptr.notifyDataSetChanged();
                }
            }
        });

        db.getDocuments();
    }
}

