package com.infoempire.wavetoget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerHomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerHomePage extends Fragment {
    View view;
    ImageView qrImage;
    //    TextView dollars;
//    TextView points;
    TextView coins;
    TextView errmsg;
    //    View starsholder;
    Button buybut;
//    Button paybut;
    Button paysendbut;
//    ImageView payqrbut;
//    ImageView sendqrbut;
    ImageView scanoffer;
    Button getoffercodebut;
    EditText offercodetext;
    Boolean isPayToStore;
    Button qrbtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerHomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerHomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerHomePage newInstance(String param1, String param2) {
        CustomerHomePage fragment = new CustomerHomePage();
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
        view = inflater.inflate(R.layout.fragment_customer_home_page, container, false);
        // Inflate the layout for this fragment
        qrImage = view.findViewById(R.id.qr2);
        coins = view.findViewById(R.id.coins);
//        starsholder = view.findViewById(R.id.starsholder);
//        payqrbut = view.findViewById(R.id.qrpay);
//        paybut = view.findViewById(R.id.paybtn);
        paysendbut = view.findViewById(R.id.paysendbtn);
//        sendqrbut = view.findViewById(R.id.qrsend);
        buybut = view.findViewById(R.id.buycoins);
        scanoffer = view.findViewById(R.id.scanoffer);
        getoffercodebut = view.findViewById(R.id.getoffer);
        offercodetext = view.findViewById(R.id.offercodetext);
        errmsg = view.findViewById(R.id.errmsg);

        scanoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        getActivity()
                );
                Account.scanningItem = false;
                intentIntegrator.setPrompt("Scan QR code");
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }
        });
        getoffercodebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedeemPerk(offercodetext.getText().toString());
                offercodetext.setText("");
            }
        });
        buybut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyCoins frag = new BuyCoins();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        paysendbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TransferCoinsClient frag = new TransferCoinsClient(null, false);
                PayAndSend frag = new PayAndSend();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
//        paybut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TransferCoinsClient frag = new TransferCoinsClient(null, true);
//                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//            }
//        });
        coins.setText(String.valueOf(CustomerAccount.coins));

//        MultiFormatWriter writer = new MultiFormatWriter();
        try {
//            BitMatrix matrix = writer.encode(CustomerAccount.phone, BarcodeFormat.QR_CODE, 350, 350);
            Map<EncodeHintType, Object> hintMap = new HashMap<EncodeHintType, Object>();
            hintMap.put(EncodeHintType.MARGIN, new Integer(1));
            BitMatrix matrix = new MultiFormatWriter().encode(
                    CustomerAccount.phone,
                    BarcodeFormat.QR_CODE, 350, 350, hintMap);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrImage.setImageBitmap(bitmap);


        } catch (WriterException e) {
            e.printStackTrace();
        }
//        payqrbut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentIntegrator intentIntegrator = new IntentIntegrator(
//                        getActivity()
//                );
//                isPayToStore = true;
//                Account.scanningItem = true;
//                intentIntegrator.setPrompt("Scan QR code");
//                intentIntegrator.setBeepEnabled(false);
//                intentIntegrator.setOrientationLocked(true);
//                intentIntegrator.setCaptureActivity(Capture.class);
//                intentIntegrator.initiateScan();
//            }
//        });
//        sendqrbut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentIntegrator intentIntegrator = new IntentIntegrator(
//                        getActivity()
//                );
//                isPayToStore = false;
//                Account.scanningItem = true;
//                intentIntegrator.setPrompt("Scan QR code");
//                intentIntegrator.setBeepEnabled(false);
//                intentIntegrator.setOrientationLocked(true);
//                intentIntegrator.setCaptureActivity(Capture.class);
//                intentIntegrator.initiateScan();
//            }
//        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        Log.d("isPay", String.valueOf(isPayToStore));
        if(intentResult.getContents() != null)
        {
                Log.d("scan", intentResult.getContents());
                TransferCoinsClient frag = new TransferCoinsClient(intentResult.getContents(), isPayToStore);
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
        }
    }

    private void RedeemPerk(String perk) {
        Log.d("test redeem", "test redeem");
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "redeem-offercode");
        fields.put("session", Account.session);
        fields.put("code", perk);
        fields.put("cardholder", String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("REDEEM CODE", _response);
                if (!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]") && !_response.isEmpty() && !_response.equals("false")) {
                    try {
                        JSONObject jObject = new JSONObject(_response);
                        Integer amount = (jObject.optInt("amount"));
                        String type = jObject.optString("type");

//                        if(type.equals("points"))
//                        {
//                            CustomerAccount.points += Math.round(amount);
//                        }
//                        else
//                        {
//                            CustomerAccount.dollars += amount;
//                        }

                        customerpage1 frag = new customerpage1();
                        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                        errmsg.setText("Redeemed " + amount + " " + type + "with code \"" + perk + "\"!");
                        errmsg.setTextColor(Color.parseColor("#00FF00"));
                        coins.setText(String.valueOf(CustomerAccount.coins) + " coins");
//                        dollars.setText("$" + String.format("%.2f", CustomerAccount.dollars));
//                        points.setText(String.valueOf(CustomerAccount.points) + " pts");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    errmsg.setText("Failed to redeem code...");
                    errmsg.setTextColor(Color.parseColor("#FF0000"));
                    //view.invalidate();
                    //error message TODO
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}


//    void SetUpStars()
//    {
//        Stampsheet storeStampSheet = StoreAccount.stampsheets.get(0);
//
//        List<Stamp> customerStamps = new ArrayList<>();
//
//        for (int i = 0; i < CustomerAccount.stamps.size(); i++) {
//            if(CustomerAccount.stamps.get(i).stampsheet == storeStampSheet.id)
//            {
//                customerStamps.add(CustomerAccount.stamps.get(i));
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            if (i < customerStamps.size()) {
//                ImageView star = starsholder.findViewWithTag("star" + i);
//                star.setColorFilter(getResources().getColor(R.color.myDarkRed));
//                Log.d("star number", String.valueOf(i));
//            }
//        }
//    }


