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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends Fragment {
    View view;
    Button home;
    Button notif;
    Button activity;
    Button askme;
    Button more;
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

    public MainMenu() {
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
    public static MainMenu newInstance(String param1, String param2) {
        MainMenu fragment = new MainMenu();
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
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        home = view.findViewById(R.id.homebtn);
//        notif = view.findViewById(R.id.notifbtn);
        askme = view.findViewById(R.id.askbtn);
        activity = view.findViewById(R.id.clientactivitybtn);
        more  = view.findViewById(R.id.menumorebtn);
        unread = view.findViewById(R.id.unreadcountclient);
        SetColor(home);
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(!Account.isCust){
                    Balance homefrag = new Balance();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, homefrag).commit();
                }
                else {
                    CustomerHomePage homefrag = new CustomerHomePage();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, homefrag).commit();
                }
                SetColor(home);
            }
        });
        activity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                CustomerActivity frag = new CustomerActivity();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                SetColor(activity);
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
        more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (!Account.isCust){
//                    view.findViewById(R.id.page).getLayoutParams().height = 550;
                    StoreMainMenu frag = new StoreMainMenu();
                    getParentFragmentManager().beginTransaction().replace(R.id.storemenu, frag).commit();
                    getActivity().findViewById(R.id.infopage).setVisibility(View.GONE);
                }
                else {
                    CustomerMoreMenu frag = new CustomerMoreMenu();
                    getParentFragmentManager().beginTransaction().replace(R.id.customermenu, frag).commit();
                }
                CustomerMorePage morepage = new CustomerMorePage();
                getParentFragmentManager().beginTransaction().replace(R.id.page, morepage).commit();
            }
        });
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "unread-notification-count");
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("session", Account.session);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("client unread count", _response);
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
                Log.d("failed unread client", t.getMessage());
            }
        });
        //        notif.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                    Balance balanceFrag = new Balance();
//                    getParentFragmentManager().beginTransaction().replace(R.id.page, balanceFrag).commit();
//                    SetColor(notif);
//            }
//        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void SetColor(Button but)
    {
        if(but != lastClicked)
        {
            but.setTextColor(Color.parseColor("#4275B3"));
            if (but == home){
                clickedImg = view.findViewById(R.id.homeimg);
                clickedImg.setImageResource(R.drawable.home_blue);
            }
            if (but == activity){
                clickedImg = view.findViewById(R.id.activityimg);
                clickedImg.setImageResource(R.drawable.activity_blue);
            }
            if (but == askme){
                clickedImg = view.findViewById(R.id.askimg);
                clickedImg.setImageResource(R.drawable.chat_blue);
            }
            if(lastClicked != null){
                lastClicked.setTextColor(Color.parseColor("#8E8E93"));
                if (lastClicked == home){
                    lastclickedImg = view.findViewById(R.id.homeimg);
                    lastclickedImg.setImageResource(R.drawable.home_grey);
                }
                if (lastClicked == activity){
                    lastclickedImg = view.findViewById(R.id.activityimg);
                    lastclickedImg.setImageResource(R.drawable.activity_grey);
                }
                if (lastClicked == askme){
                    lastclickedImg = view.findViewById(R.id.askimg);
                    lastclickedImg.setImageResource(R.drawable.chat_grey);
                }
            }
            lastClicked = but;
        }
    }
}