package com.infoempire.wavetoget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransferCoinsStore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferCoinsStore extends Fragment {
    View view;
//    Button cancel;
    Button use;
    EditText amount;
    TextView errormsg;
    TextView storeCoins;
    Integer  coinsValue;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransferCoinsStore() {
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
    public static TransferCoinsStore newInstance(String param1, String param2) {
        TransferCoinsStore fragment = new TransferCoinsStore();
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
        view = inflater.inflate(R.layout.fragment_transfer_coins_store, container, false);
        amount = view.findViewById(R.id.editTextNumber);
        use = view.findViewById(R.id.transcoins);
        errormsg = view.findViewById(R.id.errormsg);
        use.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
               TransferCoinsFunc();
            }
        });
//        cancel = view.findViewById(R.id.canceladddol);
//        cancel.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Cancel();
//            }
//        });
        return view;
    }
//    void Cancel()
//    {
//        pagetext.setText("BALANCE");
////        BalanceButtons frag = new BalanceButtons();
//        Balance frag = new Balance();
//        getParentFragmentManager().beginTransaction().replace(R.id.balancepage, frag).commit();
//    }
    private void TransferCoinsFunc() {
        String val = amount.getText().toString();
        if(val == null || val.isEmpty())
        {
            errormsg.setText("Please enter correct amount");
            return;
        }
        coinsValue = Integer.valueOf(amount.getText().toString());
        if (StoreAccount.coins < coinsValue) {
            errormsg.setText("Insufficient amount of coins");
            return;
        }
        if (coinsValue==0) {
            errormsg.setText("Please enter correct amount");
            return;
        }
        errormsg.setText("");
        AlertDialog.Builder confirm = new AlertDialog.Builder(getActivity());
        confirm.setMessage("Are you sure you want to send " + amount.getText().toString() + " coins to this user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Map<String, String> transCoins = new HashMap<>();
                        transCoins.put("action", "transfer-coins");
                        transCoins.put("key", "KEY REMOVED");
                        transCoins.put("session",  Account.session);
                        transCoins.put("userA",  String.valueOf(StoreAccount.id));
                        transCoins.put("viable", "false");
                        transCoins.put("coins",  val);
                        transCoins.put("userB",  String.valueOf(CustomerAccount.user));
                        Call<String> transCall = MainActivity.apiService.createPost(transCoins);
                        transCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> transCall, Response<String> response) {
                                String _response = response.body();
                                Log.d("store trans coins", _response);
                                try {
                                    JSONObject jObject = new JSONObject(_response);
                                    if (!jObject.getBoolean("error")) {
                                        StoreAccount.coins = jObject.optInt("new_coins_a");
                                        CustomerAccount.coins = jObject.optInt("new_coins_b");
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


                                        Balance frag = new Balance();
                                        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                                    }
                                } catch (JSONException e) {
                                    //should happen really
                                    Log.d("cant transfer", String.valueOf(e));
                                }
                            }

                            @Override
                            public void onFailure(Call<String> transCall, Throwable t) {
                            Log.d("Fail transfer", t.getMessage());
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        return;
                    }
                });


        AlertDialog confAlert = confirm.create();
        confAlert.show();


            }
    }
