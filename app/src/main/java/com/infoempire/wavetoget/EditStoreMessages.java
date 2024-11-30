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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditStoreMessages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditStoreMessages extends Fragment {


    View view;
    ImageView back;
    Button addNewMessBut;
    TextView multilinetext;
    TextView error;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditStoreMessages() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditStoreMessages.
     */
    // TODO: Rename and change types and number of parameters
    public static EditStoreMessages newInstance(String param1, String param2) {
        EditStoreMessages fragment = new EditStoreMessages();
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
        view = inflater.inflate(R.layout.fragment_edit_store_messages, container, false);
        GetStoreMessages();
        back = view.findViewById(R.id.backbutton4);
        multilinetext = view.findViewById(R.id.editTextTextMultiLine);
        addNewMessBut = view.findViewById(R.id.addnewmessage);
        error = view.findViewById(R.id.errortext);
        addNewMessBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStoreMessages();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSettingsPage1 frag = new StoreSettingsPage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        return view;
    }
    private void GetStoreMessages() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-storemessages");
        fields.put("session",  Account.session);
        fields.put("id",  String.valueOf(StoreAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("storemessages response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    StoreAccount.messageList = new ArrayList<Message>();
                    JSONArray jarray = null;
                    try {
                        jarray = new JSONArray(_response);
                        for (int i=0; i < jarray.length(); i++)
                        {
                            Message message = new Message();
                            JSONObject oneObject = jarray.getJSONObject(i);
                            // Pulling items from the array
                            message.id = oneObject.optInt("id");
                            message.store = oneObject.optInt("store");
                            message.message = oneObject.optString("message");
                            message.created = oneObject.optString("created");
                            StoreAccount.messageList.add(message);
                        }
                        Collections.reverse(StoreAccount.messageList);
//                        Cancel();
                        LayoutInflater inf = getLayoutInflater();
                        LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin); // The layout inside scroll view
                        for(int i = 0; i < StoreAccount.messageList.size(); i++) {
                            LinearLayout layout2 = new LinearLayout(getContext());
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            View item = inf.inflate(R.layout.message_layout, null, false);
                            layout2.setId(i);
                            layout2.addView(item);
                            scrollViewLinearlayout.addView(layout2);

                            TextView msg = item.findViewById(R.id.itemname);
                            String desc = StoreAccount.messageList.get(i).message;
                            msg.setText(desc);
                            Button iuse = item.findViewById(R.id.usebtn);
                            Integer id = StoreAccount.messageList.get(i).id;
                            iuse.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    RemoveStoreMessages(id);
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
    private void AddStoreMessages() {
        String mes = multilinetext.getText().toString();
        if (mes.isEmpty()){
            error.setText("Message is empty!");
            return;
        }
        error.setText("");
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "add-storemessage");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("message",  mes);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("storemessages response", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                {
                    Message message = new Message();
                    message.id = Integer.valueOf(_response);
                    message.message = mes;
                    StoreAccount.messageList.add(message);
                    multilinetext.setText("");
                    EditStoreMessages frag = new EditStoreMessages();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void RemoveStoreMessages(Integer id) {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "remove-storemessage");
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
                    for(int i = 0; i < StoreAccount.messageList.size(); i++)
                    {
                        if(id == StoreAccount.messageList.get(i).id) {
                            StoreAccount.messageList.remove(i);
                            break;
                        }
                    }
                    EditStoreMessages frag = new EditStoreMessages();
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}