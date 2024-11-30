package com.infoempire.wavetoget;

import android.graphics.Color;
import android.graphics.Typeface;
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
 * Use the {@link CustomerActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerActivity extends Fragment {

    View view;
    ImageView backbut;
    Boolean loading;
    TextView pagenum;
    TextView errortext;
    ImageView next;
    ImageView prev;
    Integer pagecount = 0;
    Integer maxpage = 0;
    Button readAll;
    Call<String> call;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment activity.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerActivity newInstance(String param1, String param2) {
        CustomerActivity fragment = new CustomerActivity();
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
    public void onDestroy() {
        super.onDestroy();
        if (call !=null){
            call.cancel(); //RxJava
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_activity, container, false);
        loading = false;
        backbut = view.findViewById(R.id.backbutton);
        next = view.findViewById(R.id.nextarrow2);
        prev = view.findViewById(R.id.prevarrow2);
        pagenum = view.findViewById(R.id.pagenumber2);
        errortext = view.findViewById(R.id.activityerror);
        readAll = view.findViewById(R.id.custreadallbtn);
        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loading)
                    return;
                if(!Account.isCust)
                    Cancel();
                else
                {
//                    customerpage1 frag = new customerpage1();
                    CustomerMorePage frag = new CustomerMorePage();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }

            }
        });
        readAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> fields = new HashMap<>();
                fields.put("action", "mark-all-read-ch");
                fields.put("session",  Account.session);
                fields.put("cardholder",  String.valueOf(CustomerAccount.id));
                fields.put("key", "KEY REMOVED");
                call = MainActivity.apiService.createPost(fields);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String _response = response.body();
                        Log.d("Mark all read cust", _response);
                        if(!_response.equals("nosession") && !_response.equals("failed"))
                        {
                            GetCustomerActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        errortext.setText("Unable to read all. Check internet connection");
                        Log.d("read all cust error", t.getMessage());
                    }
                });
            }
        });
        GetCustomerActivity();

        return view;
    }
    private void GetCustomerActivity() {
        GetActivityCount();
        Map<String, String> fields = new HashMap<>();
        Log.d("test", String.valueOf(CustomerAccount.id));
        fields.put("action", "get-transactions-cardholder");
        fields.put("session",  Account.session);
//        fields.put("store", String.valueOf(CustomerAccount.store));
        fields.put("page",  String.valueOf(pagecount));
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("customer activity", _response);
                loading = false;
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                {
                    CustomerAccount.custactivity = new ArrayList<ActivityObj>();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(_response);
                        Log.d("activity response", _response);
                        for (int i=0; i < jsonArray.length(); i++)
                        {
                            ActivityObj act = new ActivityObj();
                            JSONObject oneObject = jsonArray.getJSONObject(i);
                            // Pulling items from the array
                            if (oneObject.optString("transactor_displayname").equals("null")){
                                act.publishedby = "system";
                            }
                            else {
                                act.publishedby = oneObject.optString("transactor_displayname");
                            }
                            act.date = oneObject.optString("created");
                            act.description = oneObject.optString("description");
                            if (1 == oneObject.getInt("was_read")) {
                                act.wasRead = true;
                            }
                            act.id = oneObject.optString("id");
                            CustomerAccount.custactivity.add(act);
                        }
                        LayoutInflater inf = getLayoutInflater();
                        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
                        scrollViewLinearlayout.removeAllViews();
                        for(int i = 0; i < CustomerAccount.custactivity.size(); i++) {
                            Integer container = i;
                            LinearLayout layout2 = new LinearLayout(getContext());
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            View item = inf.inflate(R.layout.activity_layout, null, false);
                            layout2.setId(i);
                            layout2.addView(item);
                            scrollViewLinearlayout.addView(layout2);

                            TextView iname = item.findViewById(R.id.messagetext);
                            View dot = item.findViewById(R.id.markRead);

                            String desc = CustomerAccount.custactivity.get(i).description;
                            iname.setText(desc);

                            TextView date = item.findViewById(R.id.date);
                            date.setText(CustomerAccount.custactivity.get(i).date);

                            TextView publishedby = item.findViewById(R.id.publishedby);
                            publishedby.setText(CustomerAccount.custactivity.get(i).publishedby);
                            if (!CustomerAccount.custactivity.get(i).wasRead){
                                publishedby.setTypeface(null, Typeface.BOLD);
                                date.setTypeface(null, Typeface.BOLD);
                                iname.setTypeface(null, Typeface.BOLD);
                            }
                            else {
                                dot.setVisibility(View.GONE);
                            }
                            item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Map<String, String> fields = new HashMap<>();
                                    Log.d("mark as read", CustomerAccount.custactivity.get(container).id);
                                    fields.put("action", "mark-as-read");
                                    fields.put("session",  Account.session);
//        fields.put("store", String.valueOf(CustomerAccount.store));
                                    fields.put("id",  CustomerAccount.custactivity.get(container).id);
                                    fields.put("key", "KEY REMOVED");
                                    Call<String> call = MainActivity.apiService.createPost(fields);
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String _response = response.body();
                                            Log.d("mark as read resp", _response);
                                            if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                                            {
                                                dot.setVisibility(View.GONE);
                                                publishedby.setTypeface(null, Typeface.NORMAL);
                                                date.setTypeface(null, Typeface.NORMAL);
                                                iname.setTypeface(null, Typeface.NORMAL);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d("cant mark as read", t.getMessage());
                                            errortext.setText("Unable to set as read. Check internet connection");
                                        }
                                    });
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loading = false;
                errortext.setText("Unable to load activities, check internet connection");
                Log.d("activity problem", t.getMessage());
            }
        });
    }
    private void NextPage(){
        pagecount++;
        GetCustomerActivity();
    }
    private void PrevPage(){
        pagecount--;
        GetCustomerActivity();
    }
    private void GetActivityCount(){
        Map<String, String> fields = new HashMap<>();
        Log.d("test", String.valueOf(CustomerAccount.id));
        fields.put("action", "get-transactions-cardholder-count");
        fields.put("session",  Account.session);
//        fields.put("store", String.valueOf(CustomerAccount.store));
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("max pages", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                {
//                    try {
//                        JSONObject jObject = new JSONObject(_response);
//                        maxpage = jObject.optInt("page_count");
                    maxpage = Integer.parseInt(_response);
                    pagenum.setText("Page " + String.valueOf(pagecount+1) + "/" + String.valueOf(maxpage));
                    if (pagecount==0){
                        prev.setColorFilter(Color.parseColor("#8E8E93"));
                    }
                    else {
                        prev.setColorFilter(Color.parseColor("#4275B3"));
                        prev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PrevPage();
                                prev.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                            }
                        });
                    }
                    if (!(maxpage==(pagecount+1))){
                        next.setColorFilter(Color.parseColor("#4275B3"));
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                NextPage();
                                next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                            }
                        });
                    }
                    else {
                        next.setColorFilter(Color.parseColor("#8E8E93"));
                    }

//                    } catch (JSONException ex) {
//                        Log.d("Penis", ex.getMessage());
//                        throw new RuntimeException(ex);
//                    }

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    void Cancel()
    {
//        more frag = new more();
//        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
        LoadedAccount.lastClicked.performClick();
    }
}