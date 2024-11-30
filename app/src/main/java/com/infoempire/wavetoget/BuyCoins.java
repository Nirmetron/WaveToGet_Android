package com.infoempire.wavetoget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyCoins#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyCoins extends Fragment {
    View view;
//    TextView pagetext;
    Button cancel;
    Button buy50;
    Button buy150;
    Button buy325;
    Button buy700;
//    Button buy500;
//    Button buy1000;
    List<Button> buttons = new ArrayList<Button>(6);
    EditText amount;
    Group pageLayout;
    FrameLayout buycoinsview;
    TextView errormsg;
    Integer coinsToAdd;
    Integer coinsAmount;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  BillingClient billingClient;
    public BuyCoins() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Convert.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyCoins newInstance(String param1, String param2) {
        BuyCoins fragment = new BuyCoins();
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
        view = inflater.inflate(R.layout.fragment_buycoins, container, false);
        cancel = view.findViewById(R.id.canceltransfer);
        errormsg = view.findViewById(R.id.errormsg);
        buy50 = view.findViewById(R.id.buypts50);
        buy150 = view.findViewById(R.id.buypts150);
        buy325 = view.findViewById(R.id.buypts325);
        buy700 = view.findViewById(R.id.buypts700);
//        buy500 = view.findViewById(R.id.buypts500);
//        buy1000 = view.findViewById(R.id.buypts1000);
        buttons.add(0, buy150);
        buttons.add(1, buy325);
        buttons.add(2, buy50);
        buttons.add(3, buy700);
//        buttons.add(2, buy1000);
//        buttons.add(5, buy500);
//        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
//            @Override
//            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
//
//            }
//        };
        PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null){
                    for(Purchase purchase: purchases){
                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()){
                            Log.d("JSOn", purchase.getOriginalJson());
                            Log.d("Package Name", purchase.getPackageName());
                            Log.d("products", purchase.getProducts().toString());
                            ConsumeParams consumeParams =
                                    ConsumeParams.newBuilder()
                                            .setPurchaseToken(purchase.getPurchaseToken())
                                            .build();
                            switch (purchase.getProducts().toString()){
                                case ("[buycoins150]"):
                                    coinsAmount = 150;
                                    break;
                                case ("[buycoins50]"):
                                    coinsAmount = 50;
                                    break;
                                case ("[buycoins325]"):
                                    coinsAmount = 325;
                                    break;
                                case ("[buycoins700]"):
                                    coinsAmount = 700;
                                    break;
                            }
                            ConsumeResponseListener listener = new ConsumeResponseListener() {
                                @Override
                                public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                        Map<String, String> addCoins = new HashMap<>();
                                        addCoins.put("action", "add-coins");
                                        addCoins.put("key", "KEY REMOVED");
                                        addCoins.put("session",  Account.session);
                                        if (Account.isCust){
                                            addCoins.put("user",  String.valueOf(CustomerAccount.user));
                                        }
                                        else {
                                            addCoins.put("user", String.valueOf(StoreAccount.id));
                                        }
                                        addCoins.put("coins",  String.valueOf(coinsAmount));
                                        Call<String> addCall = MainActivity.apiService.createPost(addCoins);
                                        addCall.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> addCall, Response<String> response) {
                                                String _response = response.body();
                                                Log.d("buy-add response", _response);
                                                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                                                {
                                                    if (Account.isCust){
                                                        CustomerAccount.coins = Integer.valueOf(_response);
                                                    }
                                                    else {
                                                        StoreAccount.coins = Integer.valueOf(_response);
                                                    }
                                                    if (getActivity() != null) {
                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                                        if (Account.isCust){
                                                            builder1.setMessage("Added " + (Integer.valueOf(_response) - CustomerAccount.coins) + " coins to your account");
                                                        }
                                                        else {
                                                            builder1.setMessage("Added " + (Integer.valueOf(_response) - StoreAccount.coins) + " coins to your account");
                                                        }
                                                        builder1.setCancelable(true);

                                                        builder1.setPositiveButton(
                                                                "Okay",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                        AlertDialog alert11 = builder1.create();
                                                        alert11.show();
                                                    }

                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<String> addCall, Throwable t) {

                                            }
                                        });
                                    }
                                }
                            };
                            billingClient.consumeAsync(consumeParams, listener);
//                            AcknowledgePurchaseParams acknowledgePurchaseParams =
//                                    AcknowledgePurchaseParams.newBuilder()
//                                            .setPurchaseToken(purchase.getPurchaseToken())
//                                            .build();
//                            billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                        }
                    }
                }
            }
        };
        //check for unused consumable purchases
        if (getContext() != null) {
            billingClient = BillingClient.newBuilder(getContext())
                    .setListener(purchasesUpdatedListener)
                    .enablePendingPurchases()
                    .build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                        Log.d("Connection success", String.valueOf(billingResult));
                        billingClient.queryPurchasesAsync(
                                QueryPurchasesParams.newBuilder()
                                        .setProductType(BillingClient.ProductType.INAPP)
                                        .build(),
                                new PurchasesResponseListener() {
                                    @Override
                                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                                        for (Purchase purchase: list){
                                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED){
                                                Log.d("JSOn", purchase.getOriginalJson());
                                                Log.d("Package Name", purchase.getPackageName());
                                                Log.d("products", purchase.getProducts().toString());
                                                ConsumeParams consumeParams =
                                                        ConsumeParams.newBuilder()
                                                                .setPurchaseToken(purchase.getPurchaseToken())
                                                                .build();
                                                ConsumeResponseListener listener = new ConsumeResponseListener() {
                                                    @Override
                                                    public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                                                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                                            switch (purchase.getProducts().toString()){
                                                                case ("[buycoins150]"):
                                                                    coinsAmount = 150;
                                                                    break;
                                                                case ("[buycoins50]"):
                                                                    coinsAmount = 50;
                                                                    break;
                                                                case ("[buycoins325]"):
                                                                    coinsAmount = 325;
                                                                    break;
                                                                case ("[buycoins700]"):
                                                                    coinsAmount = 700;
                                                                    break;
                                                            }
                                                            Map<String, String> addCoins = new HashMap<>();
                                                            addCoins.put("action", "add-coins");
                                                            addCoins.put("key", "KEY REMOVED");
                                                            addCoins.put("session",  Account.session);
                                                            if (Account.isCust){
                                                                addCoins.put("user",  String.valueOf(CustomerAccount.user));
                                                            }
                                                            else {
                                                                addCoins.put("user", String.valueOf(StoreAccount.id));
                                                            }
                                                            addCoins.put("coins",  String.valueOf(coinsAmount));
                                                            Call<String> addCall = MainActivity.apiService.createPost(addCoins);
                                                            addCall.enqueue(new Callback<String>() {
                                                                @Override
                                                                public void onResponse(Call<String> addCall, Response<String> response) {
                                                                    String _response = response.body();
                                                                    Log.d("buy-add response", _response);
                                                                    if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
                                                                    {
                                                                        if (getActivity() != null) {
                                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                                                            builder1.setMessage("Added " + (Integer.valueOf(_response) - CustomerAccount.coins) + " coins to your account");
                                                                            builder1.setCancelable(true);

                                                                            builder1.setPositiveButton(
                                                                                    "Okay",
                                                                                    new DialogInterface.OnClickListener() {
                                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                                            dialog.cancel();
                                                                                        }
                                                                                    });

                                                                            AlertDialog alert11 = builder1.create();
                                                                            alert11.show();
                                                                        }
                                                                        if (Account.isCust){
                                                                            CustomerAccount.coins = Integer.valueOf(_response);
                                                                        }
                                                                        else {
                                                                            StoreAccount.coins = Integer.valueOf(_response);
                                                                        }

                                                                    }
                                                                }
                                                                @Override
                                                                public void onFailure(Call<String> addCall, Throwable t) {

                                                                }
                                                            });
                                                            Log.d("Consume prev", String.valueOf(billingResult));
                                                        }
                                                    }
                                                };
                                                billingClient.consumeAsync(consumeParams, listener);
                                            }
                                        }
                                    }
                                }
                        );
                        getProducts();
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                    Log.d("connection", "failure");
                    errormsg.setText("Unable to connect to Google Play services, please retry");
                }
            });
        }
        else {
            errormsg.setText("Something is wrong please try again");
        }
        if(!Account.isCust) {
            pageLayout = getActivity().findViewById(R.id.pageGroup);
            buycoinsview = getActivity().findViewById(R.id.buycoinsview);
        }


        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Cancel();
            }
        });
        return view;
    }
    private void getProducts(){
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("buycoins50")
                                                .setProductType(BillingClient.ProductType.INAPP)
                                                .build(),
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("buycoins150")
                                                .setProductType(BillingClient.ProductType.INAPP)
                                                .build(),
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("buycoins325")
                                                .setProductType(BillingClient.ProductType.INAPP)
                                                .build(),
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("buycoins700")
                                                .setProductType(BillingClient.ProductType.INAPP)
                                                .build()
                                ))
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                new ProductDetailsResponseListener() {
                    @Override
                    public void onProductDetailsResponse(BillingResult billingResult,
                                                         @Nullable List<ProductDetails> productDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && productDetailsList !=null){
                            Log.d("details list", productDetailsList.toString());
                            for (int i = 0; i<productDetailsList.size(); i++){
                                ImmutableList productDetailsParamsList =
                                        ImmutableList.of(
                                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                                        // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                                        .setProductDetails(productDetailsList.get(i))
                                                        .build()
                                        );
                                Integer container = i;
                                buttons.get(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        coinsToAdd = container;
                                        billingClient.launchBillingFlow(getActivity(), BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList).build());
                                    }
                                });
                            }
                        }
                    }
                }
        );
    }
//    private void buyProduct(Integer id){
//        ImmutableList productDetailsParamsList =
//                ImmutableList.of(
//                        BillingFlowParams.ProductDetailsParams.newBuilder()
//                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
//                                .setProductDetails(productDetails)
//                                // to get an offer token, call ProductDetails.getSubscriptionOfferDetails()
//                                // for a list of offers that are available to the user
//                                .setOfferToken(selectedOfferToken)
//                                .build()
//                );
//
//        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
//                .setProductDetailsParamsList(productDetailsParamsList)
//                .build();
//
//// Launch the billing flow
//        BillingResult billingResult = billingClient.launchBillingFlow(activity, billingFlowParams);
//    }
    void Cancel()
    {
        if(!Account.isCust) {
            buycoinsview.setVisibility(View.GONE);
            pageLayout.setVisibility(View.VISIBLE);
            getParentFragmentManager().beginTransaction().remove(this).commit();
        }
        else
        {
            customerpage1 frag = new customerpage1();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
        }
    }

}