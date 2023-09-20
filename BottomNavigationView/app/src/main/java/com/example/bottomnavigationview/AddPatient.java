package com.example.bottomnavigationview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationview.profileData.IServices;
import com.example.bottomnavigationview.profileData.dbSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class AddPatient extends AppCompatActivity {

    private boolean dataFetched = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        Button btn = findViewById(R.id.btnDob);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePatientData();
                finish();
            }
        });

        dbSingleton db = dbSingleton.getInstance();
        db.setServiceInterface(new IServices() {
            @Override
            public void onServiceFetched(Map<String, Object> data) {
                Set<String> keys = data.keySet();

                for (String key : keys) {
                    Log.d("TAG", key);
                }
            }
            @Override
            public void onServiceNotFetched() {
                Toast.makeText(getApplicationContext(),"No data Found",Toast.LENGTH_SHORT).show();
            }
        });

        db.getServices();

    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.datepicker);
        DatePicker dtp = dialog.findViewById(R.id.date);
        dtp.setMaxDate(new Date().getTime());
        Button btnselect = dialog.findViewById(R.id.btnselect);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txt = findViewById(R.id.etDob);
                txt.setText(String.valueOf(dtp.getDayOfMonth()) + "/" + String.valueOf(dtp.getMonth() + 1) + "/" + String.valueOf(dtp.getYear()));
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void savePatientData() {
        EditText Name = findViewById(R.id.etName);
        EditText FName = findViewById(R.id.etFname);
        EditText OPD_ID = findViewById(R.id.etOPD);
        EditText Phone = findViewById(R.id.etPhone);
        EditText Email = findViewById(R.id.etmail);
        EditText Address = findViewById(R.id.etAddress);
        TextView Dob = findViewById(R.id.etDob);
        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);

        int selectedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String selectedGender = selectedRadioButton.getText().toString();

        dbSingleton.getInstance().saveToFirestore(Name.getText().toString(),FName.getText().toString(),
                OPD_ID.getText().toString(),Phone.getText().toString(),selectedGender,Email.getText().toString(),Address.getText().toString(),
                DateConversion(Dob.getText().toString())
        );
    }

    private Date DateConversion(String s) {
        String dateString = s;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
           return  null;
        }
    }
}