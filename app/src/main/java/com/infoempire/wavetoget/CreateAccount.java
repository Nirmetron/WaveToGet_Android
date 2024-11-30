package com.infoempire.wavetoget;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccount extends Fragment {


    EditText businesstext;
    EditText emailtext;
    EditText passwordtext;
    EditText cardnametext;
    EditText lastnametext;
    EditText phonetext;
    EditText addresstext;
    EditText citytext;
    EditText postaltext;
    TextView referralnametext;
    TextView referralphonetext;
    TextView referralerror;
    TextView refcontainer;
    EditText referralphoneEditText;
    ImageView back;
    Button save;
    View view;
    String refid;

    boolean loading;

    private Spinner spinner;

    private Spinner provinces;
    Integer selectedProvince;

    private Map<String, Integer> storeList = new HashMap<>();
    ArrayList<String> stores  = new ArrayList<String>();
    Integer selectedStore;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreSettingsPage1.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateAccount newInstance(String param1, String param2) {
        CreateAccount fragment = new CreateAccount();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_create_account, container, false);
        // Inflate the layout for this fragment
        businesstext = view.findViewById(R.id.businessname);
        emailtext = view.findViewById(R.id.emailText);
        passwordtext = view.findViewById(R.id.passwordtext);
        cardnametext = view.findViewById(R.id.cardnameText);
        lastnametext = view.findViewById(R.id.lastnametext);
        phonetext = view.findViewById(R.id.phonetext);
        addresstext = view.findViewById(R.id.addresstext);
        citytext = view.findViewById(R.id.citytext);
        postaltext = view.findViewById(R.id.postaltext);
        save = view.findViewById(R.id.savecred);
        back = view.findViewById(R.id.backbutton4);
        referralnametext = view.findViewById(R.id.referralnametext);
        referralphonetext = view.findViewById(R.id.referralphonetext);
        referralphoneEditText = view.findViewById(R.id.referralphoneimput);
        referralerror = view.findViewById(R.id.referralerror);
        refcontainer = view.findViewById(R.id.textView34);
        refid = "";

        //emailtext.setText(Account.verifiedEmail);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountCreationPrompt frag = new AccountCreationPrompt();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

        spinner = view.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                Log.d("spinner", stores.get(position));
                selectedStore = storeList.get(stores.get(position));
                GetReferral();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        provinces = view.findViewById(R.id.provinces);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,Account.ProvinceList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinces.setAdapter(adapter);

        provinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                Log.d("spinner", Account.ProvinceList[position]);
                selectedProvince = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        referralphoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // When focus is lost check that the text field has valid values.

                if (!hasFocus) {
                    FindCardholder();
                }
            }
        });
        referralphonetext.setVisibility(View.INVISIBLE);
        referralphoneEditText.setVisibility(View.INVISIBLE);
        refcontainer.setVisibility(View.INVISIBLE);
        GetStores();
        return view;
    }
    private void GetReferral() {
        referralnametext.setText("");
        referralphonetext.setVisibility(View.INVISIBLE);
        referralphoneEditText.setVisibility(View.INVISIBLE);
        refcontainer.setVisibility(View.INVISIBLE);
        if(selectedStore == 0)
            return;
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-referral");
        fields.put("store", selectedStore.toString());
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("get ref", plain_text_response);
                try {
                    if(!plain_text_response.equals("[]"))
                    {
                        JSONArray jArray = new JSONArray(plain_text_response);
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject oneObject = jArray.getJSONObject(i);
                                String name = oneObject.optString("name");
                                referralnametext.setText(name);
                                referralphonetext.setVisibility(View.VISIBLE);
                                referralphoneEditText.setVisibility(View.VISIBLE);
                                refcontainer.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                // Oops
                            }
                        }
                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void FindCardholder() {
        Log.d("find cardholder", "create account");
        refid = "";
        referralerror.setText("");
        loading = true;
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "find-cardholder");
        fields.put("store", selectedStore.toString());
        fields.put("phone", referralphoneEditText.getText().toString());
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("find cardholder", response.body());
                if(!plain_text_response.equals("false") &&!plain_text_response.equals("[]") && !plain_text_response.equals("nosession") && !plain_text_response.isEmpty())
                {
                    refid = phonetext.getText().toString();
                    referralerror.setText("Referral account found!");
                }
                else
                {
                    refid = "";
                    referralerror.setText("Referral account not found...");
                }
                loading = false;
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void GetStores() {
        if(loading)
            return;
        //if(email.getText().toString() != "") {
        stores.clear();
        storeList.clear();
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-all-stores");
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("stores", plain_text_response);
                try {
                    JSONArray jArray = new JSONArray(plain_text_response);
                    storeList.put("Please select a store you would like to register for.",0);
                    stores.add("Please select a store you would like to register for.");
                    for (int i=0; i < jArray.length(); i++)
                    {
                        try {
                            JSONObject oneObject = jArray.getJSONObject(i);
                            // Pulling items from the array
                            Integer store = oneObject.optInt("id");
                            String store_displayname = oneObject.optString("name");
                            storeList.put(store_displayname,store);
                            stores.add(store_displayname);

                        } catch (JSONException e) {
                            // Oops
                        }
                    }
//                    if(storeList.size() == 0)
//                    {
//                        errormessage.setText("No accounts associated with this email found...");
//                    }
//                    else
//                    {
//                        errormessage.setText("");
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
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
    @SuppressLint("NotConstructor")
    private void CreateAccount() {
        if(selectedStore == 0)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Please select a store...");
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
        if(emailtext.getText().toString().isEmpty() ||cardnametext.getText().toString().isEmpty() || lastnametext.getText().toString().isEmpty() || passwordtext.getText().toString().isEmpty() ||
                phonetext.getText().toString().isEmpty() || addresstext.getText().toString().isEmpty() ||
                citytext.getText().toString().isEmpty() || postaltext.getText().toString().isEmpty()) {
            //add a error message here
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Please fill in all fields...");
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
        if(selectedProvince == 0)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Please select a province/state...");
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
        if(!isValidEmail(emailtext.getText().toString()))
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Please enter a valid email address...");
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
        if(!TextUtils.isDigitsOnly(phonetext.getText().toString()) || phonetext.getText().toString().length() != 10)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Please enter a valid phone number...");
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

        Map<String, String> phoneCheck = new HashMap<>();
        phoneCheck.put("action", "phone-exists");
        phoneCheck.put("key", "KEY REMOVED");
        phoneCheck.put("phone", phonetext.getText().toString());
        Call<String> phoneCall = MainActivity.apiService.createPost(phoneCheck);
        phoneCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("phone-exist", _response);
                if (Objects.equals(_response, "false") || _response.contains("0") || Integer.parseInt(_response) == 0) {
                    Log.d("got here", "0 it is");
                    Map<String, String> fields = new HashMap<>();
                    fields.put("action", "add-user");
                    fields.put("displayname", cardnametext.getText().toString());
                    fields.put("firstname", cardnametext.getText().toString());
                    fields.put("lastname", lastnametext.getText().toString());
                    fields.put("password", passwordtext.getText().toString());
                    fields.put("email", emailtext.getText().toString());
                    fields.put("phone", phonetext.getText().toString());
                    fields.put("address", addresstext.getText().toString());
                    fields.put("city", citytext.getText().toString());
                    fields.put("province", selectedProvince.toString());
                    fields.put("pin", "0000");
                    fields.put("postal", postaltext.getText().toString());
                    fields.put("store", selectedStore.toString());
                    fields.put("ref", refid);
                    fields.put("user_type", "1");
                    fields.put("key", "KEY REMOVED");
                    Call<String> addCall = MainActivity.apiService.createPost(fields);
                    addCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String _response = response.body();
                            Log.d("add acc response", _response);
                            if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                            {
                                //add success message here
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("Account created!");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Okay",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                getActivity().finish();
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                            else
                            {
                                //accountexists at store
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("Email or phone number is already associated with this store...");
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
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("call failed", t.getMessage());
                        }
                    });
                }
                else {
                    Log.d("number exists", phonetext.getText().toString());
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Phone number already exists");
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
                Log.d("phone check failed", t.getMessage());
            }
        });
    }
    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}