package com.refDelegateFamily.services;


import com.refDelegateFamily.models.NearbyStoreDataModel;
import com.refDelegateFamily.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {


    @GET("place/details/json")
    Call<NearbyStoreDataModel> getPlaceReview(@Query(value = "placeid") String placeid,
                                       @Query(value = "key") String key
    );

    @GET("place/nearbysearch/json")
    Call<NearbyStoreDataModel> getNearbyStores(@Query(value = "location") String location,
                                               @Query(value = "radius") int radius,
                                               @Query(value = "type") String type,
                                               @Query(value = "language") String language,
                                               @Query(value = "key") String key
    );


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone

    );


    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUpWithoutImage(@Field("name") String name,
                                       @Field("email") String email,
                                       @Field("phone_code") String phone_code,
                                       @Field("phone") String phone,
                                       @Field("address") String address,
                                       @Field("user_type") String user_type,
                                       @Field("software_type") String software_type,
                                       @Field("account_bank_number") String account_bank_number,
                                       @Field("ipad_number") String ipad_number,
                                       @Field("category_id[]") List<String> category_id


    );

    @Multipart
    @POST("api/register")
    Call<UserModel> signUpWithImage(@Part("name") RequestBody name,
                                    @Part("email") RequestBody email,
                                    @Part("phone_code") RequestBody phone_code,
                                    @Part("phone") RequestBody phone,
                                    @Part("address") RequestBody address,
                                    @Part MultipartBody.Part logo,
                                    @Part("user_type") RequestBody user_type,
                                    @Part("software_type") RequestBody software_type,
                                    @Part("account_bank_number") RequestBody account_bank_number,
                                    @Part("ipad_number") RequestBody ipad_number,
                                    @Part("category_id[]") List<RequestBody> category_id
    );


}