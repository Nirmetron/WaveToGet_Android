package com.infoempire.wavetoget;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Balance extends Fragment {
    View view;
    TextView client_coins;
    TextView membership;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public Balance() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment testfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Balance newInstance(String param1, String param2) {
        Balance fragment = new Balance();
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
        notes.showPages = false;
        view = inflater.inflate(R.layout.activity_balance, container, false);
        CustomerAccount.messageString = "";
        client_coins = view.findViewById(R.id.coinsClientValue);
        membership = view.findViewById(R.id.clientMembership);
        Log.d("coinS", String.valueOf(CustomerAccount.coins));
        client_coins.setText(String.valueOf(CustomerAccount.coins));
//        shop_coins = view.findViewById(R.id.coinsStoreValue);
//        shop_coins.setText(String.valueOf(StoreAccount.coins));

        if (CustomerAccount.spent<1000) {
            membership.setText("Member");
//            details.setText("Details: Basic membership");
        }
        else if (CustomerAccount.spent<2000) {
            membership.setText("Bronze tier");
//            details.setText("Details: 1% transfer discount");
//            details.setText("Details: First league");
        }
        else if (CustomerAccount.spent<3000) {
            membership.setText("Silver tier");
//            details.setText("Details: 2% transfer discount");
//            details.setText("Details: Second league");

        }
        else if (CustomerAccount.spent<4000) {
            membership.setText("Golden tire");
//            details.setText("Details: 3% transfer discount");
//            details.setText("Details: Third league");

        }
        else if (CustomerAccount.spent<5000) {
            membership.setText("Platinum tire");
//            details.setText("Details: 4% transfer discount");
//            details.setText("Details: Fourth league");

        }
        else {
            membership.setText("Diamond tier");
//            details.setText("Details: 5% transfer discount(max)");
//            details.setText("Details: Final league");

        }


        TransferCoinsStore transfcoins = new TransferCoinsStore();
        getParentFragmentManager().beginTransaction().replace(R.id.transfercoins, transfcoins).commit();
        return view;
    }

//    void TransferReferrals()
//    {
//        for(int i = CustomerAccount.referrals.size() - 1; i >= 0; i--)
//        {
//            Referral ref = CustomerAccount.referrals.get(i);
//            if(ref.reciever == Double.parseDouble(CustomerAccount.phone) && ref.claimed == 1)
//            {
//                ClaimReferral(ref.id,StoreAccount.referralRecAmt, ref.claimed + 1);
//                CustomerAccount.referrals.remove(i);
//            }
//            else if(ref.sender == Double.parseDouble(CustomerAccount.phone) && ref.claimed == 0)
//            {
//                ClaimReferral(ref.id,StoreAccount.referralSenderAmt, ref.claimed + 1);
//                CustomerAccount.referrals.remove(i);
//            }
//        }
//    }

//    private void ClaimReferral(int id, float amount, int claimed) {
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "use-referral");
//        fields.put("session",  Account.session);
//        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
//        fields.put("id",  String.valueOf(id));
//        fields.put("claimed",  String.valueOf(claimed));
//        fields.put("amount",  String.valueOf(amount));
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("claimref response", _response);
//                if(!_response.equals("nosession") && !_response.isEmpty())
//                {
//                    CustomerAccount.dollars += amount;
//                    CustomerAccount.referraltotal -= amount;
//                    Balance frag = new Balance();
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