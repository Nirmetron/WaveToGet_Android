package com.infoempire.wavetoget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class StoreSettings extends AppCompatActivity {

    //ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_settings);

        StoreSettingsPage1 frag = new StoreSettingsPage1();
        getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
    }

}