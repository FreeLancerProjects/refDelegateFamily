package com.refDelegateFamily.models;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.refDelegateFamily.BR;
import com.refDelegateFamily.R;

import java.io.Serializable;


public class SignUpModel extends BaseObservable implements Serializable {
    private String logo;
    private String name;
    private String email;
    private String address;
    private String phone_code;
    private String phone;
    private String user_type;
    private String software_type;
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
    private String latitude;
    private String longitude;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();
    public ObservableField<String> error_nationality = new ObservableField<>();
    public ObservableField<String> error_identity_card = new ObservableField<>();
    public ObservableField<String> error_car_date = new ObservableField<>();
    public ObservableField<String> error_car_model = new ObservableField<>();
    public ObservableField<String> error_car_type = new ObservableField<>();
    public ObservableField<String> error_account_bank_number = new ObservableField<>();
    public ObservableField<String> error_ipad_number = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() &&
                !email.trim().isEmpty() &&
                !address.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        ) {
            error_name.set(null);
            error_email.set(null);
            error_address.set(null);

            return true;
        } else {
            if (name.trim().isEmpty()) {
                error_name.set(context.getString(R.string.field_required));

            } else {
                error_name.set(null);

            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                error_email.set(context.getString(R.string.inv_email));

            } else {
                error_email.set(null);

            }
            if (address.trim().isEmpty()) {
                error_address.set(context.getString(R.string.field_required));

            } else {
                error_address.set(null);

            }
            return false;
        }
    }

    public boolean step1(Context context) {

        if (!name.trim().isEmpty() &&
                !email.trim().isEmpty() &&
                !address.trim().isEmpty() &&
                !nationality_title.trim().isEmpty() &&
                !card_id.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
                && !logo.isEmpty()
        ) {
            error_name.set(null);
            error_email.set(null);
            error_address.set(null);
            error_nationality.set(null);
            error_identity_card.set(null);

            return true;
        } else {
            if (name.trim().isEmpty()) {
                error_name.set(context.getString(R.string.field_required));

            } else {
                error_name.set(null);

            }
            if (logo.isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_logo), Toast.LENGTH_LONG).show();
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                error_email.set(context.getString(R.string.inv_email));

            } else {
                error_email.set(null);

            }
            if (card_id.trim().isEmpty()) {
                error_identity_card.set(context.getString(R.string.field_required));

            } else {
                error_identity_card.set(null);

            }
            if (address.trim().isEmpty()) {
                error_address.set(context.getString(R.string.field_required));

            } else {
                error_address.set(null);

            }
            if (nationality_title.trim().isEmpty()) {
                error_nationality.set(context.getString(R.string.field_required));

            } else {
                error_nationality.set(null);

            }
            return false;
        }
    }

    public boolean step2(Context context) {

        if (!year_of_manufacture.trim().isEmpty() &&
                !car_model.trim().isEmpty() &&
                !car_type.trim().isEmpty() &&
                !account_bank_number.trim().isEmpty() &&
                !ipad_number.trim().isEmpty()
                && !card_image.isEmpty()
        ) {
            error_car_date.set(null);
            error_car_model.set(null);
            error_car_type.set(null);
            error_account_bank_number.set(null);
            error_ipad_number.set(null);

            return true;
        } else {
            if (card_image.isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_national_image), Toast.LENGTH_LONG).show();
            }
            if (car_type.trim().isEmpty()) {
                error_car_type.set(context.getString(R.string.field_required));

            } else {
                error_car_type.set(null);

            }
            if (car_model.trim().isEmpty()) {
                error_car_model.set(context.getString(R.string.field_required));

            } else {
                error_car_model.set(null);
            }
            if (year_of_manufacture.trim().isEmpty()) {
                error_car_date.set(context.getString(R.string.field_required));

            } else {
                error_car_date.set(null);

            }

            if (account_bank_number.trim().isEmpty()) {
                error_account_bank_number.set(context.getString(R.string.field_required));

            } else {
                error_account_bank_number.set(null);
            }

            if (ipad_number.trim().isEmpty()) {
                error_ipad_number.set(context.getString(R.string.field_required));

            } else if (ipad_number.length() != 22) {
                error_ipad_number.set(context.getString(R.string.ipan_number_length_error));
            } else {
                error_ipad_number.set(null);
            }

            return false;
        }


    }

    public boolean step3(Context context) {

        if (!back_car_image.isEmpty()
                && !front_car_image.isEmpty()
                && !licence_image.isEmpty()
        ) {


            return true;
        } else {
            if (licence_image.isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_licence), Toast.LENGTH_LONG).show();
            }
            if (back_car_image.isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_car), Toast.LENGTH_LONG).show();
            }
            if (front_car_image.isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_fr), Toast.LENGTH_LONG).show();
            }

            return false;
        }


    }


    public SignUpModel() {
        setName("");
        setEmail("");
        setLogo("");
        setAddress("");
        setAccount_bank_number("");
        setIpad_number("");
        setSoftware_type("android");
        setUser_type("driver");
        setLatitude("0.0");
        setLongitude("0.0");
        setYear_of_manufacture("");
        setCard_id("");
        setNationality_title("");
        setCar_model("");
        setCar_type("");
        setCard_image("");
        setBack_car_image("");
        setFront_car_image("");
        setLicence_image("");
        setAddress_registered_for_bank_account("aa");


    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }


    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }


    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getSoftware_type() {
        return software_type;
    }

    public void setSoftware_type(String software_type) {
        this.software_type = software_type;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getCard_image() {
        return card_image;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    public String getLicence_image() {
        return licence_image;
    }

    public void setLicence_image(String licence_image) {
        this.licence_image = licence_image;
    }

    public String getFront_car_image() {
        return front_car_image;
    }

    public void setFront_car_image(String front_car_image) {
        this.front_car_image = front_car_image;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBack_car_image() {
        return back_car_image;
    }

    public void setBack_car_image(String back_car_image) {
        this.back_car_image = back_car_image;
    }


    @Bindable
    public String getAccount_bank_number() {
        return account_bank_number;
    }

    public void setAccount_bank_number(String account_bank_number) {
        this.account_bank_number = account_bank_number;
        notifyPropertyChanged(BR.account_bank_number);
    }

    @Bindable
    public String getIpad_number() {
        return ipad_number;
    }

    public void setIpad_number(String ipad_number) {
        this.ipad_number = ipad_number;
        notifyPropertyChanged(BR.ipad_number);
    }

    @Bindable
    public String getNationality_title() {
        return nationality_title;
    }

    public void setNationality_title(String nationality_title) {
        this.nationality_title = nationality_title;
        notifyPropertyChanged(BR.nationality_title);
    }

    @Bindable
    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
        notifyPropertyChanged(BR.car_type);
    }

    @Bindable
    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
        notifyPropertyChanged(BR.car_model);
    }

    @Bindable
    public String getYear_of_manufacture() {
        return year_of_manufacture;
    }

    public void setYear_of_manufacture(String year_of_manufacture) {
        this.year_of_manufacture = year_of_manufacture;
        notifyPropertyChanged(BR.year_of_manufacture);
    }

    @Bindable
    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
        notifyPropertyChanged(BR.card_id);
    }

    @Bindable
    public String getAddress_registered_for_bank_account() {
        return address_registered_for_bank_account;
    }

    public void setAddress_registered_for_bank_account(String address_registered_for_bank_account) {
        this.address_registered_for_bank_account = address_registered_for_bank_account;
        notifyPropertyChanged(BR.address_registered_for_bank_account);
    }
}

