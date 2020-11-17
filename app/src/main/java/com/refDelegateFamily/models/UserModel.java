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
        private String notification_status;
        private String city;
        private String package_finished_at;
        private String phone_code;
        private String show_phone_status;

        private String phone;
        private String image;
        private String logo;
        private String account_bank_number;
        private String ipad_number;
        private String nationality_title;
        private String card_id;
        private String address_registered_for_bank_account;
        private String token;
        private String user_type;
        private String software_type;
        private String details;
        private String fireBaseToken;
        private Tracker tracker_fk;
        private Car car_fk;

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

        public String getShow_phone_status() {
            return show_phone_status;
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

        public void setNotification_status(String notification_status) {
            this.notification_status = notification_status;
        }

        public String getNotification_status() {
            return notification_status;
        }

        public String getPackage_finished_at() {
            return package_finished_at;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getDetails() {
            return details;
        }

        public String getNationality_title() {
            return nationality_title;
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

        public Tracker getTracker_fk() {
            return tracker_fk;
        }

        public Car getCar_fk() {
            return car_fk;
        }

        public class Tracker implements Serializable {
            private String latitude;
            private String longitude;
            private String address;


            public String getLatitude() {
                return latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public String getAddress() {
                return address;
            }
        }

        public class Car implements Serializable {
            private int id;
            private int car_type_id;
            private String car_model;
            private String year_of_manufacture;
            private String licence_image;
            private String back_car_image;
            private String front_car_image;
            private CarType car_type_fk;

            public int getId() {
                return id;
            }

            public String getCar_model() {
                return car_model;
            }

            public String getYear_of_manufacture() {
                return year_of_manufacture;
            }

            public String getLicence_image() {
                return licence_image;
            }

            public int getCar_type_id() {
                return car_type_id;
            }

            public String getBack_car_image() {
                return back_car_image;
            }

            public String getFront_car_image() {
                return front_car_image;
            }

            public CarType getCar_type_fk() {
                return car_type_fk;
            }

            public class CarType implements Serializable {
                private int id;
                private String title;
                private String image;

                public int getId() {
                    return id;
                }

                public String getTitle() {
                    return title;
                }

                public String getImage() {
                    return image;
                }
            }

        }

    }
}
