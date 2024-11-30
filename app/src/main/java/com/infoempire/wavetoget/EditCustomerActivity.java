package com.infoempire.wavetoget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EditCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        EditCustomerAccount frag = new EditCustomerAccount();
        getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
    }
}