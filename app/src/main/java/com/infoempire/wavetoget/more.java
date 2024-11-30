package com.infoempire.wavetoget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link more#newInstance} factory method to
 * create an instance of this fragment.
 */
public class more extends Fragment {

    View view;

//    TextView planname;
//    TextView plandate;
//    TextView plandesc;
//    Button plansbut;
    Button notesbut;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public more() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment more.
     */
    // TODO: Rename and change types and number of parameters
    public static more newInstance(String param1, String param2) {
        more fragment = new more();
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
        view = inflater.inflate(R.layout.fragment_more, container, false);
//        planname = view.findViewById(R.id.itemname);
//        plandate = view.findViewById(R.id.planduration);
//        plandesc = view.findViewById(R.id.plandesc);
//        plansbut = view.findViewById(R.id.plansbut);
//        plansbut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Plans frag = new Plans();
//                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//            }
//        });
        notesbut = view.findViewById(R.id.notesbut);
        notesbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes frag = new notes(true);
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });

//        planname.setText(CustomerAccount.plan.name);
//        plandate.setText(CustomerAccount.plan.expdate);
//        plandesc.setText(CustomerAccount.plan.details);

        LayoutInflater inf = getLayoutInflater();
        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
//        for(int i = 0; i < CustomerAccount.benefits.size(); i++) {
//            LinearLayout layout2 = new LinearLayout(getContext());
//            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            View item = inf.inflate(R.layout.item_layout, null, false);
//            layout2.setId(i);
//            layout2.addView(item);
//            scrollViewLinearlayout.addView(layout2);
//
//            TextView iname = item.findViewById(R.id.itemname);
//
//            String desc = CustomerAccount.benefits.get(i).description;
//            iname.setText(desc);
//            TextView iprice = item.findViewById(R.id.itemprice);
//            int qty = CustomerAccount.benefits.get(i).quantity;
//            iprice.setText(String.valueOf(qty));
//            int id = CustomerAccount.benefits.get(i).id;
//            int index = i;
//            Button iuse = item.findViewById(R.id.usebtn);
////            if (pts > CustomerAccount.points) {
////                //iuse.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
////            }
//            iuse.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    useBenefit(qty,id,desc,index);
//                    //Cancel();
//                }
//            });
//
//        }
        return view;
    }
//    private void useBenefit(int amount, int id, String desc, int i) {
//        if(amount < 1)
//        {
//            return;
//        }
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "use-benefit");
//        fields.put("session",  Account.session);
//        fields.put("benefit",  String.valueOf(id));
//        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
//        fields.put("name",  desc);
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("add dollars response", _response);
//                if(_response.equals("success"))
//                {
//                    CustomerAccount.benefits.get(i).quantity -= 1;
//                    more frag = new more();
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
}