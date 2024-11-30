package com.infoempire.wavetoget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link payqr#newInstance} factory method to
 * create an instance of this fragment.
 */
public class payqr extends Fragment {

    TextView textview;
    TextView currentamount;
    TextView errormsg;
    View view;
    Button payqrbutton;
    Button cancelbutton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public payqr() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment payqr.
     */
    // TODO: Rename and change types and number of parameters
    public static payqr newInstance(String param1, String param2) {
        payqr fragment = new payqr();
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
    String itemname = "";
    String amount = "";
    String id = "";
    String quantity = "";
    String type = "";
    Boolean insufficientfunds = false;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payqr, container, false);


        textview = view.findViewById(R.id.itemstring);
        currentamount = view.findViewById(R.id.currentamount);
        payqrbutton = view.findViewById(R.id.paybutt);
        cancelbutton = view.findViewById(R.id.cancelbut);
        errormsg = view.findViewById(R.id.errormsg);
        String[] separated = Account.qrstring.split("`");
        if(separated.length == 5) {
            itemname = separated[0];
            amount = separated[1];
            type = separated[2];
            id = separated[3];
            quantity = separated[4];
            textview.setText(itemname + " costs " + amount + " " + type);
        }
        insufficientfunds = false;
        int temp = Integer.parseInt(amount);
            currentamount.setText(String.valueOf(CustomerAccount.coins) + "coins");
            if (temp > CustomerAccount.coins)
            {
                payqrbutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
                insufficientfunds = true;
            }
        payqrbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Paycoins();
            }
        });
        cancelbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                customerpage1 frag = new customerpage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });

        return view;
    }
    private void Paycoins() {

        Map<String, String> fields = new HashMap<>();
        fields.put("action", "buy-item");
        fields.put("session",  Account.session);
        fields.put("coins",  "0");
//        fields.put("id",  id);
//        fields.put("quantity",  String.valueOf(Integer.parseInt(quantity) - 1));
        fields.put("name",  itemname);
//        fields.put("pin",  CustomerAccount.pin);
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("buy-item", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false")) {
                    Map<String, String> addCoins = new HashMap<>();
                    addCoins.put("action", "add-coins");
                    addCoins.put("key", "KEY REMOVED");
                    addCoins.put("session",  Account.session);
                    addCoins.put("user",  String.valueOf(CustomerAccount.user));
                    addCoins.put("coins",  "-"+amount);
                    Call<String> addCall = MainActivity.apiService.createPost(addCoins);
                    addCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> addCall, Response<String> response) {
                            String _response = response.body();
                            Log.d("buy-add response", _response);
                            if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                            {
                                CustomerAccount.coins += Integer.valueOf(_response);
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("You successfully bought " + itemname);
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Okay",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                customerpage1 frag = new customerpage1();
                                                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> addCall, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                errormsg.setText("Unable to fetch item");
                Log.d("buy-item error", t.getMessage());
            }
        });
    }
}