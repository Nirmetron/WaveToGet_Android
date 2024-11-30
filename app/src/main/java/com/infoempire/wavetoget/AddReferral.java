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
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link AddReferral#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class AddReferral extends Fragment {
//
//    View view;
//    EditText sendamt;
//    EditText recamt;
//    EditText name;
//    EditText msg;
//    ImageView back;
//    Button save;
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
//    public AddReferral() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment AddReferral.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static AddReferral newInstance(String param1, String param2) {
//        AddReferral fragment = new AddReferral();
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
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.future_use_fragment_add_referral, container, false);
//        sendamt = view.findViewById(R.id.sendamt);
//        recamt = view.findViewById(R.id.recamt);
//        name = view.findViewById(R.id.nametxt);
//        msg = view.findViewById(R.id.editTextTextMultiLine2);
//
//        if(!StoreAccount.referralName.isEmpty())
//        {
//            name.setText(StoreAccount.referralName);
//        }
//        if(!StoreAccount.referralmsg.isEmpty())
//        {
//            msg.setText(StoreAccount.referralmsg);
//        }
//        if(StoreAccount.referralSenderAmt != 0)
//        {
//            sendamt.setText(StoreAccount.referralSenderAmt.toString());
//        }
//        if(StoreAccount.referralRecAmt != 0)
//        {
//            recamt.setText(StoreAccount.referralRecAmt.toString());
//        }
//
//        save = view.findViewById(R.id.savecred);
//        back = view.findViewById(R.id.backbutton4);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Save();
//            }
//        });
//
//        return view;
//    }
//
//    private void Save() {
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "add-referral");
//        fields.put("session",  Account.session);
//        fields.put("store",  String.valueOf(StoreAccount.id));
//        fields.put("name", name.getText().toString());
//        fields.put("message", msg.getText().toString());
//        fields.put("senderamount", sendamt.getText().toString());
//        fields.put("recieveramount", recamt.getText().toString());
//
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("adref response", _response);
//                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
//                {
//                    StoreAccount.referralRecAmt = Float.parseFloat(recamt.getText().toString());
//                    StoreAccount.referralSenderAmt = Float.parseFloat(sendamt.getText().toString());
//                    StoreAccount.referralName = name.getText().toString();
//                    StoreAccount.referralmsg = msg.getText().toString();
//                    getActivity().finish();
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