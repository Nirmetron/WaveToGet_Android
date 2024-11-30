package com.infoempire.wavetoget;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerMorePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerMorePage extends Fragment {

    View view;
    TextView membership;
    TextView details;
    FrameLayout notes;
    FrameLayout redeem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerMorePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerMorePage.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerMorePage newInstance(String param1, String param2) {
        CustomerMorePage fragment = new CustomerMorePage();
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
        view = inflater.inflate(R.layout.fragment_customer_more_page, container, false);
        // Inflate the layout for this fragment
        membership = view.findViewById(R.id.membership);
        details = view.findViewById(R.id.memberdetails);
        notes = view.findViewById(R.id.notesmore);
        redeem = view.findViewById(R.id.redeemablemore);
        notes notefrag = new notes(false);
        getChildFragmentManager().beginTransaction().replace(R.id.notesmore, notefrag).commit();
        GetStoreRedeemables();
        Log.d("user spent", String.valueOf(CustomerAccount.spent));
        if (CustomerAccount.spent<1000) {
            membership.setText("Member");
            details.setText("Details: Basic membership");
        }
        else if (CustomerAccount.spent<2000) {
            membership.setText("Bronze tier");
//            details.setText("Details: 1% transfer discount");
            details.setText("Details: First league");
        }
        else if (CustomerAccount.spent<3000) {
            membership.setText("Silver tier");
//            details.setText("Details: 2% transfer discount");
            details.setText("Details: Second league");

        }
        else if (CustomerAccount.spent<4000) {
            membership.setText("Golden tire");
//            details.setText("Details: 3% transfer discount");
            details.setText("Details: Third league");

        }
        else if (CustomerAccount.spent<5000) {
            membership.setText("Platinum tire");
//            details.setText("Details: 4% transfer discount");
            details.setText("Details: Fourth league");

        }
        else {
            membership.setText("Diamond tier");
//            details.setText("Details: 5% transfer discount(max)");
            details.setText("Details: Final league");

        }
        return view;
    }
    private void GetStoreRedeemables() {

        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-store-redeemables");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("STORE REDEEMABLES", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                {
                    Log.d("response is not empty", "true");
                    try {
                        JSONArray jArray = new JSONArray(_response);
                        StoreAccount.redeemables = new ArrayList<Redeemable>();
                        for (int i=0; i < jArray.length(); i++)
                        {
                            Log.d("jArray.get(i)", String.valueOf(jArray.getJSONObject(i)));
                            try {
                                Redeemable redeemable = new Redeemable();
                                Log.d("rek", String.valueOf(StoreAccount.redeemables.size()));
                                JSONObject oneObject = jArray.getJSONObject(i);
                                redeemable.id = oneObject.getInt("id");

                                redeemable.name = oneObject.getString("name");
                                Log.d("redeem name", redeemable.name);
//                                redeemable.points = oneObject.getInt("points");
                                redeemable.coins = oneObject.optInt("coins");
                                redeemable.description = oneObject.getString("description");
                                Log.d("redeemable", redeemable.toString());
                                StoreAccount.redeemables.add(redeemable);

                            } catch (JSONException e) {
                                // Oops
                                Log.d("error", e.getMessage());
                                if (e.getMessage().equals("No value for coins")) {
                                    try {
                                        Redeemable redeemable = new Redeemable();
                                        Log.d("rek", String.valueOf(StoreAccount.redeemables.size()));
                                        JSONObject oneObject = jArray.getJSONObject(i);
                                        redeemable.id = oneObject.getInt("id");

                                        redeemable.name = oneObject.getString("name");
                                        Log.d("redeem name", redeemable.name);
//                                redeemable.points = oneObject.getInt("points");
//                                        redeemable.coins = oneObject.getInt("coins");
                                        redeemable.coins = oneObject.optInt("points");
                                        redeemable.description = oneObject.getString("description");
                                        Log.d("redeemable", redeemable.toString());
                                        StoreAccount.redeemables.add(redeemable);

                                    } catch (JSONException err) {
                                        // Oops
                                        Log.d("error", String.valueOf(err));
                                    }
                                }
                            }
                        }
                        Log.d("redeemables", StoreAccount.redeemables.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                redeem redeemfrag = new redeem();
                getChildFragmentManager().beginTransaction().replace(R.id.redeemablemore, redeemfrag).commit();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}