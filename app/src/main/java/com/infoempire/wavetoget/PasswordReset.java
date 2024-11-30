package com.infoempire.wavetoget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordReset extends AppCompatActivity {

    EditText email;
    TextView errormessage;
    Button send;
    Spinner storesSpinner;
    ImageView back;
    private Map<String, Integer> storeList = new HashMap<>();
    ArrayList<String> stores  = new ArrayList<String>();
    Integer selectedStore = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        send = findViewById(R.id.login);
        errormessage = findViewById(R.id.errormessage);
        storesSpinner = findViewById(R.id.spinner);
        back = findViewById(R.id.backbutton6);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        email = findViewById(R.id.editTextTextEmailAddress);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    GetStores();
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset();
            }
        });
        storesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                Log.d("spinner", stores.get(position));
                selectedStore = storeList.get(stores.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }
    private void GetStores() {

        stores.clear();
        storeList.clear();
        Log.d("test", "GetStores: ");
        if(email.getText().toString().isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PasswordReset.this,
                    android.R.layout.simple_spinner_item,stores);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            storesSpinner.setAdapter(adapter);
            return;
        }
        Map<String, String> fields = new HashMap<>();
        fields.put("email", email.getText().toString());
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.storeSelect(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("stores", plain_text_response);
                try {
                    JSONArray jArray = new JSONArray(plain_text_response);
                    for (int i=0; i < jArray.length(); i++)
                    {
                        try {
                            JSONObject oneObject = jArray.getJSONObject(i);
                            // Pulling items from the array
                            Integer store = oneObject.getInt("store");
                            Log.d("store", String.valueOf(store));
                            Integer usertype = oneObject.getInt("usertype");
                            Log.d("usertype", String.valueOf(usertype));
                            String store_displayname = oneObject.getString("store_displayname");
                            Log.d("store_displayname", store_displayname);
                            String typename = oneObject.getString("typename");
                            Log.d("typename", typename);
                            storeList.put(store_displayname  + " - " + typename,store);
                            stores.add(store_displayname + " - " + typename);

                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                    if(storeList.size() == 0)
                    {
                        errormessage.setText("No accounts associated with this email found...");
                    }
                    else
                    {
                        errormessage.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                ArrayAdapter<String>adapter = new ArrayAdapter<String>(PasswordReset.this,
                        android.R.layout.simple_spinner_item,stores);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                storesSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });
    }
    private void Reset() {
        errormessage.setText("");
        if(email.getText().toString().isEmpty())
        {
            errormessage.setText("Enter an email.");
            return;
        }
        if(selectedStore == 0)
        {
            errormessage.setText("Please select a store.");
            return;
        }
        Map<String, String> fields = new HashMap<>();
        fields.put("email", email.getText().toString());
        fields.put("store", selectedStore.toString());
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.sendReset(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("reset", "reset");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PasswordReset.this);
                builder1.setMessage("An email to reset your password will be sent to you shortly. Thank you for your patience.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Okay",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });
    }
}