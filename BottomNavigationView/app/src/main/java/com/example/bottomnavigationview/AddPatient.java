package com.example.bottomnavigationview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BlendMode;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationview.profileData.IServices;
import com.example.bottomnavigationview.profileData.dbSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddPatient extends AppCompatActivity {
    private  Map<String,List<String>>  selected_services;
    private boolean dataFetched = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        RelativeLayout rlServices = findViewById(R.id.rlServices);

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
                List<Integer> price = new ArrayList<>();
                 Set<String> keys = data.keySet();
                List<String> items = new ArrayList<>();

                for (String key : keys) {
                    items.add(key);
                    price.add(Integer.valueOf(data.get(key).toString()));
                }

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

    }

    private  void showMultipleSelects(List<String> items,List<Integer>  price){
        String[] listItems = items.toArray(new String[0]);
        Integer[] prices = price.toArray(new Integer[0]);
        selected_services = new HashMap<>();
        List<String>selection_list = new ArrayList<>();

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
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:00");
            data.put(dateFormat.format(new Date()),selection_list);
            selected_services= data;
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

    private void savePatientData() {
        EditText Name = findViewById(R.id.etName);
        EditText FName = findViewById(R.id.etFname);
        EditText OPD_ID = findViewById(R.id.etOPD);
        EditText Phone = findViewById(R.id.etPhone);
        EditText Age = findViewById(R.id.etAge);
        EditText Address = findViewById(R.id.etAddress);
        EditText etAmount = findViewById(R.id.etAmount);
        EditText etNote = findViewById(R.id.etNote);

            RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
            String selectedGender = "";
            int selectedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
            try{
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                selectedGender = selectedRadioButton.getText().toString();
            }catch (Exception e){
                selectedGender = "";
            }

            dbSingleton.getInstance().saveToFirestore(
                    ValidateEditTexts(Name),
                    ValidateEditTexts(FName),
                    ValidateEditTexts(OPD_ID),
                    ValidateEditTexts(Phone),
                    selectedGender,
                    ValidateEditTexts(Age),
                    selected_services,
                    ValidateEditTexts(etAmount),
                    ValidateEditTexts(etNote),
                    ValidateEditTexts(Address)
            );


    }

    private String ValidateEditTexts(EditText texts){
        if(texts.getText().toString().trim() == ""){
            return  "";
        }
        else {
            return  texts.getText().toString().trim();
        }
    }

}