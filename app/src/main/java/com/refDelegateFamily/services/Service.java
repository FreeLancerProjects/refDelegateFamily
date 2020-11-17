package com.refDelegateFamily.services;


import com.refDelegateFamily.models.FeedbackDataModel;
import com.refDelegateFamily.models.MainCategoryModel;
import com.refDelegateFamily.models.MessageDataModel;
import com.refDelegateFamily.models.MessageModel;
import com.refDelegateFamily.models.NearbyStoreDataModel;
import com.refDelegateFamily.models.NotificationModel;
import com.refDelegateFamily.models.OrderModel;
import com.refDelegateFamily.models.PackageResponse;
import com.refDelegateFamily.models.PlaceGeocodeData;
import com.refDelegateFamily.models.PlaceMapDetailsData;
import com.refDelegateFamily.models.SettingModel;
import com.refDelegateFamily.models.SubscriptionDataModel;
import com.refDelegateFamily.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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


    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone

    );

    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String user_token,
                              @Field("phone_token") String phone_token
    );

    @GET("api/Get-my-notifications")
    Call<NotificationModel> getNotification(@Header("Authorization") String user_token,
                                            @Query("user_id") int user_id);


    @FormUrlEncoded
    @POST("api/drive_register")
    Call<UserModel> signUpWithoutImage(@Field("name") String name,
                                       @Field("email") String email,
                                       @Field("phone_code") String phone_code,
                                       @Field("phone") String phone,
                                       @Field("address") String address,
                                       @Field("user_type") String user_type,
                                       @Field("software_type") String software_type,
                                       @Field("account_bank_number") String account_bank_number,
                                       @Field("ipad_number") String ipad_number,
                                       @Field("nationality_title") String nationality_title,
                                       @Field("car_model") String car_model,
                                       @Field("year_of_manufacture") String year_of_manufacture,
                                       @Field("card_id") String card_id,
                                       @Field("latitude") String latitude,
                                       @Field("longitude") String longitude,
                                       @Field("address_registered_for_bank_account") String address_registered_for_bank_account

    );

    @Multipart
    @POST("api/drive_register")
    Call<UserModel> signUpWithImage(
            @Part MultipartBody.Part logo,
            @Part MultipartBody.Part licence_image,
            @Part MultipartBody.Part back_car_image,
            @Part MultipartBody.Part front_car_image,
            @Part MultipartBody.Part card_image,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phone_code") RequestBody phone_code,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("user_type") RequestBody user_type,
            @Part("software_type") RequestBody software_type,
            @Part("account_bank_number") RequestBody account_bank_number,
            @Part("ipad_number") RequestBody ipad_number,
            @Part("nationality_title") RequestBody nationality_title,
            @Part("car_model") RequestBody car_model,
            @Part("car_type_id") RequestBody car_type_id,
            @Part("year_of_manufacture") RequestBody year_of_manufacture,
            @Part("card_id") RequestBody card_id,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("address_registered_for_bank_account") RequestBody address_registered_for_bank_account
    );


    @Multipart
    @POST("api/update_drive_register")
    Call<UserModel> updateProfileWithImage(
            @Header("Authorization") String user_token,
            @Part("id") RequestBody id,
            @Part MultipartBody.Part logo,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phone_code") RequestBody phone_code,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("user_type") RequestBody user_type,
            @Part("software_type") RequestBody software_type,
            @Part("account_bank_number") RequestBody account_bank_number,
            @Part("ipad_number") RequestBody ipad_number,
            @Part("nationality_title") RequestBody nationality_title,
            @Part("card_id") RequestBody card_id,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("address_registered_for_bank_account") RequestBody address_registered_for_bank_account
    );

    @FormUrlEncoded
    @POST("api/update_drive_register")
    Call<UserModel> updateProfileWithoutImage(
            @Header("Authorization") String user_token,
            @Field("id") int id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone_code") String phone_code,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("user_type") String user_type,
            @Field("software_type") String software_type,
            @Field("account_bank_number") String account_bank_number,
            @Field("ipad_number") String ipad_number,
            @Field("nationality_title") String nationality_title,
            @Field("card_id") String card_id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("address_registered_for_bank_account") String address_registered_for_bank_account

    );

    @FormUrlEncoded
    @POST("api/update-notifications-status")
    Call<ResponseBody> updateStatus(@Header("Authorization") String user_token,
                                    @Field("user_id") int user_id,
                                    @Field("notification_status") String notification_status

    );

    @GET("api/Get-current-or-previous-order-by-Status")
    Call<OrderModel> getOrderByStatus(@Header("Authorization") String user_token,
                                      @Query("user_id") int user_id,
                                      @Query("user_type") String user_type,
                                      @Query("status") String status
    );

    @GET("api/get-driver-new-orders")
    Call<OrderModel> getOrderByStatus(
            @Header("Authorization") String user_token,
            @Query("user_id") int user_id,
            @Query("pagination") String pagination);

    @FormUrlEncoded
    @POST("api/driver-accept-order")
    Call<ResponseBody> familyAcceptOrder(@Header("Authorization") String user_token,
                                         @Field("client_id") int client_id,
                                         @Field("order_id") int order_id,
                                         @Field("driver_id") int driver_id
    );

    @FormUrlEncoded
    @POST("api/driver-refuse-order")
    Call<ResponseBody> familyrefuesOrder(@Header("Authorization") String user_token,
                                         @Field("client_id") int client_id,
                                         @Field("order_id") int order_id,
                                         @Field("driver_id") int driver_id
    );


    @GET("api/get-one-order")
    Call<OrderModel> getorderdetials(@Header("Authorization") String user_token,
                                     @Query("order_id") int order_id
    );

    @FormUrlEncoded
    @POST("api/driver-change-order-status")
    Call<ResponseBody> driverchangeOrderstatus(
            @Header("Authorization") String user_token,
            @Field("order_id") int order_id,
            @Field("status") String status
    );

    @GET("api/sttings")
    Call<SettingModel> getSetting();

    @GET("api/Get-chat-messages-by-roomID")
    Call<MessageDataModel> getRoomMessages(
            @Header("Authorization") String user_token,

            @Query("room_id") int room_id,
            @Query("page") int pagination_status
    );


    @FormUrlEncoded
    @POST("api/send-message")
    Call<MessageModel> sendmessagetext(
            @Header("Authorization") String user_token,

            @Field("from_user_id") String from_user_id,

            @Field("to_user_id") String to_user_id,
            @Field("message_kind") String message_kind,
            @Field("chat_room_id") String chat_room_id,
            @Field("message") String message


    );


    @Multipart
    @POST("api/send-message")
    Call<MessageModel> sendmessagewithimage
            (
                    @Header("Authorization") String user_token,

                    @Part("from_user_id") RequestBody from_user_id,

                    @Part("to_user_id") RequestBody to_user_id,
                    @Part("message_kind") RequestBody message_kind,
                    @Part("chat_room_id") RequestBody chat_room_id,

                    @Part MultipartBody.Part imagepart

//
            );

    @GET("api/Get-Packages")
    Call<SubscriptionDataModel> getSubscription(@Query("type") String type);


    @FormUrlEncoded
    @POST("api/pay")
    Call<PackageResponse> buyPackage(@Field("package_id") int package_id,
                                     @Field("user_id") int user_id,
                                     @Field("price") String price


    );

    @GET("api/Get-cars")
    Call<MainCategoryModel> getMainCategory(@Query("pagination_status") String pagination_status);

    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Call<ResponseBody> updateToken(
            @Header("Authorization") String user_token,
            @Field("user_id") int user_id,
            @Field("phone_token") String phone_token,
            @Field("software_type") String software_type
    );

    @FormUrlEncoded
    @POST("api/drive_profile")
    Call<UserModel> getProfile(@Header("Authorization") String user_token,
                               @Field("id") int id
    );
    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseBody> contactUs(@Field("name") String name,
                                 @Field("email") String email,
                                 @Field("phone") String phone,
                                 @Field("subject") String subject,
                                 @Field("message") String message


    );
    @GET("api/get-rates")
    Call<FeedbackDataModel> getFeedback(@Header("Authorization") String user_token,
                                        @Query("user_id") int user_id,
                                        @Query("page") int page,
                                        @Query("pagination") String pagination,
                                        @Query("limit_per_page") int limit_per_page


    );

    @FormUrlEncoded
    @POST("api/update-show-phone-status")
    Call<UserModel> updatePhoneStatus(@Header("Authorization") String user_token,
                                      @Field("user_id") int user_id,
                                      @Field("show_phone_status") String status
    );
}