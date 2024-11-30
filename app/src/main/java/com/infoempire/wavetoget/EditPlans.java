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

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPlans#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPlans extends Fragment {

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

    public EditPlans() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPlans.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPlans newInstance(String param1, String param2) {
        EditPlans fragment = new EditPlans();
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
        view = inflater.inflate(R.layout.fragment_edit_plans, container, false);
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
                AddPlan();
            }
        });
        LayoutInflater inf = getLayoutInflater();
        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
        for(int i = 0; i < StoreAccount.plans.size(); i++){
            LinearLayout layout2 = new LinearLayout(getContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            View item = inf.inflate(R.layout.plan_layout, null, false);
            layout2.setId(i);
            layout2.addView(item);
            scrollViewLinearlayout.addView(layout2);

            TextView iname = item.findViewById(R.id.itemname);
            iname.setText(StoreAccount.plans.get(i).name);
            Button iuse = item.findViewById(R.id.usebtn);
            Integer id = StoreAccount.plans.get(i).id;
            iuse.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    RemovePlan(id);

                }
            });
            Button editplanbut = item.findViewById(R.id.editbut);
            editplanbut.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    StoreAccount.currentPlanId = id;
                    EditSinglePlan frag = new EditSinglePlan();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            });
        }
        return view;
    }
    private void AddPlan() {
        String mes = codetext.getText().toString();
        String amount = amounttext.getText().toString();
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "create-plan");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("name",  mes);
        fields.put("months",  amount);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("storemessages response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    Plan plan = new Plan();
                    plan.id = Integer.valueOf(_response);
                    plan.name = mes;
                    plan.term_months = Integer.valueOf(amount);
                    StoreAccount.plans.add(plan);
                    EditPlans frag = new EditPlans();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void RemovePlan(Integer id) {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "remove-plan");
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
                    for(int i = 0; i < StoreAccount.plans.size(); i++)
                    {
                        if(id == StoreAccount.plans.get(i).id) {
                            StoreAccount.plans.remove(i);
                            break;
                        }
                    }
                    EditPlans frag = new EditPlans();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}