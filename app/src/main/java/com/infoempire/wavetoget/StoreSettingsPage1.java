package com.infoempire.wavetoget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreSettingsPage1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreSettingsPage1 extends Fragment {

    View view;
    ImageView back;
    Button editStoreMessagesBut;
    Button editOfferCodesBut;
    Button editRedeemablesBut;
    Button editMembershipBut;
    Button editaccinfobut;
    Button editStoreItemsBut;
    Button editReviewsURL;
    Button showActivity;
    Button btnSmsInvitation;
    Button customersbut;
    Button buyCoins;
    TextView unread;
    Button advert;
    Button referralBut;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreSettingsPage1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreSettingsPage1.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreSettingsPage1 newInstance(String param1, String param2) {
        StoreSettingsPage1 fragment = new StoreSettingsPage1();
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
        view = inflater.inflate(R.layout.fragment_store_settings_page1, container, false);
        back = view.findViewById(R.id.backbutton3);
        editStoreMessagesBut =  view.findViewById(R.id.storemessbut);
        editOfferCodesBut =  view.findViewById(R.id.editoffercodebut);
        editRedeemablesBut =  view.findViewById(R.id.editredeemablebut);
//        editMembershipBut =  view.findViewById(R.id.editmembershipbut);
        editaccinfobut = view.findViewById(R.id.editaccinfobut);
        editStoreItemsBut = view.findViewById(R.id.editstoreitemsbut);
        editReviewsURL = view.findViewById(R.id.editreviewsbut);
        showActivity = view.findViewById(R.id.viewstoreactivity);
        buyCoins = view.findViewById(R.id.buyCoins);
        customersbut = view.findViewById(R.id.customerbut);
        btnSmsInvitation = view.findViewById(R.id.btnSmsInvitation);
        advert = view.findViewById(R.id.advert);
        unread = view.findViewById(R.id.unreadstorecounter);
//        referralBut = view.findViewById(R.id.referralbut);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        editStoreMessagesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStoreMessages frag = new EditStoreMessages();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        editaccinfobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAccountInfo frag = new EditAccountInfo();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        editOfferCodesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditOfferCodes frag = new EditOfferCodes();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        editReviewsURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreEditReviewsURL frag = new StoreEditReviewsURL();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        buyCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyCoins buyfrag = new BuyCoins();
                getParentFragmentManager().beginTransaction().replace(R.id.page, buyfrag).commit();
            }
        });
        showActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreActivity frag = new StoreActivity();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        editRedeemablesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStoreRedeemables frag = new EditStoreRedeemables();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        btnSmsInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsInvitationFragment frag = new SmsInvitationFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
//        editMembershipBut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditPlans frag = new EditPlans();
//                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//            }
//        });
        editStoreItemsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStoreProducts frag = new EditStoreProducts();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        customersbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerListFragment frag = new CustomerListFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdvertisementBannerFragment frag = new AdvertisementBannerFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "unread-notification-count");
        fields.put("session", Account.session);
//        fields.put("cardholder",  String.valueOf(CustomerAccount.user));
        fields.put("store",  String.valueOf(StoreAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("store unread count", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty() && !_response.equals("0") && !_response.contains("b"))
                {
                    unread.setText(_response);
                    unread.setVisibility(View.VISIBLE);
                }
                else {
                    Log.d("failed unread or 0", _response);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("failed unread store", t.getMessage());
            }
        });
//        referralBut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddReferral frag = new AddReferral();
//                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//            }
//        });
        return view;
    }
}