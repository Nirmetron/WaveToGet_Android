package com.infoempire.wavetoget;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SmsInvitationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SmsInvitationFragment extends Fragment {

    private static final String TAG = "SmsInvitationFragment";

    View view;
    ImageView back;
    Button btnCancel;
    Button btnSet;
    TextView multilinetext;
    private PopupWindow popWindow;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SmsInvitationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SmsInvitationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SmsInvitationFragment newInstance(String param1, String param2) {
        SmsInvitationFragment fragment = new SmsInvitationFragment();
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
        view = inflater.inflate(R.layout.fragment_sms_invitation, container, false);
        back = view.findViewById(R.id.backbutton4);
        multilinetext = view.findViewById(R.id.editTextTextMultiLine);
        btnCancel = view.findViewById(R.id.btn_cancel_sms_invitation);
        btnSet = view.findViewById(R.id.btn_set_sms_invitation);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClicked();
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSmsInvitationText();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSettingsPage1 frag = new StoreSettingsPage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });

        getSmsInvitationText();

        return view;
    }

    private void getSmsInvitationText() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get");
        fields.put("session",  Account.session);
        fields.put("id",  String.valueOf(Account.store));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.sendSMS(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d(TAG, "onResponse: response = " + _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    try {
                        JSONObject jObject = new JSONObject(_response);
                        String msg = jObject.optString("msg");
                        String storeName = jObject.optString("store_name");

                        multilinetext.setText(msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Message message = new Message();
//                    message.id = Integer.valueOf(_response);
//                    message.message = mes;
//                    StoreAccount.messageList.add(message);
//                    multilinetext.setText("");
//                    EditStoreMessages frag = new EditStoreMessages();
//                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void setSmsInvitationText() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "set");
        fields.put("session",  Account.session);
        fields.put("id",  String.valueOf(Account.store));
        fields.put("message",  multilinetext.getText().toString());
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.sendSMS(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d(TAG, "onResponse: response = " + _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    StoreSettingsPage1 frag = new StoreSettingsPage1();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void cancelButtonClicked() {
        StoreSettingsPage1 frag = new StoreSettingsPage1();
        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
    }
}