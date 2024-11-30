//package com.infoempire.wavetoget;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.res.ColorStateList;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//
//import androidx.annotation.RequiresApi;
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link rewards#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class rewards extends Fragment {
//
//    View view;
//    Button addstamp;
//    boolean loading;
//    TextView collectiontext;
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    int stampsheetid = 0;
//    public rewards() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment rewards.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static rewards newInstance(String param1, String param2) {
//        rewards fragment = new rewards();
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
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_rewards, container, false);
//        addstamp = view.findViewById(R.id.addstamp);
//        Stampsheet storeStampSheet = StoreAccount.stampsheets.get(0);
//        stampsheetid = storeStampSheet.id;
//        collectiontext = view.findViewById(R.id.collectiontext);
//
//
//        List<Stamp> customerStamps = new ArrayList<>();
//
//        for (int i = 0; i < CustomerAccount.stamps.size(); i++) {
//            if(CustomerAccount.stamps.get(i).stampsheet == storeStampSheet.id)
//            {
//                customerStamps.add(CustomerAccount.stamps.get(i));
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            if (i < customerStamps.size()) {
//                ImageView star = view.findViewWithTag("star" + i);
//                star.setColorFilter(Color.parseColor("#4275B3"));
//                TextView startext = view.findViewWithTag("text" + i);
//                long yourNumber = CustomerAccount.stamps.get(i).created;
//                yourNumber *= 1000;
//                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-mm-dd");
//                Date date = new Date(yourNumber);
//                startext.setText(DateFormat.getDateInstance().format(date));
//            }
//        }
//
//        if(customerStamps.size() >= 10)
//        {
//            addstamp.setText("CLAIM REWARD");
//            addstamp.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v) {
//                    ClaimReward();
//                }
//            });
//        }
//        else
//        {
//            addstamp.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v) {
//                    AddStamp();
//                }
//            });
//        }
//
//        collectiontext.setText("Collect " + String.valueOf(10 - customerStamps.size() + " more to get your " + storeStampSheet.prize));
//        addstamp.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4275B3")));
//        loading = false;
//        return view;
//    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void AddStamp() {
//        if(stampsheetid == 0 || loading)
//            return;
//            loading = true;
//            addstamp.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "add-stamp");
//        fields.put("session",  Account.session);
//        fields.put("stampsheet",  String.valueOf(stampsheetid));
//        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("added stamp", _response);
//                if(_response.equals("success"))
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
//                    builder1.setMessage("Stamp added!");
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
//                    Stamp stamp = new Stamp();
//                    stamp.stampsheet = stampsheetid;
//                    Long currentTime = Calendar.getInstance().getTimeInMillis();
//                    currentTime /= 1000;
//                    stamp.created = currentTime.intValue();
//                    CustomerAccount.stamps.add(stamp);
//                    rewards frag = new rewards();
//                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void ClaimReward() {
//        if(stampsheetid == 0 || loading)
//            return;;
//        loading = true;
//        addstamp.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "claim-reward");
//        fields.put("session",  Account.session);
//        fields.put("stampsheet",  String.valueOf(stampsheetid));
//        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("added stamp", _response);
//                if(_response.equals("success"))
//                {
//                    CustomerAccount.stamps.clear();
//                    rewards frag = new rewards();
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
//                    builder1.setMessage(StoreAccount.stampsheets.get(0).prize + " claimed!");
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
//                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }
//}
