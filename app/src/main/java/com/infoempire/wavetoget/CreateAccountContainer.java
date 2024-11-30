package com.infoempire.wavetoget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CreateAccountContainer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_container);

        AccountCreationPrompt frag = new AccountCreationPrompt();
        getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
    }
}