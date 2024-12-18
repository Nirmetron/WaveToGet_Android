package com.infoempire.wavetoget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Info extends Fragment {
    View view;
    TextView name;
    TextView address;
    TextView city;
    TextView phone;
    TextView editAccount;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment testfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Info newInstance(String param1, String param2) {
        Info fragment = new Info();
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
        view = inflater.inflate(R.layout.activity_info, container, false);
        name = view.findViewById(R.id.firstname);
        name.setText(CustomerAccount.firstname + " " + CustomerAccount.lastname);
        address = view.findViewById(R.id.address);
        address.setText(CustomerAccount.address);
        phone = view.findViewById(R.id.phone);
        phone.setText(String.valueOf(CustomerAccount.phone).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3"));
        city = view.findViewById(R.id.cityprovpost);
        city.setText(CustomerAccount.city + " " + CustomerAccount.provCode + " " + CustomerAccount.postal);

        editAccount = view.findViewById(R.id.editinfotext);
        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadEditInfo();
            }
        });


        return view;
    }
    void LoadEditInfo()
    {
        Intent intent = new Intent(getActivity(), EditCustomerActivity.class);
        startActivity(intent);
    }
}