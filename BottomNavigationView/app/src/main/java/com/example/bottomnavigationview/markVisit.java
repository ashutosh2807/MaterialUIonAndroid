package com.example.bottomnavigationview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bottomnavigationview.adapter.recyclerAdapter;
import com.example.bottomnavigationview.profileData.IServices;
import com.example.bottomnavigationview.profileData.OnDocumentFetchListener;
import com.example.bottomnavigationview.profileData.dbSingleton;
import com.example.bottomnavigationview.profileData.profile;
import com.example.bottomnavigationview.viewModels.HomeViewModel;
import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class markVisit extends AppCompatActivity {
    private HomeViewModel viewModel;
    private dbSingleton db;
    private  List<String> selection_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = dbSingleton.getInstance();

        db.setServiceInterface(new IServices() {
            @Override
            public void onServiceFetched(Map<String, Object> data) {
                List<Integer> price = new ArrayList<>();
                Set<String> keys = data.keySet();
                List<String> items = new ArrayList<>();

                for (String key : keys) {
                    items.add(key);
                    price.add(Integer.valueOf(data.get(key).toString()));
                }
                RelativeLayout rlServices = findViewById(R.id.rlServices);
                rlServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMultipleSelects(items,price);
                    }
                });
            }
            @Override
            public void onServiceNotFetched() {
                Toast.makeText(getApplicationContext(),"No data Found",Toast.LENGTH_SHORT).show();
            }
        });

        db.getServices();
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setContentView(R.layout.activity_mark_visit);
        Button btnDate = findViewById(R.id.btnDate);
        fetchData();
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        Button btnVisit = findViewById(R.id.btnVisit);
        btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView selectedDate = findViewById(R.id.selectedDate);
                TextView txtOPD =  findViewById(R.id.tvOPD);
                EditText amount = findViewById(R.id.etAmount);
                EditText etNote = findViewById(R.id.etNote);
                if(selectedDate.getText().toString().equals("date")){
                    Toast.makeText(getBaseContext(), "Please select Date", Toast.LENGTH_SHORT).show();
                }
                else if(txtOPD.getText().toString().trim().equals("")){
                    Toast.makeText(getBaseContext(), "Please Select a Patient", Toast.LENGTH_SHORT).show();
                }
                else{
                    Map<String,List<String>> data = new HashMap<>();
                    data.put(selectedDate.getText().toString(),selection_list);
                    try{
                        boolean check =  db.markVisitDate(
                                viewModel.getProfileWithOPD(txtOPD.getText().toString().trim())
                                ,selectedDate.getText().toString(),data,ValidateEditTexts(amount),ValidateEditTexts(etNote));
                        if(check){
                            Toast.makeText(getBaseContext(), "Date Marked Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getBaseContext(), "Got an error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        boolean check =  db.markVisitDate(
                                viewModel.getProfileWithOPD(txtOPD.getText().toString().trim())
                                ,selectedDate.getText().toString(),data, amount.getText().toString(),"-");
                        if(check){
                            Toast.makeText(getBaseContext(), "Date Marked Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getBaseContext(), "Got an error.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }

    private String ValidateEditTexts(EditText texts){

        try{
            if(texts.getText().toString().trim() == ""){
                return  "";
            }
            else {
                return  texts.getText().toString().trim();
            }
        }
        catch (Exception e){
            return  "";
        }

    }
    private  void showMultipleSelects(List<String> items,List<Integer>  price){
        String[] listItems = items.toArray(new String[0]);
        Integer[] prices = price.toArray(new Integer[0]);
        selection_list = new ArrayList<>();

        boolean[] checkedItems = new boolean[listItems.length];
        List<String> selectedItems = Arrays.asList(listItems);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Services");
        builder.setIcon(R.drawable.arrowdown);

        // now this is the function which sets the alert dialog for multiple item selection ready
        builder.setMultiChoiceItems(listItems, checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
            String currentItem = selectedItems.get(which);
        });

        // alert dialog shouldn't be cancellable
        builder.setCancelable(false);

        // handle the positive button of the dialog
        builder.setPositiveButton("Done", (dialog, which) -> {
            Integer TotalPrice = 0;
            Map<String,List<String>> data = new HashMap<>();

            String selected_items = "Select Services";
            TextView tvSelectedservices = findViewById(R.id.tvSelectedservices);
            EditText etAmount = findViewById(R.id.etAmount);
            selected_items = "";
            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) {
                    selected_items += listItems[i]+", ";
                    selection_list.add(listItems[i]);
                    TotalPrice += prices[i];
                }
            }

            if(selected_items.length() > 2){
                tvSelectedservices.setText(selected_items.toUpperCase().substring(0,selected_items.length()-2));
            }
            else {
                tvSelectedservices.setText("Select Services");
            }

            etAmount.setText(String.valueOf(TotalPrice));
        });

        // handle the negative button of the alert dialog
        builder.setNegativeButton("CANCEL", (dialog, which) -> {});

        // handle the neutral button of the dialog to clear the selected items boolean checkedItem
        builder.setNeutralButton("CLEAR ALL", (dialog, which) -> {
            Arrays.fill(checkedItems, false);
        });

        // create the builder
        builder.create();

        // create the alert dialog with the alert dialog builder instance
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

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
                            doc.get("Age").toString(),
                            (Map<String,List<String>> ) doc.get("Services"),
                            doc.get("Amount").toString(),
                            doc.get("Note").toString(),
                            ( List<Timestamp> ) doc.get("Visit_date")
                    );
                    newData.add(data);
                }

                viewModel.setProfileList(newData);

                AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.etOPD);
                List<String> profile_names = new ArrayList<>();
                List<String> Opd_profiles = new ArrayList<>();
                for(profile p : viewModel.getProfileList()){
                    Opd_profiles.add(p.getOPD_ID());
                    profile_names.add(p.getName()+" : "+p.getOPD_ID());
                }

                String[] Opd_profilesArray = Opd_profiles.toArray(new String[0]);
                String[] profile_namesArray = profile_names.toArray(new String[0]);

                ArrayAdapter<String> opdadapter =
                        new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, Opd_profilesArray);
                ArrayAdapter<String> nameadapter =
                        new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, profile_namesArray);

                textView.setAdapter(opdadapter);

                textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AutoCompleteTextView etName = (AutoCompleteTextView) findViewById(R.id.etName);
                        etName.setText("");
                        String selectedItem = (String) adapterView.getItemAtPosition(i);
                        textView.setText(selectedItem);
                        textView.clearFocus();

                        profile data = viewModel.getProfileWithOPD(selectedItem);

                        TextView txt =  findViewById(R.id.tvName);
                        txt.setText(data.getName());

                        TextView txtOPD =  findViewById(R.id.tvOPD);
                        txtOPD.setText(data.getOPD_ID());

                        TextView txtPhone =  findViewById(R.id.tvPhone);
                        txtPhone.setText(data.getPhone_number());

                        TextView tvFatherName =  findViewById(R.id.tvFatherName);
                        tvFatherName.setText(data.getFatherName());

                    }
                });

                AutoCompleteTextView etName = (AutoCompleteTextView) findViewById(R.id.etName);
                etName.setAdapter(nameadapter);
                etName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.etOPD);
                        textView.setText("");
                        String selectedItem = (String) adapterView.getItemAtPosition(i);
                        etName.setText(selectedItem);
                        etName.clearFocus();

                        profile data = viewModel.getProfileWithNameOPD(selectedItem);

                        TextView txt =  findViewById(R.id.tvName);
                        txt.setText(data.getName());

                        TextView txtOPD =  findViewById(R.id.tvOPD);
                        txtOPD.setText(data.getOPD_ID());

                        TextView txtPhone =  findViewById(R.id.tvPhone);
                        txtPhone.setText(data.getPhone_number());

                        TextView tvFatherName =  findViewById(R.id.tvFatherName);
                        tvFatherName.setText(data.getFatherName());
                    }
                });
            }

        });
        db.getDocuments();
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.datecard_markdate);
        DatePicker dtp = dialog.findViewById(R.id.date);
        TimePicker tm = dialog.findViewById(R.id.timePicker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtp.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    tm.setVisibility(View.VISIBLE);
                }
            });
        }

        String textToUnderline = "Now";
        SpannableString spannableString = new SpannableString(textToUnderline);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, 0, textToUnderline.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int color = Color.RED;
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, 0, textToUnderline.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Button today = dialog.findViewById(R.id.btntoday);

        today.setText(spannableString);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtselected =  findViewById(R.id.selectedDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:00");
                txtselected.setText(dateFormat.format(new Date()));
                dialog.dismiss();
            }
        });
        dtp.setMaxDate(new Date().getTime());
        Button btnselect = dialog.findViewById(R.id.btnselect);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtselected = findViewById(R.id.selectedDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                String selectedDate = String.format(
                        "%02d-%02d-%04d %02d:%02d:%02d",
                        dtp.getDayOfMonth(),
                        dtp.getMonth() + 1,
                        dtp.getYear(),
                        tm.getHour(),
                        tm.getMinute(),
                        0  // Set seconds to 0 or adjust as needed
                );
                txtselected.setText(selectedDate);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
}