package com.example.bottomnavigationview.profileData;

import static android.content.ContentValues.TAG;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class profile {
    private String OPD_ID;
    private String Name;
    private String FatherName;
    private String Gender;
    private String Phone_number;
    private String Address;
    private String Age;

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    private Map<String,List<String>> services;

    public Map<String,List<String>> getServices() {
        return services;
    }

    public void setServices(Map<String,List<String>> services) {
        this.services = services;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    private  String amount;
    private  String note;
    private Date Registration_Time;

    private  List<Timestamp> visit_dates;

    public  List<Timestamp>  getVisit_dates() {
        return visit_dates;
    }

    public Timestamp getLastVisit(){
        return visit_dates.get(visit_dates.size()-1);
    }
    public void setVisit_dates( List<Timestamp>  visit_dates) {
        this.visit_dates = visit_dates;
    }

    public String getOPD_ID() {
        return OPD_ID;
    }

    public void setOPD_ID(String OPD_ID) {
        this.OPD_ID = OPD_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(String phone_number) {
        Phone_number = phone_number;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Date getRegistration_Time() {
        return Registration_Time;
    }

    public void setRegistration_Time(Date registration_Time) {
        Registration_Time = registration_Time;
    }

    public profile(String OPD_ID, String Name, String FatherName, String Gender,
                   String Phone_number, String Address, String Age, Map<String,List<String>> Services,String amount, String note, List<Timestamp>  Visit_dates) {
        this.OPD_ID = OPD_ID;
        this.Name = Name;
        this.FatherName = FatherName;
        this.Gender = Gender;
        this.Phone_number = Phone_number;
        this.Address = Address;
        this.Age = Age;
        this.services = Services;
        this.amount = amount;
        this.note = note;
        this.Registration_Time = new Date();
        this.visit_dates = Visit_dates;
    }


    }
