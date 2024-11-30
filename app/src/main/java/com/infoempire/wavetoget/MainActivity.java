package com.infoempire.wavetoget;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    SendToken();
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                // Directly ask for the permission
            }
        }
    }

    private Button button;
    private Button testemail;
    private EditText email;
    private EditText password;
    private CheckBox checkBox;
    private Spinner spinner;
    TextView createAccount;
    TextView errormessage;
    ImageSlider bannerImageSlider;
    ArrayList<SlideModel> advertisementBanners = new ArrayList<>();
    ArrayList<String> advertisementBannerIds = new ArrayList<>();

    private Map<String, Integer> storeList = new HashMap<>();
    ArrayList<String> stores  = new ArrayList<String>();
    Integer selectedStore;
    public static ApiService apiService;
    TextView resetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("not successful", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("successful", token);
                        Account.token = token;
                    }
                });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.login);

        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        spinner = (Spinner) findViewById(R.id.spinner);
        createAccount = findViewById(R.id.createaccount);
        errormessage = findViewById(R.id.errormessage);
        resetPassword =  findViewById(R.id.resetpasswordtext);
        bannerImageSlider = findViewById(R.id.banner_slider);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                           @Override
                                           public void onFocusChange(View v, boolean hasFocus) {

                                               // When focus is lost check that the text field has valid values.

                                               if (!hasFocus) {
                                                   GetStores();
                                               }
                                           }
                                       });
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        checkBox.setChecked(sp1.getBoolean("check", false));
        if(checkBox.isChecked())
        {
            email.setText(sp1.getString("email", ""));
            password.setText(sp1.getString("pw", ""));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.wavetoget.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                LoadResetPassword();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                LoadCreateAccount();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                                                    SharedPreferences.Editor Ed = sp.edit();
                                                    Ed.putBoolean("check", isChecked);
                                                    Ed.commit();
                                                }
                                            }
        );
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        GetStores();
        fetchAllAdvertisementBanners();
    }
    public void LoadResetPassword(){
        Intent intent = new Intent(this, PasswordReset.class);
        startActivity(intent);
    }
    private void GetStores() {

        stores.clear();
        storeList.clear();
        if(email.getText().toString().isEmpty()) {
            ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_item,stores);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            return;
        }
            Map<String, String> fields = new HashMap<>();
            fields.put("email", email.getText().toString());
            fields.put("key", "KEY REMOVED");
            Call<String> call = apiService.storeSelect(fields);
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


                    ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item,stores);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        //}
    }
    public void LoadCreateAccount(){
            Intent intent = new Intent(this, CreateAccountContainer.class);
            startActivity(intent);
    }
    public void LoadNextPage(){
        if(Account.isCust) {
            Intent intent = new Intent(this, CustomerPage.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, StorePage.class);
            startActivity(intent);
        }
    }
    private void Login() {

        Map<String, String> fields = new HashMap<>();
        fields.put("action", "login");
        fields.put("email",  email.getText().toString());
        fields.put("password", password.getText().toString());
        fields.put("store", selectedStore.toString());
        Log.d("StoreNumber", selectedStore.toString());
        fields.put("key", "KEY REMOVED");
        Call<String> call = apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                if(plain_text_response == null)
                    return;
                Log.d("login key", plain_text_response);
                if(checkBox.isChecked())
                {
                    SaveCredentials();
                }
                if(!plain_text_response.equals("nosession") && !plain_text_response.equals("failed") && !plain_text_response.isEmpty())
                {
                    //openNewActivity();
                    Account.session = plain_text_response;
                    Account.email = email.getText().toString();
                    Account.password = password.getText().toString();
                    GetAccType();
                    errormessage.setText("");
                }
                else
                {
                    errormessage.setText("Incorrect password...");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void GetAccType() {

        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-user");
        fields.put("session",  Account.session);
        fields.put("key", "KEY REMOVED");
        Call<String> call = apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("login key", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                {
                    try {
                        JSONObject jObject = new JSONObject(_response);
                        Account.id = jObject.getInt("id");
                        Account.displayname = jObject.getString("displayname");
                        Account.accountType = jObject.getString("type");
                        Account.store = jObject.getInt("store");
                        askNotificationPermission();

                        Log.d("accountType", Account.accountType);
                        if(Account.accountType.equals("cardholder"))
                        {
                            Log.d("login cardholder", Account.accountType);
                            Account.isCust = true;
                        }
                        else
                        {
                            Account.isCust = false;
                        }
                        LoadNextPage();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    void SendToken(){
        Map<String, String> sendToken = new HashMap<>();
        sendToken.put("user", String.valueOf(Account.id));
        sendToken.put("action", "firebase-device-token-add");
        sendToken.put("session", Account.session);
        sendToken.put("token", Account.token);
        sendToken.put("key", "KEY REMOVED");
        Call<String> tokenCall = apiService.storeSelect(sendToken);
        tokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("token received", _response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error sending token", t.getMessage());
            }
        });
    }
    void SaveCredentials()
    {
        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("email",email.getText().toString() );
        Ed.putString("pw",password.getText().toString());
        Ed.commit();
    }

    private void fetchAllAdvertisementBanners() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-all");
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.advertisementAPI(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String plain_text_response = response.body();
                Log.d("fetchAllAdvertisementBanners", response.body());
                if(plain_text_response != null && !plain_text_response.equals("false") &&!plain_text_response.equals("[]") && !plain_text_response.equals("nosession") && !plain_text_response.isEmpty())
                {
                    try {
                        JSONArray jArray = new JSONArray(plain_text_response);

                        for (int i=0; i < jArray.length(); i++) {
                            JSONObject oneObject = jArray.getJSONObject(i);
                            String bannerID = oneObject.optString("id");
                            String bannerComment = oneObject.optString("text");
                            String bannerImageURL = oneObject.optString("image");

                            advertisementBanners.add(new SlideModel(bannerImageURL, null));
                            advertisementBannerIds.add(bannerID);
                        }
                        bannerImageSlider.setImageList(advertisementBanners);

                        bannerImageSlider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                String bannerID = advertisementBannerIds.get(i);
                                getAdvertisedStore(bannerID);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void getAdvertisedStore(String bannerID) {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-store");
        fields.put("id", bannerID);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.advertisementAPI(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("fetchAdvertisement", response.body());
                if(plain_text_response != null && !plain_text_response.equals("false") &&!plain_text_response.equals("[]") && !plain_text_response.equals("nosession") && !plain_text_response.isEmpty())
                {
                    try {
                        JSONObject jObject = new JSONObject(plain_text_response);
                        String storeID = jObject.optString("store_id");
                        String store = jObject.optString("store");
                        String referral = jObject.optString("referral");
                        Boolean status = jObject.optBoolean("status");

                        if (status == true) {

                            Intent intent = new Intent(MainActivity.this, CreateAccountContainer.class);
                            intent.putExtra("storeID", Integer.parseInt(storeID));
                            intent.putExtra("storeName", store);
                            intent.putExtra("referralPhone", referral);
                            startActivity(intent);

                        }
                        else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setMessage("Couldn't get the store!");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Okay",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            return;
                        }

                    } catch (JSONException  e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("Something went wrong! Please try again later");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}