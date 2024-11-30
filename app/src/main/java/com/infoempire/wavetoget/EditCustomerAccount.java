package com.infoempire.wavetoget;

import android.content.Intent;
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
 * Use the {@link EditCustomerAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCustomerAccount extends Fragment {
    View view;
    EditText businesstext;
    EditText emailtext;
    EditText passwordtext;
    EditText cardnametext;
    EditText lastnametext;
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

    public EditCustomerAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditCustomerAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCustomerAccount newInstance(String param1, String param2) {
        EditCustomerAccount fragment = new EditCustomerAccount();
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
        view = inflater.inflate(R.layout.fragment_edit_customer_account, container, false);
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

        //businesstext.setHint(CustomerAccount.);
        emailtext.setHint(CustomerAccount.email);
        //passwordtext.setText(Account.password);
        cardnametext.setHint(CustomerAccount.firstname);
        lastnametext.setHint(CustomerAccount.lastname);
        phonetext.setHint(CustomerAccount.phone);
        addresstext.setHint(CustomerAccount.address);
        citytext.setHint(CustomerAccount.city);
        postaltext.setHint(CustomerAccount.postal);

        back = view.findViewById(R.id.backbutton4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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
        fields.put("action", "update-cardholder");
        fields.put("session",  Account.session);
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("user",  String.valueOf(CustomerAccount.user));
        if(!cardnametext.getText().toString().isEmpty()) {
            fields.put("firstname", cardnametext.getText().toString());
            CustomerAccount.firstname = cardnametext.getText().toString();
        }
        if(!lastnametext.getText().toString().isEmpty()) {
            fields.put("lastname", lastnametext.getText().toString());
            CustomerAccount.lastname = lastnametext.getText().toString();
        }
        if(!passwordtext.getText().toString().isEmpty()) {
            fields.put("password", passwordtext.getText().toString());
            //Account.password = passwordtext.getText().toString();
        }
        if(!emailtext.getText().toString().isEmpty()) {
            fields.put("email", emailtext.getText().toString());
            CustomerAccount.email = emailtext.getText().toString();
        }
        if(!phonetext.getText().toString().isEmpty()) {
            fields.put("phone", phonetext.getText().toString());
            CustomerAccount.phone = phonetext.getText().toString();
        }
        if(!addresstext.getText().toString().isEmpty()) {
            fields.put("address", addresstext.getText().toString());
            CustomerAccount.address = addresstext.getText().toString();
        }
        if(!citytext.getText().toString().isEmpty()) {
            fields.put("city", citytext.getText().toString());
            CustomerAccount.city = citytext.getText().toString();
        }
        if(!postaltext.getText().toString().isEmpty()) {
            fields.put("postalcode", postaltext.getText().toString());
            CustomerAccount.postal = postaltext.getText().toString();
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
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}