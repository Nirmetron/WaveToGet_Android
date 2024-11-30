package com.infoempire.wavetoget;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        GetCustomerAccount();
    }
    private void GetStoreAccount() {

        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-store");
        fields.put("session",  Account.session);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("STORE ACCOUNT", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                {
                    try {
                        JSONObject jObject = new JSONObject(_response);
                        StoreAccount.id = jObject.optInt("id");
                        StoreAccount.name = jObject.optString("name");
                        StoreAccount.address = jObject.optString("address");
                        StoreAccount.city = jObject.optString("city");
                        StoreAccount.cardname = jObject.optString("cardname");
                        StoreAccount.province = jObject.optInt("province");
                        StoreAccount.country = jObject.optInt("country");
                        StoreAccount.postalcode = jObject.optString("postalcode");
                        StoreAccount.phone = jObject.optString("phone");
                        StoreAccount.googleReviewURL = jObject.optString("GoogleReviewURL");
//                        StoreAccount.point_expand = jObject.optString("point_expand");
//                        StoreAccount.point_value = jObject.optString("point_value");
//                        JSONArray jArray = jObject.optJSONArray("stampsheets");

//                        StoreAccount.stampsheets = new ArrayList<Stampsheet>();
//                        for (int i=0; i < jArray.length(); i++)
//                        {
//                            try {
//                                Stampsheet stampsheet = new Stampsheet();
//                                JSONObject oneObject = jArray.getJSONObject(i);
//                                // Pulling items from the array
//                                stampsheet.id = oneObject.optInt("id");
//                                stampsheet.prize = oneObject.optString("prize");
//                                stampsheet.size = oneObject.optInt("size");
//                                StoreAccount.stampsheets.add(stampsheet);
//                            } catch (JSONException e) {
//                                // Oops
//                            }
//                        }
                        JSONArray jArrayplans = jObject.optJSONArray("plans");
                        StoreAccount.plans = new ArrayList<Plan>();
                        for (int i=0; i < jArrayplans.length(); i++)
                        {
                            try {
                                Plan plan = new Plan();
                                JSONObject oneObject = jArrayplans.getJSONObject(i);
                                // Pulling items from the array
                                plan.id = oneObject.optInt("id");
                                plan.store = oneObject.optInt("store");
                                plan.name = oneObject.optString("name");
                                plan.term_months = oneObject.optInt("term_months");
                                plan.details = oneObject.optString("details");

                                StoreAccount.plans.add(plan);
                            } catch (JSONException e) {
                                // Oops
                            }
                        }
                        customerpage1 frag = new customerpage1();
                        getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//                        GetStoreRedeemables();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        if(intentResult.getContents() != null)
        {
            Log.d("scan", intentResult.getContents());
            Account.qrstring = intentResult.getContents();
            if(Account.scanningItem) {
                GetStoreitem(intentResult.getContents());
            }
            else
            {
                RedeemPerk(intentResult.getContents());
            }
        }
    }

    private void GetStoreitem(String itemid) {

        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-storeitem");
        fields.put("session",  Account.session);
        fields.put("id",  itemid);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("STORE ITEM", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty())
                {
                    try {
                        JSONObject jObject = new JSONObject(_response);
                        Account.qrstring = "";
                        Account.qrstring += jObject.optString("code") + '`';
                        Account.qrstring += jObject.optString("amount") + '`';
                        Account.qrstring += jObject.optString("type") + '`';
                        Account.qrstring += String.valueOf(jObject.optInt("id")) + '`';
                        Account.qrstring += String.valueOf(jObject.optInt("quantity"));
                        payqr frag = new payqr();
                        getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    TextView errmsg = findViewById(R.id.errmsg);
                    errmsg.setText("No item found...");
                    errmsg.setTextColor(Color.parseColor("#FF0000"));
                    //error message TODO
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void GetSpendings(){
        Map<String, String> userSpent = new HashMap<>();
        userSpent.put("action", "get-spent");
        userSpent.put("key", "KEY REMOVED");
        userSpent.put("session",  Account.session);
        userSpent.put("user",  String.valueOf(CustomerAccount.user));
        Call<String> getCall = MainActivity.apiService.createPost(userSpent);
        getCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> getCall, Response<String> response) {
                String _response = response.body();
                Log.d("get-spent response", _response);
//                                Log.d("get-coins response 2", _response);
                if (_response.isEmpty() || _response==""){
                    CustomerAccount.spent = 0;
                }
                else {
                    CustomerAccount.spent = Integer.valueOf(_response);
                }
            }

            @Override
            public void onFailure(Call<String> getCall, Throwable t) {
                CustomerAccount.spent = 0;
            }
        });
    }

    private void RedeemPerk(String perk) {
        Log.d("test redeem", "test redeem");
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "redeem-offercode");
//        fields.put("session",  Account.session);
//        fields.put("code",  perk);
//        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("REDEEM CODE", _response);
//                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.equals("[]")&& !_response.isEmpty() && !_response.equals("false"))
//                {
//                    try {
//                        JSONObject jObject = new JSONObject(_response);
//                        float amount = Float.parseFloat(jObject.optString("amount"));
//                        String type = jObject.optString("type");
//
//                        if(type.equals("points"))
//                        {
//                            CustomerAccount.points += Math.round(amount);
//                        }
//                        else
//                        {
//                            CustomerAccount.dollars += amount;
//                        }
//                        TextView dollars = findViewById(R.id.dollars);
//                        TextView points = findViewById(R.id.clientcoins);
//                        TextView errmsg = findViewById(R.id.errmsg);
//
//                        errmsg.setText("Redeemed " + amount + " " + type + "with code \"" + perk +"\"!");
//                        errmsg.setTextColor(Color.parseColor("#00FF00"));
//                        dollars.setText("$" + String.format("%.2f", CustomerAccount.dollars));
//                        points.setText(String.valueOf(CustomerAccount.points) + " pts");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else
//                {
//                    TextView errmsg = findViewById(R.id.errmsg);
//                    errmsg.setText("Failed to redeem code...");
//                    errmsg.setTextColor(Color.parseColor("#FF0000"));
//                    //error message TODO
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
    }


//    private void GetStoreRedeemables() {
//
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "get-store-redeemables");
//        fields.put("session",  Account.session);
//        fields.put("store",  String.valueOf(StoreAccount.id));
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("STORE REDEEMABLES", _response);
//                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
//                {
//                    try {
//                        JSONArray jArray = new JSONArray(_response);
//                        StoreAccount.redeemables = new ArrayList<Redeemable>();
//                        for (int i=0; i < jArray.length(); i++)
//                        {
//                            try {
//                                Redeemable redeemable = new Redeemable();
//                                Log.d("rek", String.valueOf(StoreAccount.redeemables.size()));
//                                JSONObject oneObject = jArray.getJSONObject(i);
//                                redeemable.id = oneObject.getInt("id");
//
//                                redeemable.name = oneObject.getString("name");
//                                Log.d("redeemable name", redeemable.name);
//                                redeemable.points = oneObject.getInt("points");
//
//                                StoreAccount.redeemables.add(redeemable);
//
//
//                            } catch (JSONException e) {
//                                // Oops
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                GetStoreReferral();
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }

    private void GetCustomerPlans() {

        Map<String, String> fields = new HashMap<>();
        Log.d("test", String.valueOf(CustomerAccount.id));
        fields.put("action", "get-cardholder-plan");
        fields.put("session",  Account.session);
        fields.put("cardholder",  String.valueOf(CustomerAccount.id));
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("CUSTOMER PLANS", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                {
                    try {
                        CustomerAccount.plan = new Plan();
                        JSONObject jObject = new JSONObject(_response);
                        CustomerAccount.plan.name = jObject.optString("name");
                        CustomerAccount.plan.details = jObject.optString("details");
                        CustomerAccount.plan.expdate = jObject.optString("expirydate");
                        CustomerAccount.plan.id = jObject.optInt("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                GetStoreAccount();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

//    private void GetStoreReferral() {
//        StoreAccount.referralRecAmt = 0f;
//        StoreAccount.referralSenderAmt = 0f;
//        StoreAccount.referralName = "";
//        StoreAccount.referralmsg = "";
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "get-referral");
//        fields.put("session",  Account.session);
//        fields.put("store",  String.valueOf(StoreAccount.id));
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("STORE Referrals", _response);
//                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
//                {
//                    try {
//                        JSONArray jArray = new JSONArray(_response);
//                        for (int i=0; i < jArray.length(); i++)
//                        {
//                            try {
//                                JSONObject oneObject = jArray.getJSONObject(i);
//                                StoreAccount.referralRecAmt = Float.parseFloat(oneObject.optString("recieveramount"));
//                                StoreAccount.referralSenderAmt = Float.parseFloat(oneObject.optString("senderamount"));
//                                StoreAccount.referralName = oneObject.getString("name");
//                                StoreAccount.referralmsg = oneObject.getString("message");
//                            } catch (JSONException e) {
//                                // Oops
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                GetCustomersReferral();
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }

//    private void GetCustomersReferral() {
//        CustomerAccount.referraltotal = 0f;
//        Map<String, String> fields = new HashMap<>();
//        fields.put("action", "get-customer-referrals");
//        fields.put("session",  Account.session);
//        fields.put("store",  String.valueOf(StoreAccount.id));
//        fields.put("phone",  CustomerAccount.phone);
//        fields.put("key", "KEY REMOVED");
//        Call<String> call = MainActivity.apiService.createPost(fields);
//        CustomerAccount.referrals = new ArrayList<Referral>();
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String _response = response.body();
//                Log.d("customer Referrals", _response);
//                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
//                {
//                    try {
//                        JSONArray jArray = new JSONArray(_response);
//                        for (int i=0; i < jArray.length(); i++)
//                        {
//                            try {
//                                Referral ref = new Referral();
//                                JSONObject oneObject = jArray.getJSONObject(i);
//                                ref.id = oneObject.getInt("id");
//                                ref.sender = oneObject.getDouble("sender");
//                                ref.reciever = oneObject.getDouble("reciever");
//                                ref.claimed = oneObject.getInt("claimed");
//                                CustomerAccount.referrals.add(ref);
//
//                                if(ref.reciever == Double.parseDouble(CustomerAccount.phone) && ref.claimed == 1)
//                                {
//                                    CustomerAccount.referraltotal += StoreAccount.referralRecAmt;
//                                }
//                                if(ref.sender == Double.parseDouble(CustomerAccount.phone) && ref.claimed == 0)
//                                {
//                                    CustomerAccount.referraltotal += StoreAccount.referralSenderAmt;
//                                }
//                            } catch (JSONException e) {
//                                // Oops
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                customerpage1 frag = new customerpage1();
//                getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//            }
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }

    private void GetCustomerAccount() {

        Map<String, String> fields = new HashMap<>();
        fields.put("action", "get-cardholder");
        fields.put("store",  String.valueOf(Account.store));
        fields.put("val",  String.valueOf(Account.id));
        fields.put("session",  Account.session);
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("CUSTOMER ACCOUNT", _response);
                if(!_response.equals("nosession") && !_response.equals("failed") && !_response.isEmpty())
                {
                    try {
                        JSONObject jObject = new JSONObject(_response);
                        CustomerAccount.id = jObject.optInt("id");
                        CustomerAccount.user = jObject.optInt("user");
                        CustomerAccount.firstname = jObject.optString("firstname");
                        CustomerAccount.lastname = jObject.optString("lastname");
                        CustomerAccount.address = jObject.optString("address");
                        CustomerAccount.city = jObject.optString("city");
                        CustomerAccount.province = jObject.optInt("province");
                        CustomerAccount.postal = jObject.optString("postalcode");
                        CustomerAccount.phone = jObject.optString("phone");
                        CustomerAccount.coins = jObject.optInt("coins");
//                        CustomerAccount.spent = jObject.optLong("spent");
//                        Log.d("CustomerPageSpent", String.valueOf(jObject.optLong("spent")));
                        CustomerAccount.provCode = jObject.optString("provCode");
                        CustomerAccount.provName = jObject.optString("provName");
                        CustomerAccount.email = jObject.optString("email");
                        CustomerAccount.store = jObject.optInt("store");
                        CustomerAccount.pin = jObject.optString("pin");
                        GetSpendings();
//                        JSONArray jArray = jObject.optJSONArray("stampsheets");
//                        CustomerAccount.stampsheets = new ArrayList<Stampsheet>();
//                        if(jArray != null) {
//                            for (int i = 0; i < jArray.length(); i++) {
//                                try {
//
//                                    Stampsheet stampsheet = new Stampsheet();
//                                    JSONObject oneObject = jArray.getJSONObject(i);
//                                    // Pulling items from the array
//                                    stampsheet.id = oneObject.optInt("stampsheet");
//                                    stampsheet.updated = oneObject.optString("updated");
//                                    stampsheet.prize = oneObject.optString("prize");
//                                    stampsheet.stamps = oneObject.optInt("stamps");
//                                    CustomerAccount.stampsheets.add(stampsheet);
//                                } catch (JSONException e) {
//                                    // Oops
//                                }
//                            }
//                        }

//                        JSONArray jArraystamps = jObject.optJSONArray("stamps");
//                        CustomerAccount.stamps = new ArrayList<Stamp>();
//                        if(jArraystamps != null) {
//                            for (int i = 0; i < jArraystamps.length(); i++) {
//                                try {
//
//                                    Stamp stamp = new Stamp();
//                                    JSONObject oneObject = jArraystamps.getJSONObject(i);
//                                    // Pulling items from the array
//
//                                    stamp.stampsheet = oneObject.optInt("stampsheet");
//                                    stamp.created = oneObject.optInt("created");
//                                    CustomerAccount.stamps.add(stamp);
//                                } catch (JSONException e) {
//                                    // Oops
//                                }
//                            }
//                        }

//                        JSONArray jArrayBenefits = jObject.optJSONArray("benefits");
//                        CustomerAccount.benefits = new ArrayList<Benefit>();
//                        if(jArrayBenefits != null) {
//                            for (int i = 0; i < jArrayBenefits.length(); i++) {
//                                try {
//
//                                    Benefit benefit = new Benefit();
//                                    JSONObject oneObject = jArrayBenefits.getJSONObject(i);
//                                    // Pulling items from the array
//                                    benefit.id = oneObject.optInt("id");
//                                    benefit.benefit = oneObject.optInt("benefit");
//                                    benefit.quantity = oneObject.optInt("quantity");
//                                    benefit.startdate = oneObject.optString("startdate");
//                                    benefit.expirydate = oneObject.optString("expirydate");
//                                    benefit.description = oneObject.optString("description");
//                                    CustomerAccount.benefits.add(benefit);
//                                } catch (JSONException e) {
//                                    // Oops
//                                }
//                            }
//                        }
                        GetCustomerPlans();

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
}