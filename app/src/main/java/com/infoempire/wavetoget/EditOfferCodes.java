package com.infoempire.wavetoget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditOfferCodes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditOfferCodes extends Fragment {

    View view;
    Spinner spinner;
    String type;
    TextView datetext;
    TextView codetext;
    TextView amounttext;
    Button addCodeButton;
    ImageView back;
    DatePickerDialog datePickerDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditOfferCodes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditOfferCodes.
     */
    // TODO: Rename and change types and number of parameters
    public static EditOfferCodes newInstance(String param1, String param2) {
        EditOfferCodes fragment = new EditOfferCodes();
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
        view = inflater.inflate(R.layout.fragment_edit_offer_codes, container, false);
        codetext = view.findViewById(R.id.codetextview);
        amounttext = view.findViewById(R.id.amountedittext);
        datetext = view.findViewById(R.id.datetext);
        type = "points";
        initDatePicker();
        back = view.findViewById(R.id.backbutton4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSettingsPage1 frag = new StoreSettingsPage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        datetext.setText(getTodaysDate());
        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(view);
            }
        });
        addCodeButton = view.findViewById(R.id.addcodebut);
        addCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddOffercode();
            }
        });

//        spinner = view.findViewById(R.id.pointsordollars);
//        ArrayList<String> pointdol = new ArrayList<String>();
//        pointdol.add("points");
//        pointdol.add("dollars");
//
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item,pointdol);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // your code here
//                if(position == 0)
//                {
//                    type = "points";
//                }
//                else
//                {
//                    type = "dollars";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//        });
        GetStoreOfferCodes();
        return view;
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    int _year;
    int _month;
    int _day;
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                _year = year;
                _month = month;
                _day = day;
                String date = makeDateString(day, month, year);
                datetext.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
         _year = cal.get(Calendar.YEAR);
         _month = cal.get(Calendar.MONTH);
         _day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, _year, _month, _day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
    private void GetStoreOfferCodes() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-offercodes");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("storemessages response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    StoreAccount.offercodeList = new ArrayList<OfferCode>();
                    JSONArray jarray = null;
                    try {
                        jarray = new JSONArray(_response);
                        for (int i=0; i < jarray.length(); i++)
                        {
                            OfferCode offercode = new OfferCode();
                            JSONObject oneObject = jarray.getJSONObject(i);
                            // Pulling items from the array
                            offercode.id = oneObject.optInt("id");
                            offercode.name = oneObject.optString("name");
                            offercode.code = oneObject.optString("code");
                            offercode.amount = oneObject.optInt("amount");
                            offercode.type = oneObject.optString("type");
                            offercode.end = oneObject.optString("end");
                            offercode.created = oneObject.optString("created");
                            StoreAccount.offercodeList.add(offercode);
                        }
//                        Cancel();
                        LayoutInflater inf = getLayoutInflater();
                        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
                        for(int i = 0; i < StoreAccount.offercodeList.size(); i++) {
                            LinearLayout layout2 = new LinearLayout(getContext());
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            View item = inf.inflate(R.layout.offercode_layout, null, false);
                            layout2.setId(i);
                            layout2.addView(item);
                            scrollViewLinearlayout.addView(layout2);

                            TextView msg = item.findViewById(R.id.itemname);
                            String desc = StoreAccount.offercodeList.get(i).code;
                            msg.setText(desc);
                            TextView amt = item.findViewById(R.id.amount);
                            amt.setText(StoreAccount.offercodeList.get(i).amount.toString());
                            TextView typ = item.findViewById(R.id.type);
//                            typ.setText(StoreAccount.offercodeList.get(i).type);
                            typ.setText("coins");
                            TextView dur = item.findViewById(R.id.duration);
                            dur.setText(StoreAccount.offercodeList.get(i).end);
                            Button iuse = item.findViewById(R.id.usebtn);
                            Integer id = StoreAccount.offercodeList.get(i).id;
                            iuse.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    RemoveOfferCode(id);
                                    //Cancel();
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
    private void RemoveOfferCode(Integer id) {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "remove-offercode");
        fields.put("session",  Account.session);
        fields.put("id",  String.valueOf(id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("RemoveOffer response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    EditOfferCodes frag = new EditOfferCodes();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void AddOffercode() {
        Log.d("AddOffercode", formattedDateFromString(_year + "-" + _month + "-" + _day));
        String code = codetext.getText().toString();
        String amount = amounttext.getText().toString();
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "add-offercode");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("code",  code);
        fields.put("amount",  amount);
        fields.put("type",  type);
        fields.put("start",  formattedDateFromString(Calendar.getInstance().getTime().toString()));
        fields.put("end",  formattedDateFromString(_year + "-" + _month + "-" + _day));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("addoffer response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    EditOfferCodes frag = new EditOfferCodes();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public String formattedDateFromString(String inputDate){

        String inputFormat = "yyyy-MM-dd";
        String outputFormat = "yyyy-MM-dd hh:mm:ss";
        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new Locale("es", "EST"));
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("es", "EST"));

        // You can set a different Locale, This example set a locale of Country Mexico.
        //SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new Locale("es", "MX"));
        //SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("es", "MX"));

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
        }
        return outputDate;

    }
}