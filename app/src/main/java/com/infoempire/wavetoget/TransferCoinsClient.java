package com.infoempire.wavetoget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransferCoinsClient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferCoinsClient extends Fragment {
    View view;
    Button cancel;
    Button use;
    EditText amount;
    EditText  phone;
    TextView errormsg;
    TextView customerCoins;
    Integer coinsValue;
    String receiverPhone;
    String phoneString;
    Boolean isPayToStore;
    String receiverId;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransferCoinsClient(String receiverPhone, Boolean isPayToStore) {
        this.receiverPhone = receiverPhone;
        this.isPayToStore = isPayToStore;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPoints.
     */
    // TODO: Rename and change types and number of parameters
    public static TransferCoinsClient newInstance(String param1, String param2) {
        TransferCoinsClient fragment = new TransferCoinsClient(null, null);
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
        view = inflater.inflate(R.layout.fragment_transfer_coins_client, container, false);
        amount = view.findViewById(R.id.editCoinsTransfer);
        phone = view.findViewById(R.id.editPhoneNumber);
        use = view.findViewById(R.id.transcoins);
        errormsg = view.findViewById(R.id.errormsg);
        customerCoins = view.findViewById(R.id.showClientCoins);
        customerCoins.setText(String.valueOf(CustomerAccount.coins));
        use.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
               TransferCoinsFunc();
            }
        });
        cancel = view.findViewById(R.id.canceltransfer);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Cancel();
            }
        });
        return view;
    }
    void Cancel()
    {
//        BalanceButtons frag = new BalanceButtons();
        customerpage1 frag = new customerpage1();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
    }
    private void TransferCoinsFunc() {
        String val = amount.getText().toString();
        if(val == null || val.isEmpty())
        {
            errormsg.setText("Please enter correct amount");
            return;
        }
        coinsValue = Integer.valueOf(amount.getText().toString());
        if (CustomerAccount.coins < coinsValue) {
            errormsg.setText("Insufficient amount of coins");
            return;
        }
        if (coinsValue==0) {
            errormsg.setText("Please enter correct amount");
            return;
        }
        phoneString = phone.getText().toString();
        if (phoneString==null || phoneString.length() != 10) {
            errormsg.setText("Enter correct phone number (10 digits)");
            return;
        }
        if (receiverPhone == null) {
            receiverPhone = phone.getText().toString();
        }
        if (isPayToStore){
            Map<String, String> getStoreId = new HashMap<>();
            getStoreId.put("action", "find-storeowner");
            getStoreId.put("key", "KEY REMOVED");
            getStoreId.put("session", Account.session);
            getStoreId.put("phone", receiverPhone);
            Call<String> getCall = MainActivity.apiService.createPost(getStoreId);
            getCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String _response = response.body();
                    Log.d("find storeowner", _response);
                    if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty()){
                        try {
                            JSONObject jObject = new JSONObject(_response);
                            receiverId = String.valueOf(jObject.optInt("user_id"));
                        } catch (JSONException e) {
                            Log.d("didn't get 1", e.getMessage());
                            return;
                        }
                    } else {
                        Log.d("didn't get 2", _response);
                        return;
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("didn't get 3", t.getMessage());
                    errormsg.setText("Could't find this store");
                    return;
                }
            });
        }
        else {
            Map<String, String> fields = new HashMap<>();
            fields.put("action", "get-cardholder");
            fields.put("store",  String.valueOf(Account.store));
            Log.d("phone", receiverPhone);
            fields.put("val",  receiverPhone);
            fields.put("session",  Account.session);
            fields.put("key", "KEY REMOVED");
            Call<String> call = MainActivity.apiService.createPost(fields);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String _response = response.body();
                    Log.d("cust receiver account", _response);
                    if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty()) {
                        try {
                            JSONObject jObject = new JSONObject(_response);
//                            receiverId = jObject.optInt("id");
                            receiverId = String.valueOf(jObject.optInt("user"));
                        } catch (JSONException e) {
                            Log.d("cust id didn't 1", e.getMessage());
                            return;
                        }
                    }
                    else
                    {
                        Log.d("cust id didn't 2", _response);
                        return;
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("cust id didn't 3", t.getMessage());
                    errormsg.setText("Could't find this user");
                    return;
                }
            });
        }
        errormsg.setText("");
                Map<String, String> transCoins = new HashMap<>();
                transCoins.put("action", "transfer-coins");
                transCoins.put("key", "KEY REMOVED");
                transCoins.put("session",  Account.session);
                transCoins.put("userA",  String.valueOf(CustomerAccount.user));
                transCoins.put("viable", String.valueOf(isPayToStore));
                transCoins.put("coins",  val);
                transCoins.put("userB",  receiverId);
        Call<String> transCall = MainActivity.apiService.createPost(transCoins);
                transCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> transCall, Response<String> response) {
                        String _response = response.body();
                        Log.d("store trans coins", _response);
                        try {
                            JSONObject jObject = new JSONObject(_response);
                            if (!jObject.getBoolean("error")) {
                                CustomerAccount.coins = jObject.optInt("new_coins_a");
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("Transferred " + amount.getText().toString() + " coins to account!");
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
                        }
                    }

                    @Override
                    public void onFailure(Call<String> transCall, Throwable t) {

                    }
                });
//        Map<String, String> addPoints = new HashMap<>();
//        addPoints.put("action", "add-coins");
//        addPoints.put("key", "KEY REMOVED");
//        addPoints.put("session",  Account.session);
//        addPoints.put("user",  String.valueOf(CustomerAccount.user));
//        addPoints.put("coins",  "+"+val);
//        Call<String> addCall = MainActivity.apiService.createPost(addPoints);
//        addCall.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> addCall, Response<String> response) {
//                String _response = response.body();
//                Log.d("add points response", _response);
////                if(_response.equals("success"))
////                {
//                    Log.d("customer coin before", String.valueOf(CustomerAccount.coins));
//
//                    CustomerAccount.coins += Long.parseLong(amount.getText().toString());
//                    Log.d("customer coin after", String.valueOf(CustomerAccount.coins));
//
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
//                    builder1.setMessage("Transferred " + Long.parseLong(amount.getText().toString()) + " points to account!");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//
//
//                    Balance frag = new Balance();
//                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
////                }
//            }
//
//            @Override
//            public void onFailure(Call<String> addCall, Throwable t) {
//
//            }
//        });
            }
    }
