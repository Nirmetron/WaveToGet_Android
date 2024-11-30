package com.infoempire.wavetoget;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreMainMenu extends Fragment {
    View view;
//    Button back;
    Button activity;
//    Button redeem;
    Button notes;
    Button askme;
    Button home;
    ImageView clickedImg;
    ImageView lastclickedImg;
    TextView unread;
    public static Button lastClicked;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreMainMenu() {
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
    public static StoreMainMenu newInstance(String param1, String param2) {
        StoreMainMenu fragment = new StoreMainMenu();
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
        view = inflater.inflate(R.layout.fragment_store_main_menu, container, false);
//        back = view.findViewById(R.id.storebackbtn);
        activity = view.findViewById(R.id.storeactivitybtn);
//        redeem = view.findViewById(R.id.storeredeembtn);
        notes  = view.findViewById(R.id.storenotesbtn);
        askme = view.findViewById(R.id.askbtn2);
        home = view.findViewById(R.id.homebtn2);
        unread = view.findViewById(R.id.unreadcountstore);
        SetColor(home);
//        back.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                MainMenu mainMenu = new MainMenu();
//                Balance balanceFrag = new Balance();
//                getParentFragmentManager().beginTransaction().replace(R.id.page, balanceFrag).commit();
//                getParentFragmentManager().beginTransaction().replace(R.id.storemenu, mainMenu).commit();
//                getActivity().findViewById(R.id.infopage).setVisibility(View.VISIBLE);
//            }
//        });
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                    Balance homefrag = new Balance();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, homefrag).commit();
                    SetColor(home);
            }
        });
        activity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                CustomerActivity frag = new CustomerActivity();
//                StoreActivity frag = new StoreActivity();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                SetColor(activity);
            }
        });
//        redeem.setOnClickListener(new View.OnClickListener()
//        {41632
//            @Override
//            public void onClick(View v) {
//                redeem balanceFrag = new redeem();
//                getParentFragmentManager().beginTransaction().replace(R.id.page, balanceFrag).commit();
//                SetColor(redeem);
//            }
//        });
        notes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                notes frag = new notes(true);
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                SetColor(notes);
            }
        });
        askme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                AskMe askFrag = new AskMe();
                getParentFragmentManager().beginTransaction().replace(R.id.page, askFrag).commit();
                SetColor(askme);
            }
        });
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "unread-notification-count");
//        fields.put("cardholder",  String.valueOf(CustomerAccount.user));
        fields.put("session", Account.session);
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("store unread count", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty() && !_response.equals("0") && !_response.contains("b"))
                {
                    unread.setText(_response);
                    unread.setVisibility(View.VISIBLE);
                }
                else {
                    Log.d("failed unread or 0", _response);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("failed unread store", t.getMessage());
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void SetColor(Button but)
    {
        getActivity().findViewById(R.id.infopage).setVisibility(View.VISIBLE);
        if(but != lastClicked)
        {
            but.setTextColor(Color.parseColor("#4275B3"));
            if (but == activity){
                clickedImg = view.findViewById(R.id.activityimg);
                clickedImg.setImageResource(R.drawable.activity_blue);
            }
            if (but == home){
                clickedImg = view.findViewById(R.id.homeimg2);
                clickedImg.setImageResource(R.drawable.home_blue);
            }
            if (but == askme){
                clickedImg = view.findViewById(R.id.askimg2);
                clickedImg.setImageResource(R.drawable.chat_blue);
            }
//            if (but == redeem){
//                clickedImg = view.findViewById(R.id.redeemimg);
//                clickedImg.setImageResource(R.drawable.redeem_blue);
//            }
            if (but == notes){
                clickedImg = view.findViewById(R.id.buyimg);
                clickedImg.setImageResource(R.drawable.notes_blue);
            }
            if(lastClicked != null){
                lastClicked.setTextColor(Color.parseColor("#8E8E93"));
                if (lastClicked == activity){
                    lastclickedImg = view.findViewById(R.id.activityimg);
                    lastclickedImg.setImageResource(R.drawable.activity_grey);
                }
                if (lastClicked == home){
                    lastclickedImg = view.findViewById(R.id.homeimg2);
                    lastclickedImg.setImageResource(R.drawable.home_grey);
                }
//                if (lastClicked == redeem){
//                    lastclickedImg = view.findViewById(R.id.redeemimg);
//                    lastclickedImg.setImageResource(R.drawable.redeem_grey);
//                }
                if (lastClicked == notes){
                    lastclickedImg = view.findViewById(R.id.buyimg);
                    lastclickedImg.setImageResource(R.drawable.notes_grey);
                }
                if (lastClicked == askme){
                    lastclickedImg = view.findViewById(R.id.askimg2);
                    lastclickedImg.setImageResource(R.drawable.chat_grey);
                }
            }
            lastClicked = but;
        }
    }
}