package com.example.bottomnavigationview.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavigationview.R;
import com.example.bottomnavigationview.adapter.DialogAdapter;
import com.example.bottomnavigationview.adapter.recyclerAdapter;
import com.example.bottomnavigationview.profileData.OnDocumentFetchListener;
import com.example.bottomnavigationview.profileData.dbSingleton;
import com.example.bottomnavigationview.profileData.profile;
import com.example.bottomnavigationview.viewModels.HomeViewModel;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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

        adptr.setOnItemClickListener(new recyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClicking(int position) {
                profile pdata =  viewModel.getProfile(position);
                showDialog(pdata);
            }
        });


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

    private  void showDialog(profile data){
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.detailed_dialog);

        // Find and populate views in the dialog with pdata's data
        TextView OPD = dialog.findViewById(R.id.tvOPD);
        OPD.setText(data.getOPD_ID());

        TextView Name = dialog.findViewById(R.id.tvName);
        Name.setText(data.getName());

        TextView tvPhone = dialog.findViewById(R.id.tvPhone);
        tvPhone.setText(data.getPhone_number());

        TextView tvGender = dialog.findViewById(R.id.tvGender);
        tvGender.setText(data.getGender());

        TextView tvTotalVisits = dialog.findViewById(R.id.tvTotalVisits);
        tvTotalVisits.setText(String.valueOf(data.getVisit_dates().size()));

        LinearLayout layout = dialog.findViewById(R.id.LinearBase);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        List<Timestamp> visits = data.getVisit_dates();

        Collections.reverse(visits);

        RecyclerView rView = dialog.findViewById(R.id.RecyclerDialog);
        DialogAdapter Dadptr = new DialogAdapter(visits,getActivity());
        rView.setAdapter(Dadptr);
        rView.setLayoutManager(new LinearLayoutManager(requireContext()));

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    public void fetchData() {
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
                            doc.get("Age").toString(),
                            (List<Map<String,List<String>>>) doc.get("Services"),
                            doc.get("Amount").toString(),
                            doc.get("Note").toString(),
                            ( List<Timestamp> ) doc.get("Visit_date")
                    );
                    newData.add(data);
                }

                // Update the ViewModel with the new data
                viewModel.setProfileList(newData);

                AutoCompleteTextView textView = (AutoCompleteTextView) getView().findViewById(R.id.autocomplete_country);
                List<String> profile_names = new ArrayList<>();

                for(profile p : viewModel.getProfileList()){
                    profile_names.add(p.getOPD_ID());
                }
                profile_names.add("All");
                // Notify the adapter that the data has changed
                if (adptr != null) {
                    adptr.notifyDataSetChanged();
                }


                String[] profileNamesArray = profile_names.toArray(new String[0]);
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, profileNamesArray);
                textView.setAdapter(adapter);

                textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedItem = (String) adapterView.getItemAtPosition(i);
                        RecyclerView rView = getView().findViewById(R.id.recyclerView);
                        adptr = new recyclerAdapter(viewModel.getAProfileInList(selectedItem), requireActivity());
                        rView.setAdapter(adptr);
                        rView.setLayoutManager(new LinearLayoutManager(requireContext()));
                        adptr.setOnItemClickListener(new recyclerAdapter.onItemClickListener() {
                            @Override
                            public void onItemClicking(int position) {
                                if(selectedItem == "All"){
                                    textView.clearFocus();
                                    profile pdata = viewModel.getProfile(position);
                                    showDialog(pdata);
                                }
                                else {
                                    textView.clearFocus();
                                    profile pdata =  viewModel.getProfileWithOPD(selectedItem);
                                    showDialog(pdata);
                                }

                            }
                        });
                    }
                });
            }

        });

        db.getDocuments();
    }
}

