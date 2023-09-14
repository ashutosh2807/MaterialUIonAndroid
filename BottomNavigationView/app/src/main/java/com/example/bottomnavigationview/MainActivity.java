package com.example.bottomnavigationview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bottomnavigationview.fragments.homeFragment;
import com.example.bottomnavigationview.fragments.profileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView btnv = findViewById(R.id.bottomNavigationView);
        btnv.setBackground(null);

        replaceFragment(new homeFragment());
        btnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.home) {
                    fragment = new homeFragment();
                } else if (item.getItemId() == R.id.profile) {
                    fragment = new profileFragment();
                }

                // Replace the current fragment with the selected fragment
                if (fragment != null) {
                    replaceFragment(fragment);
                }

                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.floatingbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showDialog(); }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data in the homeFragment when MainActivity is resumed
        homeFragment fragment = (homeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        AutoCompleteTextView textView = fragment.getView().findViewById(R.id.autocomplete_country);
        textView.setText("");
        textView.clearFocus();
        if (fragment != null) {
            fragment.fetchData();
        }
    }

    private  void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private  void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);
        TextView tvDeletePatient = dialog.findViewById(R.id.tvDeletePatient);
        tvDeletePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, deletePatientData.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        TextView tvMarkVisit = dialog.findViewById(R.id.tvMarkVisit);
        tvMarkVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, markVisit.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        TextView textView = dialog.findViewById(R.id.Addpatient);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPatient.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        ImageView imageView = dialog.findViewById(R.id.cancelButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }
}