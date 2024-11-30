package com.infoempire.wavetoget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link customerpage1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class customerpage1 extends Fragment {
    View view;
    TextView refamt;
    TextView errmsg;
//    View starsholder;
    Button backbut;
    Button username;
    Button qrbtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public customerpage1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment customerpage1.
     */
    // TODO: Rename and change types and number of parameters
    public static customerpage1 newInstance(String param1, String param2) {
        customerpage1 fragment = new customerpage1();
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
        view = inflater.inflate(R.layout.fragment_customerpage1, container, false);
        // Inflate the layout for this fragment
        refamt = view.findViewById(R.id.refamt);
        backbut = view.findViewById(R.id.logout);
        username = view.findViewById(R.id.username);
//        starsholder = view.findViewById(R.id.starsholder);
        errmsg = view.findViewById(R.id.errmsg);
        qrbtn = view.findViewById(R.id.qrbtn);
        MainMenu menufrag = new MainMenu();
        CustomerHomePage homefrag = new CustomerHomePage();
        getChildFragmentManager().beginTransaction().replace(R.id.page, homefrag).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.customermenu, menufrag).commit();
        backbut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        qrbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                QRlink frag = new QRlink();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        username.setText(CustomerAccount.firstname);

        return view;
    }
}