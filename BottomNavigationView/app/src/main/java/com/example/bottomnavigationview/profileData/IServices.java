package com.example.bottomnavigationview.profileData;

import java.util.Map;

public interface IServices {
    void onServiceFetched(Map<String,Object> data);

    void onServiceNotFetched();
}
