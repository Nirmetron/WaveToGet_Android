package com.infoempire.wavetoget;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerMorePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerMoreMenu extends Fragment {
    View view;
    Button back;
    Button redeem;
    Button reviews;
    Button mapbtn;
    Button buybtn;
    ImageView clickedImg;
    ImageView lastclickedImg;
    public static Button lastClicked;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CustomerMoreMenu() {
        // Required empty public constructor
    }

    public static CustomerMoreMenu newInstance(String param1, String param2) {
        CustomerMoreMenu fragment = new CustomerMoreMenu();
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
        view = inflater.inflate(R.layout.fragment_customer_more_menu, container, false);
        back = view.findViewById(R.id.clientbackbtn);
        reviews = view.findViewById(R.id.clientreviewsbtn);
        mapbtn  = view.findViewById(R.id.clientmapbtn);
        buybtn  = view.findViewById(R.id.clientbuybtn);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                MainMenu mainMenu = new MainMenu();
                customerpage1 frag = new customerpage1();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.customermenu, mainMenu).commit();
//                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                getParentFragmentManager().beginTransaction().replace(R.id.customermenu, mainMenu).commit();
            }
        });
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("reviews press", "reviews pressed what do you want");
                openGoogleReviewInBrowser();
            }
        });
        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("map press", "google.navigation:q=" + StoreAccount.address + "+" + StoreAccount.city);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+Account.ProvinceList[StoreAccount.province] + "+" + StoreAccount.address + "+" + StoreAccount.city + StoreAccount.postalcode));
                startActivity(intent);
            }
        });
        buybtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.d("notes press", "notes pressed what do you want");
                BuyCoins frag = new BuyCoins();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                SetColor(buybtn);
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void SetColor(Button but)
    {
        if(but != lastClicked)
        {
            but.setTextColor(Color.parseColor("#4275B3"));
            if (but == reviews){
                clickedImg = view.findViewById(R.id.redeemimg);
                clickedImg.setImageResource(R.drawable.redeem_blue);
            }
            if (but == buybtn){
                clickedImg = view.findViewById(R.id.buyimg);
                clickedImg.setImageResource(R.drawable.buy_coins);
            }
            if(lastClicked != null){
                lastClicked.setTextColor(Color.parseColor("#8E8E93"));
                if (lastClicked == redeem){
                    lastclickedImg = view.findViewById(R.id.redeemimg);
                    lastclickedImg.setImageResource(R.drawable.redeem_grey);
                }
                if (lastClicked == buybtn){
                    lastclickedImg = view.findViewById(R.id.buyimg);
                    lastclickedImg.setImageResource(R.drawable.buy_coins_grey);
                }
            }
            lastClicked = but;
        }
    }
    private void openGoogleReviewInBrowser() {
        String url = "";
        if (StoreAccount.googleReviewURL.isEmpty()) {
            Log.d("is empty", "openGoogleReviewInBrowser: storeAccount.googleReviewURL is empty");
            return;
        }

        url = StoreAccount.googleReviewURL;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        // Open browser
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}