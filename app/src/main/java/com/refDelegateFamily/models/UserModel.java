package com.refDelegateFamily.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public static class User implements Serializable {
        private int id;
        private String name;
        private String email;
        private String city;
        private String phone_code;
        private String phone;
        private String image;
        private String logo;
        private String card_image;
        private String licence_image;
        private String front_car_image;
        private String back_car_image;
        private String account_bank_number;
        private String ipad_number;
        private String nationality_title;
        private String car_type;
        private String car_model;
        private String year_of_manufacture;
        private String card_id;
        private String address_registered_for_bank_account;
        private String address;
        private String token;
        private String latitude;
        private String longitude;
        private String user_type;
        private String software_type;
        private String details;
        private String fireBaseToken;

        public User() {
        }

        public User(int id, String name, String phone_code, String phone, String logo, String token) {
            this.id = id;
            this.name = name;
            this.phone_code = phone_code;
            this.phone = phone;
            this.logo = logo;
            this.token = token;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getCity() {
            return city;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getImage() {
            return image;
        }

        public String getLogo() {
            return logo;
        }

        public String getToken() {
            return token;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }

        public String getType() {
            return user_type;
        }

        public String getFireBaseToken() {
            return fireBaseToken;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }



        public String getAccount_bank_number() {
            return account_bank_number;
        }

        public String getIpad_number() {
            return ipad_number;
        }


        public String getCar_type() {
            return car_type;
        }

        public String getCar_model() {
            return car_model;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getDetails() {
            return details;
        }

        public String getCard_image() {
            return card_image;
        }

        public String getLicence_image() {
            return licence_image;
        }

        public String getFront_car_image() {
            return front_car_image;
        }

        public String getBack_car_image() {
            return back_car_image;
        }

        public String getNationality_title() {
            return nationality_title;
        }

        public String getYear_of_manufacture() {
            return year_of_manufacture;
        }

        public String getCard_id() {
            return card_id;
        }

        public String getAddress_registered_for_bank_account() {
            return address_registered_for_bank_account;
        }

        public String getSoftware_type() {
            return software_type;
        }
    }
}
