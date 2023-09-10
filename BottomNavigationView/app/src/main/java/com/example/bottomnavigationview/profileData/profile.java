package com.example.bottomnavigationview.profileData;

import android.provider.ContactsContract;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class profile {
    private String OPD_ID;
    private String Name;
    private String FatherName;
    private String Gender;
    private String Phone_number;
    private String Address;
    private String Email;
    private Date DateOfBirth;
    private Date Registration_Time;


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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Date getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public Date getRegistration_Time() {
        return Registration_Time;
    }

    public void setRegistration_Time(Date registration_Time) {
        Registration_Time = registration_Time;
    }

    public profile(String OPD_ID, String Name, String FatherName, String Gender,
                   String Phone_number, String Address, String Email, Date Dob) {
        this.OPD_ID = OPD_ID;
        this.Name = Name;
        this.FatherName = FatherName;
        this.Gender = Gender;
        this.Phone_number = Phone_number;
        this.Address = Address;
        this.Email = Email;
        this.DateOfBirth = Dob;
        this.Registration_Time = new Date();
    }

    public Map getData(){
        Map map = new HashMap();
        map.put("OPD_ID",this.OPD_ID);
        map.put("Name",this.Name);
        map.put("FatherName",this.FatherName);
        map.put("Gender",this.Gender);
        map.put("Phone_number",this.Phone_number);
        map.put("Address",this.Address);
        map.put("Email",this.Email);
        map.put("DOB",this.DateOfBirth);
        map.put("Registration_time",this.Registration_Time);
        return  map;
    }
    }
