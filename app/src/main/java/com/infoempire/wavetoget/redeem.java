package com.infoempire.wavetoget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link redeem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class redeem extends Fragment {
    View view;
    Boolean loading;
    TextView errorText;
    Group coinsAmountText;
    TextView redeemUserCoins;
    TextView noredeem;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public redeem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment redeem.
     */
    // TODO: Rename and change types and number of parameters
    public static redeem newInstance(String param1, String param2) {
        redeem fragment = new redeem();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_redeem, container, false);
        loading = false;
        coinsAmountText = view.findViewById(R.id.usercoinsavailable);
        redeemUserCoins = view.findViewById(R.id.redeemusercoins);
        noredeem = view.findViewById(R.id.no_redeemables);
        redeemUserCoins.setText(String.valueOf(CustomerAccount.coins));
        //errorText = view.findViewById(R.id.errortext);
        //if(CustomerAccount.messageString != "")
        //errorText.setText(CustomerAccount.messageString);
        LayoutInflater inf = getLayoutInflater();
        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
        Log.d("store account id", String.valueOf(StoreAccount.id));
        if (Account.isCust){
            coinsAmountText.setVisibility(View.GONE);
        }
        else {
            coinsAmountText.setVisibility(View.VISIBLE);
            if (!notes.showPages) {
                coinsAmountText.setVisibility(View.GONE);
            }
        }
        if (StoreAccount.redeemables.size() == 0){
            noredeem.setText("Store has no redeemables");
        }
        else {
            noredeem.setText("");
        }
        for(int i = 0; i < StoreAccount.redeemables.size(); i++){
            LinearLayout layout2 = new LinearLayout(getContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            View item = inf.inflate(R.layout.item_layout, null, false);
            layout2.setId(i);
            layout2.addView(item);
            scrollViewLinearlayout.addView(layout2);
            TextView iname = item.findViewById(R.id.itemname);
            String name = StoreAccount.redeemables.get(i).name;
            int pts = StoreAccount.redeemables.get(i).coins;
            TextView iprice = item.findViewById(R.id.itemprice);
            Button iuse = item.findViewById(R.id.usebtn);
            iname.setText(StoreAccount.redeemables.get(i).name);
            iprice.setText(String.valueOf(pts) + " coins");
            if (Account.isCust){
                if(pts > CustomerAccount.coins)
                {
                    iname.setTextColor(Color.parseColor("#8E8E93"));
                    iprice.setTextColor(Color.parseColor("#8E8E93"));
                    iuse.setTextColor(Color.parseColor("#8E8E93"));
                    iuse.setBackgroundResource(R.drawable.rounded_corner_stroke_inactive_1dp);
                    iuse.setText("Need more");
                }
                else {
                    iuse.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            if(loading)
                                return;
                            redeemFunc(pts, name);
                        }
                    });
                }
            }
            else {
                iuse.setBackgroundResource(R.drawable.background);
                redeemUserCoins.setText(String.valueOf(CustomerAccount.coins));
                if(pts > CustomerAccount.coins)
                {
                    iname.setTextColor(Color.parseColor("#8E8E93"));
                    iprice.setTextColor(Color.parseColor("#8E8E93"));
                    iuse.setTextColor(Color.parseColor("#8E8E93"));
                    iuse.setText("COLLECT MORE");
                }
                else {
                    iuse.setTextColor(Color.parseColor("#000000"));
                    iuse.setText("AVAILABLE");
                }
            }
        }
        return view;
    }
    private void redeemFunc(int amount, String name) {
        loading = true;
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "transact");
        fields.put("session",  Account.session);
        fields.put("coins",  String.valueOf(amount));
        fields.put("pin",  CustomerAccount.pin);
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loading = false;
                String _response = response.body();
                if (!_response.isEmpty()){
                    Log.d("redeem response", _response);
                }
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    CustomerAccount.messageString = name + " redeemed for " + amount;
                    Log.d("TAG", CustomerAccount.messageString);
                    CustomerAccount.coins -= amount;
//                    ((Balance) getParentFragment()).UpdateValues();
                    redeem frag = new redeem();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage(CustomerAccount.messageString);
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
                loading = false;
            }
        });
    }
}