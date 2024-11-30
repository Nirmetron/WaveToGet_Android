package com.infoempire.wavetoget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditAccountInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAccountInfo extends Fragment {
    View view;
    EditText businesstext;
    EditText emailtext;
    EditText passwordtext;
    EditText cardnametext;
    EditText phonetext;
    EditText addresstext;
    EditText citytext;
    EditText postaltext;
    ImageView back;
    Button save;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditAccountInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditAccountInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static EditAccountInfo newInstance(String param1, String param2) {
        EditAccountInfo fragment = new EditAccountInfo();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_account_info, container, false);
        businesstext = view.findViewById(R.id.businessname);
        emailtext = view.findViewById(R.id.emailText);
        passwordtext = view.findViewById(R.id.passwordtext);
        cardnametext = view.findViewById(R.id.cardnameText);
        phonetext = view.findViewById(R.id.phonetext);
        addresstext = view.findViewById(R.id.addresstext);
        citytext = view.findViewById(R.id.citytext);
        postaltext = view.findViewById(R.id.postaltext);
        save = view.findViewById(R.id.savecred);

        businesstext.setHint(StoreAccount.name);
        emailtext.setHint(Account.email);
        //passwordtext.setText(Account.password);
        cardnametext.setHint(StoreAccount.cardname);
        phonetext.setHint(StoreAccount.phone);
        addresstext.setHint(StoreAccount.address);
        citytext.setHint(StoreAccount.city);
        postaltext.setHint(StoreAccount.postalcode);

        back = view.findViewById(R.id.backbutton4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSettingsPage1 frag = new StoreSettingsPage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
        return view;
    }
    private void Save() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "update-store");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("user",  String.valueOf(Account.id));
        if(!businesstext.getText().toString().isEmpty()) {
            fields.put("name", businesstext.getText().toString());
            StoreAccount.name = businesstext.getText().toString();
        }
        if(!cardnametext.getText().toString().isEmpty()) {
            fields.put("cardname", cardnametext.getText().toString());
            StoreAccount.cardname = cardnametext.getText().toString();
        }
        if(!passwordtext.getText().toString().isEmpty()) {
            fields.put("password", passwordtext.getText().toString());
            Account.password = passwordtext.getText().toString();
        }
        if(!emailtext.getText().toString().isEmpty()) {
            fields.put("email", emailtext.getText().toString());
            Account.email = emailtext.getText().toString();
        }
        if(!phonetext.getText().toString().isEmpty()) {
            fields.put("phone", phonetext.getText().toString());
            StoreAccount.phone = phonetext.getText().toString();
        }
        if(!addresstext.getText().toString().isEmpty()) {
            fields.put("address", addresstext.getText().toString());
            StoreAccount.address = addresstext.getText().toString();
        }
        if(!citytext.getText().toString().isEmpty()) {
            fields.put("city", citytext.getText().toString());
            StoreAccount.city = citytext.getText().toString();
        }
        if(!postaltext.getText().toString().isEmpty()) {
            fields.put("postalcode", postaltext.getText().toString());
            StoreAccount.postalcode = postaltext.getText().toString();
        }
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("editacc response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    StoreSettingsPage1 frag = new StoreSettingsPage1();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}