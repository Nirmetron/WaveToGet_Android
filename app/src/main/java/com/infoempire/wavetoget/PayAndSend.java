package com.infoempire.wavetoget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayAndSend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayAndSend extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    String type;
    String receiverId;
    TextView coinsVal;
    TextView errormsg;
    EditText coinsSend;
    EditText phoneReceiver;
    Button send;
    Button cancel;
    Button scanpayqr;
    ImageView buycoins;

    public PayAndSend() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayAndSend.
     */
    // TODO: Rename and change types and number of parameters
    public static PayAndSend newInstance(String param1, String param2) {
        PayAndSend fragment = new PayAndSend();
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
        view = inflater.inflate(R.layout.fragment_pay_and_send, container, false);
        send = view.findViewById(R.id.sendcoinsbtn);
        cancel = view.findViewById(R.id.cancelpaybtn);
        scanpayqr = view.findViewById(R.id.scanpayqr);
        coinsVal = view.findViewById(R.id.coinsUserContainer);
        coinsSend = view.findViewById(R.id.coinsToSend);
        phoneReceiver = view.findViewById(R.id.phoneToSend);
        buycoins = view.findViewById(R.id.buycoinsImg);
        errormsg = view.findViewById(R.id.errormsg);
        coinsVal.setText(CustomerAccount.coins.toString());
        buycoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyCoins frag = new BuyCoins();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerpage1 frag = new customerpage1();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(phoneReceiver.getText().length() == 10)) {
                    errormsg.setText("Please check the receiver number(10 digits long)");
                    return;
                }
                if (CustomerAccount.coins < Integer.valueOf(coinsSend.getText().toString())){
                    errormsg.setText("Not enough coins");
                    return;
                }
                if (0 == Integer.valueOf(coinsSend.getText().toString())){
                    errormsg.setText("Can't send 0");
                    return;
                }
                sendMoney();
            }
        });
        scanpayqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        getActivity()
                );
                Account.scanningItem = true;
                intentIntegrator.setPrompt("Scan QR code");
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        if(intentResult.getContents() != null)
        {
            Log.d("scan", intentResult.getContents());
            Account.qrstring = intentResult.getContents();
            if(Account.scanningItem) {
                GetStoreitem(intentResult.getContents());
            }
        }
    }
    private void sendMoney(){
        AlertDialog.Builder confirm = new AlertDialog.Builder(getActivity());
        confirm.setMessage("Are you sure you want to send " + coinsSend.getText().toString() + " coins to this account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Map<String, String> getReceiverId = new HashMap<>();
                        getReceiverId.put("action", "find-cardholder");
                        getReceiverId.put("session",  Account.session);
                        getReceiverId.put("phone",  phoneReceiver.getText().toString());
                        getReceiverId.put("key", "KEY REMOVED");
                        Call<String> getCall = MainActivity.apiService.createPost(getReceiverId);
                        getCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String _response = response.body();
                                Log.d("find receiver", type + " type " +  _response);
                                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty()){
                                        receiverId = _response;
                                        errormsg.setText("");
                                        TransferCommit();
                                } else {
                                    Log.d("didn't get 2", _response);
                                    return;
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("didn't get 3", t.getMessage());
                                errormsg.setText("Unable to find this account");
                                return;
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog confAlert = confirm.create();
        confAlert.show();

    }
    private void GetStoreitem(String itemid) {

        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-storeitem");
        fields.put("session",  Account.session);
        fields.put("id",  itemid);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("STORE ITEM", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty())
                {
                    try {
                        JSONObject jObject = new JSONObject(_response);
                        Account.qrstring = "";
                        Account.qrstring += jObject.optString("code") + '`';
                        Account.qrstring += jObject.optString("amount") + '`';
                        Account.qrstring += jObject.optString("type") + '`';
                        Account.qrstring += String.valueOf(jObject.optInt("id")) + '`';
                        Account.qrstring += String.valueOf(jObject.optInt("quantity"));
                        payqr frag = new payqr();
                        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    errormsg.setText("No item found...");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("get item problem", t.getMessage());
                errormsg.setText("Error occurred processing request");
            }
        });
    }
    private void TransferCommit() {
        String val = coinsSend.getText().toString();
        Map<String, String> transCoins = new HashMap<>();
        transCoins.put("action", "transfer-coins");
        transCoins.put("key", "KEY REMOVED");
        transCoins.put("session",  Account.session);
        transCoins.put("userA",  String.valueOf(CustomerAccount.user));
        transCoins.put("viable", "false");
        transCoins.put("coins",  val);
        transCoins.put("userB",  receiverId);
        Call<String> transCall = MainActivity.apiService.createPost(transCoins);
        transCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> transCall, Response<String> response) {
                String _response = response.body();
                Log.d("transfer coins", _response);
                try {
                    JSONObject jObject = new JSONObject(_response);
                    if (!jObject.getBoolean("error")) {
                        CustomerAccount.coins = jObject.optInt("new_coins_a");
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage("Transferred " + coinsSend.getText().toString() + " coins to account!");
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


                        Cancel();
                    }
                } catch (JSONException e) {
                    //should happen really
                    Log.d("cant transfer", String.valueOf(e));
                    errormsg.setText("Problems with transfer request");
                }
            }

            @Override
            public void onFailure(Call<String> transCall, Throwable t) {

            }
        });
    }
    private void Cancel(){
        PayAndSend frag = new PayAndSend();
        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
    }
}