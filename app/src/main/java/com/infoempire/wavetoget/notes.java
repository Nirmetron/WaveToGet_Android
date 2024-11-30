package com.infoempire.wavetoget;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
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
 * Use the {@link notes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notes extends Fragment {

    View view;
    EditText notestext;
    ImageView eyebut;
    ImageView next;
    ImageView prev;
    Button addnotebut;
    TableLayout addnotesfrag;
    TextView pagenum;
    Group pagecontrol;
    Call<String> call;
    public static Boolean showPages;
    int pagecount = 0;
    int maxpage;
    public boolean loading = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public notes(Boolean showPages) {
        this.showPages = showPages;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notes.
     */
    // TODO: Rename and change types and number of parameters
    public static notes newInstance(String param1, String param2) {
        notes fragment = new notes(true);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call !=null){
            call.cancel(); //RxJava
        }
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
        view = inflater.inflate(R.layout.fragment_notes, container, false);
        loading = false;
        addnotebut = view.findViewById(R.id.addnotebut);
        eyebut = view.findViewById(R.id.eyebut);
        notestext = view.findViewById(R.id.notetext);
        addnotesfrag = view.findViewById(R.id.storeaddnotes);
        next = view.findViewById(R.id.nextarrow);
        prev = view.findViewById(R.id.prevarrow);
        pagenum = view.findViewById(R.id.pagenumber);
        pagecontrol = view.findViewById(R.id.pagecontrol);
        if (!showPages){
            pagecontrol.setVisibility(View.GONE);
        }
        eyebut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ToggleEye();
            }
        });
        if (Account.isCust) {
            addnotesfrag.setVisibility(View.GONE);
        }
        else  {
            if (showPages){
                addnotebut.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        AddNote();
                    }
                });
            }
            else {
                addnotesfrag.setVisibility(View.GONE);
            }
        }
        GetNotes();


        return view;
    }
    private void NextPage(){
        pagecount++;
        GetNotes();
    }
    private void PrevPage(){
        pagecount--;
        GetNotes();
    }

    private void GetNotes() {
        GetRecordsCount();
        Map<String, String> fields = new HashMap<>();
        Log.d("test", String.valueOf(CustomerAccount.id));
        fields.put("action", "get-records");
        fields.put("record_format", "5");
        fields.put("page", String.valueOf(pagecount));
        fields.put("store", String.valueOf(CustomerAccount.store));
        fields.put("session",  Account.session);
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("CUSTOMER NOTES", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                {
                    CustomerAccount.custnotes = new ArrayList<Note>();
                    JSONArray jarray = null;
                    try {
                        jarray = new JSONArray(_response);
                        for (int i=0; i < jarray.length(); i++)
                        {
                            Note note = new Note();
                            JSONObject oneObject = jarray.getJSONObject(i);
                            // Pulling items from the array
                            note.id = oneObject.optInt("id");
                            note.metanum = oneObject.optInt("metanum");
                            note.created = oneObject.optString("created");
                            note.creator = oneObject.optString("creator_displayname");
                            note.title = oneObject.optString("title");
                            note.text = oneObject.optString("text");
                            CustomerAccount.custnotes.add(note);
                        }
//                        Cancel();
                        Collections.reverse(CustomerAccount.custnotes);
                    LayoutInflater inf = getLayoutInflater();
                    LinearLayout scrollViewLinearlayout = view.findViewById(R.id.lin);  // The layout inside scroll view
                    scrollViewLinearlayout.removeAllViews();
                    for(int i = 0; i < CustomerAccount.custnotes.size(); i++) {
                        LinearLayout layout2 = new LinearLayout(getContext());
                        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        View item = inf.inflate(R.layout.note_layout, null, false);
                        layout2.setId(i);
                        layout2.addView(item);
                        scrollViewLinearlayout.addView(layout2);
                        Note curNote = CustomerAccount.custnotes.get(i);

                        TextView msg = item.findViewById(R.id.messagetext);
                        String desc = curNote.text;
                        msg.setText(desc);

                        TextView pubby = item.findViewById(R.id.publishedby);
                        pubby.setText(curNote.creator);

                        TextView notedate = item.findViewById(R.id.notedate);
                        notedate.setText(curNote.created);

                        ImageView notedelete = item.findViewById(R.id.notedelete);
                        if (!Account.isCust){
                            notedelete.setVisibility(View.GONE);
                        }
                        else {
                            notedelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Map<String, String> delete = new HashMap<>();
                                    delete.put("action", "remove-record");
                                    delete.put("store", String.valueOf(CustomerAccount.store));
                                    delete.put("session",  Account.session);
                                    delete.put("id", String.valueOf(curNote.id));
                                    delete.put("key", "KEY REMOVED");
                                    Call<String> delCall = MainActivity.apiService.createPost(delete);
                                    delCall.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String _response = response.body();
                                            Log.d("Delete response", _response);
                                            if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                                            {
                                                GetNotes();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d("failed to delete", t.getMessage());
                                        }
                                    });
                                }
                            });
                        }

                        ImageView eyeimg = item.findViewById(R.id.eyeimg);
                        if(CustomerAccount.custnotes.get(i).metanum == 1)
                            eyeimg.setImageResource(R.drawable.eyeno);

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

    private void GetRecordsCount(){
        Map<String, String> fields = new HashMap<>();
        Log.d("test", String.valueOf(CustomerAccount.id));
        fields.put("action", "get-records-count");
        fields.put("record_format", "5");
//        fields.put("page", String.valueOf(pagecount));
        fields.put("session",  Account.session);
        fields.put("store", String.valueOf(CustomerAccount.store));
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("max pages", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty() && !_response.equals("none"))
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

    private void AddNote() {
        if(loading)
            return;
        loading = true;
        String metanum = "1";
        if(eye)
        {
            metanum = "0";
        }
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "add-record");
        fields.put("session",  Account.session);
        fields.put("permissiontype",  "");
        fields.put("recordtype",  "5");
        fields.put("title",  "Note");
        fields.put("metanum",  metanum);
        fields.put("metavar",  "");
        fields.put("metatime",  "");
        fields.put("text",  notestext.getText().toString());
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loading = false;
                String _response = response.body();
                Log.d("add notes response", _response);
                if(_response.equals("success"))
                {
                    notes frag = new notes(true);
                    getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loading = false;
            }
        });
    }

    boolean eye = true;
    void ToggleEye()
    {
        if(eye)
        {
            eye = false;
            eyebut.setImageResource(R.drawable.eyeno);
        }
        else
        {
            eye = true;
            eyebut.setImageResource(R.drawable.eye);
        }
    }
}

