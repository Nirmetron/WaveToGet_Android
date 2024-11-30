package com.infoempire.wavetoget;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditStoreRedeemables#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditStoreRedeemables extends Fragment {

    View view;

    TextView codetext;
    TextView amounttext;
    Button addCodeButton;
    ImageView back;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditStoreRedeemables() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditStoreRedeemables.
     */
    // TODO: Rename and change types and number of parameters
    public static EditStoreRedeemables newInstance(String param1, String param2) {
        EditStoreRedeemables fragment = new EditStoreRedeemables();
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
//        GetStoreRedeemables();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_store_redeemables, container, false);
        codetext = view.findViewById(R.id.codetextview);
        amounttext = view.findViewById(R.id.amountedittext);
        back = view.findViewById(R.id.backbutton4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSettingsPage1 frag = new StoreSettingsPage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        addCodeButton = view.findViewById(R.id.addcodebut);
        addCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRedeemable();
            }
        });
        LayoutInflater inf = getLayoutInflater();
        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
        for(int i = 0; i < StoreAccount.redeemables.size(); i++){
            LinearLayout layout2 = new LinearLayout(getContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            View item = inf.inflate(R.layout.item_layout, null, false);
            layout2.setId(i);
            layout2.addView(item);
            scrollViewLinearlayout.addView(layout2);

            TextView iname = item.findViewById(R.id.itemname);
            iname.setText(StoreAccount.redeemables.get(i).name);
            TextView iprice = item.findViewById(R.id.itemprice);
            int pts = StoreAccount.redeemables.get(i).coins;
            iprice.setText(String.valueOf(pts));
            Button iuse = item.findViewById(R.id.usebtn);
            Integer id = StoreAccount.redeemables.get(i).id;
            iuse.setText("REMOVE");
            iuse.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    RemoveRedeemable(id);
                    //Cancel();
                }
            });
        }



        return view;
    }
    private void AddRedeemable() {
        String mes = codetext.getText().toString();
        String amount = amounttext.getText().toString();
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "add-store-redeemable");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("name",  mes);
        fields.put("coins",  amount);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("storemessages response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    Redeemable redeemable = new Redeemable();
                    redeemable.id = Integer.valueOf(_response);
                    redeemable.name = mes;
                    redeemable.coins = Integer.valueOf(amount);
                    StoreAccount.redeemables.add(redeemable);
                    EditStoreRedeemables frag = new EditStoreRedeemables();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void RemoveRedeemable(Integer id) {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "remove-store-redeemable");
        fields.put("session",  Account.session);
        fields.put("id",  String.valueOf(id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("removerede response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    for(int i = 0; i < StoreAccount.redeemables.size(); i++)
                    {
                        if(id == StoreAccount.redeemables.get(i).id) {
                            StoreAccount.redeemables.remove(i);
                            break;
                        }
                    }
                    EditStoreRedeemables frag = new EditStoreRedeemables();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}