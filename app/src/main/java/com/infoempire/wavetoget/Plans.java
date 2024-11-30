//package com.infoempire.wavetoget;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Plans#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class Plans extends Fragment {
//
//    View view;
//    ImageView backbut;
//    Boolean loading;
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public Plans() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Plans.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static Plans newInstance(String param1, String param2) {
//        Plans fragment = new Plans();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_plans, container, false);
//        loading = false;
//        backbut = view.findViewById(R.id.backbutton);
//        backbut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(loading)
//                    return;
//                Cancel();
//            }
//        });
//        // Inflate the layout for this fragment
//        LayoutInflater inf = getLayoutInflater();
//        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
//        for(int i = 0; i < StoreAccount.plans.size(); i++) {
//            LinearLayout layout2 = new LinearLayout(getContext());
//            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            View item = inf.inflate(R.layout.item_layout, null, false);
//            layout2.setId(i);
//            layout2.addView(item);
//            scrollViewLinearlayout.addView(layout2);
//
//            TextView iname = item.findViewById(R.id.itemname);
//
//            String desc = StoreAccount.plans.get(i).name;
//            iname.setText(desc);
//            TextView iprice = item.findViewById(R.id.itemprice);
//            int qty = StoreAccount.plans.get(i).term_months;
//            iprice.setText(String.valueOf(qty));
//            int id = StoreAccount.plans.get(i).id;
//            int index = i;
//            Button iuse = item.findViewById(R.id.usebtn);
////            if (pts > CustomerAccount.points) {
////                //iuse.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
////            }
//            iuse.setText("Enroll");
//            iuse.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    addplan(id,index);
//                    //Cancel();
//                }
//            });
//
//        }
//        return view;
//    }
//    private void addplan(int id, int i) {
//        if(loading)
//            return;
//        loading = true;
//        Log.d("button click works", "click");
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "add-plan");
//        fields.put("session",  Account.session);
//        fields.put("plan",  String.valueOf(id));
//        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
//        fields.put("store",  String.valueOf(StoreAccount.id));
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("add plan response", _response);
//                if(_response.equals("success"))
//                {
//                    GetCustomerPlans();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("add plan response", "t.getMessage()");
//                loading = false;
//            }
//        });
//    }
//    private void GetCustomerPlans() {
//
//        Map<String, String> fields = new HashMap<>();
//        Log.d("test", String.valueOf(CustomerAccount.id));
//        fields.put("action", "get-cardholder-plan");
//        fields.put("session",  Account.session);
//        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("CUSTOMER PLANS", _response);
//                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
//                {
//                    try {
//                        CustomerAccount.plan = new Plan();
//                        JSONObject jObject = new JSONObject(_response);
//                        CustomerAccount.plan.name = jObject.optString("name");
//                        CustomerAccount.plan.details = jObject.optString("details");
//                        CustomerAccount.plan.expdate = jObject.optString("expirydate");
//                        CustomerAccount.plan.id = jObject.optInt("id");
//                        GetCustomerBenefits();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        loading = false;
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                loading = false;
//            }
//        });
//    }
//    private void GetCustomerBenefits() {
//
//        Map<String, String> fields = new HashMap<>();
//        Log.d("test", String.valueOf(CustomerAccount.id));
//        fields.put("action", "get-cardholder-benefits");
//        fields.put("session",  Account.session);
//        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("CUSTOMER benefits", _response);
//                loading = false;
//                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
//                {
//                    CustomerAccount.benefits = new ArrayList<Benefit>();
//                    JSONArray jArrayBenefits = null;
//                    try {
//                        jArrayBenefits = new JSONArray(_response);
//                        for (int i=0; i < jArrayBenefits.length(); i++)
//                        {
//                            Benefit benefit = new Benefit();
//                            JSONObject oneObject = jArrayBenefits.getJSONObject(i);
//                            // Pulling items from the array
//                            benefit.id = oneObject.optInt("id");
//                            benefit.benefit = oneObject.optInt("benefit");
//                            benefit.quantity = oneObject.optInt("quantity");
//                            benefit.startdate = oneObject.optString("startdate");
//                            benefit.expirydate = oneObject.optString("expirydate");
//                            benefit.description = oneObject.optString("description");
//                            CustomerAccount.benefits.add(benefit);
//                        }
//                        Cancel();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        loading = false;
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                loading = false;
//            }
//        });
//    }
//
//    void Cancel()
//    {
//        more frag = new more();
//        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//    }
//}