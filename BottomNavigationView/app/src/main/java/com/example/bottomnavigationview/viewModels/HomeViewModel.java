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

    public profile getProfile(int position){
        return  profileList.get(position);
    }
    public void setProfileList(List<profile> newList) {
        profileList.clear();
        profileList.addAll(newList);
    }
}
