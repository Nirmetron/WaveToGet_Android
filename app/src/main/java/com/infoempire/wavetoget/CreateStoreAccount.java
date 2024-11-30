package com.infoempire.wavetoget;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateStoreAccount extends Fragment {
    EditText storename;
    EditText businesstext;
    TextView emailtext;
    EditText passwordtext;
    EditText cardnametext;
    EditText lastnametext;
    EditText phonetext;
    EditText addresstext;
    EditText citytext;
    EditText postaltext;
    ImageView back;
    Button save;
    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateStoreAccount() {
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
    public static CreateStoreAccount newInstance(String param1, String param2) {
        CreateStoreAccount fragment = new CreateStoreAccount();
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
        view = inflater.inflate(R.layout.activity_create_store_account, container, false);
        storename = view.findViewById(R.id.storename);
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

        emailtext.setText(Account.verifiedEmail);

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
        return view;
    }
    public void CreateAccount()
    {
        if(storename.getText().toString().isEmpty() ||cardnametext.getText().toString().isEmpty() || lastnametext.getText().toString().isEmpty() || passwordtext.getText().toString().isEmpty() ||
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
//        if(!isValidEmail(emailtext.getText().toString()))
//        {
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateStoreAccount.this);
//            builder1.setMessage("Please enter a valid email address...");
//            builder1.setCancelable(true);
//
//            builder1.setPositiveButton(
//                    "Okay",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//            AlertDialog alert11 = builder1.create();
//            alert11.show();
//            return;
//        }
        if(!TextUtils.isDigitsOnly(phonetext.getText().toString()))
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
                if (Objects.equals(_response, "0") || Objects.equals(_response, "false")) {
                    Map<String, String> fields = new HashMap<>();
                    fields.put("action", "create-store");
                    fields.put("key", "KEY REMOVED");
                    fields.put("storename", storename.getText().toString());
                    fields.put("cardname", storename.getText().toString());
                    fields.put("firstname", cardnametext.getText().toString());
                    fields.put("lastname", lastnametext.getText().toString());
                    fields.put("password", passwordtext.getText().toString());
                    fields.put("email", emailtext.getText().toString());
                    fields.put("phone", phonetext.getText().toString());
                    fields.put("address", addresstext.getText().toString());
                    fields.put("city", citytext.getText().toString());
                    fields.put("postal", postaltext.getText().toString());
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
                                builder1.setMessage("Email or phone number already exists in teh database");
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