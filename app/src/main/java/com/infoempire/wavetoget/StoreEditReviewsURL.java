package com.infoempire.wavetoget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreEditReviewsURL#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreEditReviewsURL extends Fragment {
    View view;
    EditText reviewstext;
    ImageView back;
    Button save;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreEditReviewsURL() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreEditReviewsURL.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreEditReviewsURL newInstance(String param1, String param2) {
        StoreEditReviewsURL fragment = new StoreEditReviewsURL();
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
        view = inflater.inflate(R.layout.fragment_store_edit_reviews_url, container, false);
        reviewstext = view.findViewById(R.id.inputstorereviews);
        save = view.findViewById(R.id.savereviewsurl);
        back = view.findViewById(R.id.backbutton2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSettingsPage1 frag = new StoreSettingsPage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
        return view;
    }
    private void Save() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "update-store");
        fields.put("session",  Account.session);
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("user",  String.valueOf(Account.id));
        if(!reviewstext.getText().toString().isEmpty()) {
            fields.put("GoogleReviewURL", reviewstext.getText().toString());
            StoreAccount.name = reviewstext.getText().toString();
        }
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("editacc response", _response);
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
}