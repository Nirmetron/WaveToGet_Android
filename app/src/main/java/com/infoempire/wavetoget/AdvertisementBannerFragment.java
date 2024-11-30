package com.infoempire.wavetoget;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
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
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdvertisementBannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdvertisementBannerFragment extends Fragment {

    private static final String TAG = "AdvertisementBannerFrag";

    View view;
    EditText etBannerComment;
    EditText etBannerReferral;
    Button btnBannerStop;
    Button btnBannerRemove;
    Button btnBannerSubscribe;
    Button btnCheckReferral;
    ImageView back;
    ImageView ivBannerPlaceholder;
    TextView referralError;
    TextView errormsg;

    String path;
    Uri uri;
    String loadedBannerID = "";
    String loadedBannerURL = "";
    String encodedImage = "";
    Integer maxTries = 3;
    Integer tryCount = 0;
    Boolean isConnected = false;
    private  BillingClient billingClient = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdvertisementBannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdvertisementBannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdvertisementBannerFragment newInstance(String param1, String param2) {
        AdvertisementBannerFragment fragment = new AdvertisementBannerFragment();
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
        view = inflater.inflate(R.layout.fragment_advertisement_banner, container, false);

        back = view.findViewById(R.id.backbutton4);
        etBannerComment = view.findViewById(R.id.et_banner_comment);
        etBannerReferral = view.findViewById(R.id.et_banner_referral);
        btnBannerStop = view.findViewById(R.id.btn_banner_stop);
        btnBannerRemove = view.findViewById(R.id.btn_banner_remove);
        btnBannerSubscribe = view.findViewById(R.id.btn_banner_subscribe);
        btnCheckReferral = view.findViewById(R.id.btn_check_referral);
        referralError = view.findViewById(R.id.referralerror);
        ivBannerPlaceholder = view.findViewById(R.id.iv_banner_placeholder);
        errormsg = view.findViewById(R.id.errormsg);
        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    uploadAdvertisement();
                }
                {
                    Log.d("billingResult", String.valueOf(billingResult.getResponseCode()));
                    Log.d("debugMessage", billingResult.getDebugMessage());
                    errormsg.setText("Something went wrong while acknowledging your purchase, please contact the developers");
                }
            }
        };
        PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null){
                    for(Purchase purchase: purchases){
                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()){
                            Log.d("JSOn", purchase.getOriginalJson());
                            Log.d("Package Name", purchase.getPackageName());
                            Log.d("products", purchase.getProducts().toString());
                            AcknowledgePurchaseParams acknowledgePurchaseParams =
                                    AcknowledgePurchaseParams.newBuilder()
                                            .setPurchaseToken(purchase.getPurchaseToken())
                                            .build();
                            billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                        }
                        Log.d("billingResult pur", String.valueOf(billingResult.getResponseCode()));
                        Log.d("debugMessage pur", billingResult.getDebugMessage());
                        Log.d("purchases", purchases.toString());
                        errormsg.setText("Something went wrong while purchasing the subscription");
                    }
                }
                else {
                    Log.d("billingResult pur", String.valueOf(billingResult.getResponseCode()));
                    Log.d("debugMessage pur", billingResult.getDebugMessage());
                    errormsg.setText("Something went wrong while purchasing the subscription");
                }
            }
        };
        if (getActivity() != null) {
            billingClient = BillingClient.newBuilder(getActivity())
                    .setListener(purchasesUpdatedListener)
                    .enablePendingPurchases()
                    .build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                        Log.d("Connection success", String.valueOf(billingResult));
                        errormsg.setText("");
                        CheckSub();
//                        getProducts();
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                    Log.d("connection", "failure");
                    errormsg.setText("Unable to connect to Google Play services, trying to reconnect");
                    retryBillingServiceConnection();
                }
            });
        }
        else {
            errormsg.setText("Something is wrong trying to connect to Google, try again");
        }
        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode() == RESULT_OK){
                        Uri uri=result.getData().getData();
                        // Use the uri to load the image
                        Log.d(TAG, "onClick: Image from ImagePicker URI = " + uri.toString());
                        ivBannerPlaceholder.setImageURI(uri);

                        // Encode image to base64
                        final InputStream imageStream;
                        try {
                            imageStream = getContext().getContentResolver().openInputStream(uri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            encodedImage = "data:image/jpeg;base64," + encodeImage(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                        Log.d(TAG, "onClick: Error from ImagePicker!!");
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreSettingsPage1 frag = new StoreSettingsPage1();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });

        btnCheckReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findCardholder();
            }
        });

        btnBannerStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAdvertisement();
            }
        });

        btnBannerRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAdvertisement();
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });

        ivBannerPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(
                        ImagePicker.with(getActivity())
                                .galleryOnly()
                                .crop(2f, 1f)
                                .setMultipleAllowed(false)
                                .createIntent()
                );

            }
        });

        btnBannerSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billingClient != null){
                    CheckSub();
                }
            }
        });

        return view;
    }
    private void CheckSub(){
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                new PurchasesResponseListener() {
                    @Override
                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, List<Purchase> list) {
                        if(list == null || list.isEmpty()){
                            Log.d("list is null", billingResult.getDebugMessage() + "  " + billingResult.getResponseCode());
//                            deleteAdvertisement();
                            buySub();
                        }
                        else {
                            for (Purchase purchase: list){
                                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && purchase.isAcknowledged()){
                                    Log.d("sub JSOn", purchase.getOriginalJson());
                                    Log.d("sub Package Name", purchase.getPackageName());
                                    Log.d("sub products", purchase.getProducts().toString());
                                    uploadAdvertisement();
                                }
                                else {
                                    Log.d("something else 1", billingResult.getDebugMessage() + "  " + billingResult.getResponseCode());
                                    Log.d("something else 2", String.valueOf(purchase.getPurchaseState()));
                                    deleteAdvertisement();
                                    buySub();
                                }
                            }
                        }
                        }
                    });
    }

    private void buySub(){
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("advertsub")
                                                .setProductType(BillingClient.ProductType.SUBS)
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
                            for (ProductDetails productDetails: productDetailsList){
                                Log.d("debug", billingResult.getDebugMessage());
                                ImmutableList productDetailsParamsList =
                                        ImmutableList.of(
                                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                                        // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                                        .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
                                                        .setProductDetails(productDetails)
                                                        .build()
                                        );
                                        billingClient.launchBillingFlow(getActivity(), BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList).build());
                            }
                        }
                    }
                }
        );
    }
    private void findCardholder() {
        referralError.setText("");

        Map<String, String> fields = new HashMap<>();
        fields.put("action", " find-cardholder");
        fields.put("session",  Account.session);
        fields.put("phone", etBannerReferral.getText().toString());
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("find cardholder", response.body());
                if(plain_text_response != null && !plain_text_response.equals("false") &&!plain_text_response.equals("[]") && !plain_text_response.equals("nosession") && !plain_text_response.isEmpty())
                {
                    referralError.setText("Referral account found!");
                }
                else
                {
                    referralError.setText("Referral account not found...");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                referralError.setText("Referral account not found...");
            }
        });
    }

    private void fetchAdvertisement() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get");
        fields.put("id", String.valueOf(StoreAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.advertisementAPI(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("fetchAdvertisement", response.body());
                if(plain_text_response != null && !plain_text_response.equals("false") &&!plain_text_response.equals("[]") && !plain_text_response.equals("nosession") && !plain_text_response.isEmpty())
                {
                    try {
                        JSONObject jObject = new JSONObject(plain_text_response);
                        String imageURL = jObject.optString("image");
                        String comment = jObject.optString("text");
                        String bannerID = jObject.optString("id");
                        String referralPhone = jObject.optString("referral_phone");

                        etBannerComment.setText(comment);
                        etBannerReferral.setText(referralPhone);
                        loadedBannerID = bannerID;
                        loadedBannerURL = imageURL;

                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                        if (SDK_INT > 8)
                        {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            // Load image from url
                            URL url = new URL(imageURL);
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            ivBannerPlaceholder.setImageBitmap(bmp);
                        }



                    } catch (JSONException | MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void uploadAdvertisement() {

        if (etBannerComment.getText().toString().isEmpty() || etBannerReferral.getText().toString().isEmpty()) {
            errormsg.setText("Please fill all the fields");
            return;
        }
        errormsg.setText("");
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "set");
        fields.put("store", String.valueOf(StoreAccount.id));
        fields.put("text", etBannerComment.getText().toString());
        fields.put("phone", etBannerReferral.getText().toString());
        fields.put("id", loadedBannerID);
        fields.put("image", encodedImage);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.advertisementAPI(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("fetchAdvertisement", response.body());
                if(plain_text_response != null && !plain_text_response.equals("false") &&!plain_text_response.equals("[]") && !plain_text_response.equals("nosession") && !plain_text_response.isEmpty())
                {
                    try {
                        JSONObject jObject = new JSONObject(plain_text_response);
                        String message = jObject.optString("message");
                        Boolean status = jObject.optBoolean("status");

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage(message);
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
                        return;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Something went wrong! Please try again later.");
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
                    return;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void stopAdvertisement() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("You can cancel your subscription on Google Play Store to stop the advertisement!");
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
        return;
    }

    private void deleteAdvertisement() {
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "delete");
        fields.put("store", String.valueOf(StoreAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.advertisementAPI(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String plain_text_response = response.body();
                Log.d("deleteAdvertisement", response.body());
                if(plain_text_response != null && !plain_text_response.equals("false") &&!plain_text_response.equals("[]") && !plain_text_response.equals("nosession") && !plain_text_response.isEmpty())
                {
                    try {
                        JSONObject jObject = new JSONObject(plain_text_response);
                        String message = jObject.optString("message");
                        Boolean status = jObject.optBoolean("status");

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage(message);
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
                        return;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Something went wrong! Please try again later.");
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
                    return;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @Override
    public void onResume() {
        super.onResume();

        fetchAdvertisement();
    }
    private void retryBillingServiceConnection(){

        while (tryCount < maxTries && !isConnected) try {
            tryCount++;
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingServiceDisconnected() {
                    errormsg.setText("Unable to connect to Google Play services, trying to reconnect " + tryCount);
                }

                @Override
                public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                    isConnected = true;
                    Log.d("connected", billingClient.toString());
                    CheckSub();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Log.d("unable to connect", billingClient.toString());
        errormsg.setText("Unable to connect, try again later");
    }
}