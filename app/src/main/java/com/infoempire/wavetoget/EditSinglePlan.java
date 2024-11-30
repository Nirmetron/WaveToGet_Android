package com.infoempire.wavetoget;

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
 * Use the {@link EditSinglePlan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditSinglePlan extends Fragment {

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

    public EditSinglePlan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditSinglePlan.
     */
    // TODO: Rename and change types and number of parameters
    public static EditSinglePlan newInstance(String param1, String param2) {
        EditSinglePlan fragment = new EditSinglePlan();
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
        view = inflater.inflate(R.layout.fragment_edit_single_plan, container, false);
        codetext = view.findViewById(R.id.codetextview);
        amounttext = view.findViewById(R.id.amountedittext);
        back = view.findViewById(R.id.backbutton4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPlans frag = new EditPlans();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        addCodeButton = view.findViewById(R.id.addcodebut);
        addCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBenefit();
            }
        });
        GetBenefits();
        return view;
    }
    private void GetBenefits() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-plan");
        fields.put("session",  Account.session);
        fields.put("id",  String.valueOf(StoreAccount.currentPlanId));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("storemessages response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    StoreAccount.benefitList = new ArrayList<Benefit>();
                    JSONArray jarray = null;
                    try {
                        jarray = new JSONArray(_response);
                        for (int i=0; i < jarray.length(); i++)
                        {
                            Benefit benefit = new Benefit();
                            JSONObject oneObject = jarray.getJSONObject(i);
                            // Pulling items from the array
                            benefit.id = oneObject.optInt("id");
                            benefit.benefit = oneObject.optInt("benefit");
                            benefit.startdate = oneObject.optString("startdate");
                            benefit.expirydate = oneObject.optString("expirydate");
                            benefit.description = oneObject.optString("description");
                            StoreAccount.benefitList.add(benefit);
                        }
//                        Cancel();
                        LayoutInflater inf = getLayoutInflater();
                        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
                        for(int i = 0; i < StoreAccount.benefitList.size(); i++) {
                            LinearLayout layout2 = new LinearLayout(getContext());
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            View item = inf.inflate(R.layout.item_layout, null, false);
                            layout2.setId(i);
                            layout2.addView(item);
                            scrollViewLinearlayout.addView(layout2);

                            TextView msg = item.findViewById(R.id.itemname);
                            msg.setText(StoreAccount.benefitList.get(i).description);
                            Button iuse = item.findViewById(R.id.usebtn);
                            iuse.setText("REMOVE");
                            Integer id = StoreAccount.benefitList.get(i).id;
                            iuse.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    RemoveBenefit(id);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void RemoveBenefit(Integer id) {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "remove-benefit");
        fields.put("session",  Account.session);
        fields.put("id",  String.valueOf(id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("storemessages response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    EditSinglePlan frag = new EditSinglePlan();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void AddBenefit() {
        String mes = codetext.getText().toString();
        String amount = amounttext.getText().toString();
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "add-benefit");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("description",  mes);
        fields.put("duration",  "3");
        fields.put("plan",  String.valueOf(StoreAccount.currentPlanId));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("storemessages response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    EditSinglePlan frag = new EditSinglePlan();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}