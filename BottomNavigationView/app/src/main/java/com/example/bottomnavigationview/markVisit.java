package com.example.bottomnavigationview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bottomnavigationview.adapter.recyclerAdapter;
import com.example.bottomnavigationview.profileData.OnDocumentFetchListener;
import com.example.bottomnavigationview.profileData.dbSingleton;
import com.example.bottomnavigationview.profileData.profile;
import com.example.bottomnavigationview.viewModels.HomeViewModel;
import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class markVisit extends AppCompatActivity {
    private HomeViewModel viewModel;
    private dbSingleton db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = dbSingleton.getInstance();
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
                if(selectedDate.getText().toString().equals("date")){
                    Toast.makeText(getBaseContext(), "Please select Date", Toast.LENGTH_SHORT).show();
                }
                else if(txtOPD.getText().toString().trim().equals("")){
                    Toast.makeText(getBaseContext(), "Please Select a Patient", Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean check =  db.markVisitDate(viewModel.getProfileWithOPD(txtOPD.getText().toString().trim()),selectedDate.getText().toString());
                    if(check){
                        Toast.makeText(getBaseContext(), "Date Marked Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getBaseContext(), "Got an error.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
                            (List<Map<String,List<String>>>) doc.get("Services"),
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                String selectedDate = String.format(
                        "%02d/%02d/%04d %02d:%02d:%02d",
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