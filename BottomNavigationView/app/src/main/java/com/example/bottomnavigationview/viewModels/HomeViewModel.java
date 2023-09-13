package com.example.bottomnavigationview.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.bottomnavigationview.profileData.profile;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private List<profile> profileList = new ArrayList<>();

    public List<profile> getProfileList() {
        return profileList;
    }

    public List<profile> getAProfileInList(String s){
        List<profile> profilelist1 = new ArrayList<>();
        if(s.equals("All")){
            return  profileList;
        }
        for(profile p: profileList){
            if(p.getOPD_ID().equals(s)){ // Use .equals() for string comparison
                profilelist1.add(p); // Add the matching profile to the result list
            }
        }
        return profilelist1;
    }

    public profile getProfileWithOPD(String OPD){
        for(profile pdata: profileList){
            if(pdata.getOPD_ID().equals(OPD)){ // Use .equals() for string comparison
               return pdata; // Add the matching profile to the result list
            }
        }
        return null;
    }
    public profile getProfile(int position){
        return  profileList.get(position);
    }
    public void setProfileList(List<profile> newList) {
        profileList.clear();
        profileList.addAll(newList);
    }
    public  void  truncate(){
        profileList = new ArrayList<>();
    }
}
