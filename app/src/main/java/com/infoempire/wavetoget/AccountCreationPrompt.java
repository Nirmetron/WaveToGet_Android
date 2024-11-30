package com.infoempire.wavetoget;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class AccountCreationPrompt extends Fragment {

    View view;
    Button storeClientButton;
    Button storeOwnerButton;
    ImageView back;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountCreationPrompt() {
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
    public static AccountCreationPrompt newInstance(String param1, String param2) {
        AccountCreationPrompt fragment = new AccountCreationPrompt();
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
        view = inflater.inflate(R.layout.activity_account_creation_prompt, container, false);
        // Inflate the layout for this fragment
        storeClientButton = view.findViewById(R.id.search);
        storeOwnerButton = view.findViewById(R.id.scan);
        back = view.findViewById(R.id.backbutton5);

        storeClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadCreateCustomer();
            }
        });
        storeOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadCreateOwner();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }
    public void LoadCreateCustomer()
    {
        CreateAccount frag = new CreateAccount();
        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
    }
    public void LoadCreateOwner()
    {
        EmailVerification frag = new EmailVerification();
        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
    }
}