package com.infoempire.wavetoget;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("api4/index.php")
    Call<String> createPost(@FieldMap Map<String, String> parameters);
    @FormUrlEncoded
    @POST("lib/form-storeselect.php")
    Call<String> storeSelect(@FieldMap Map<String, String> parameters);
    @FormUrlEncoded
    @POST("sendreset.php")
    Call<String> sendReset(@FieldMap Map<String, String> parameters);
    @FormUrlEncoded
    @POST("api4/sms.php")
    Call<String> sendSMS(@FieldMap Map<String, String> parameters);
    @FormUrlEncoded
    @POST("api4/banners/index.php")
    Call<String> advertisementAPI(@FieldMap Map<String, String> parameters);
}