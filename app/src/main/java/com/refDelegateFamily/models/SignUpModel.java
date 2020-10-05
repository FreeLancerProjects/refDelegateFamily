package com.refDelegateFamily.models;

import android.content.Context;
import android.net.Uri;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.refDelegateFamily.BR;
import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_add_Product.AddProductActivity;

import java.io.Serializable;


public class SignUpModel extends BaseObservable implements Serializable {
    private String logo;
    private String identity_card_image;
    private String license_image;
    private String car_front_image;
    private String car_back_image;
    private String name;
    private String email;
    private String account_bank_number;
    private String ipad_number;
    private String nationality;
    private String car_type;
    private String car_model;
    private String car_date;
    private String identity_card;
    private String address;
    private String phone_code;
    private String phone;


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
                !nationality.trim().isEmpty() &&
                !identity_card.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
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

            if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                error_email.set(context.getString(R.string.inv_email));

            } else {
                error_email.set(null);

            }
            if (identity_card.trim().isEmpty()) {
                error_identity_card.set(context.getString(R.string.field_required));

            } else {
                error_identity_card.set(null);

            }
            if (nationality.trim().isEmpty()) {
                error_nationality.set(context.getString(R.string.field_required));

            } else {
                error_nationality.set(null);

            }
            return false;
        }
    }

    public boolean step2(Context context) {

        if (!car_date.trim().isEmpty() &&
                !car_model.trim().isEmpty() &&
                !car_type.trim().isEmpty() &&
                !account_bank_number.trim().isEmpty() &&
                !ipad_number.trim().isEmpty()
        ) {
            error_car_date.set(null);
            error_car_model.set(null);
            error_car_type.set(null);
            error_account_bank_number.set(null);
            error_ipad_number.set(null);

            return true;
        } else {
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
            if (car_date.trim().isEmpty()) {
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

            } else {
                error_ipad_number.set(null);

            }

            return false;
        }


    }

//    public boolean step3(Context context) {
//        return false;
//    }


    public SignUpModel() {
        setName("");
        setEmail("");
        setLogo("");
        setAddress("");
        setAccount_bank_number("");
        setIpad_number("");
        setCar_back_image("");
        setCar_date("");
        setCar_type("");
        setCar_model("");
        setIdentity_card("");
        setNationality("");

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


    public String getIdentity_card_image() {
        return identity_card_image;
    }

    public void setIdentity_card_image(String identity_card_image) {
        this.identity_card_image = identity_card_image;
    }

    public String getLicense_image() {
        return license_image;
    }

    public void setLicense_image(String license_image) {
        this.license_image = license_image;
    }

    public String getCar_front_image() {
        return car_front_image;
    }

    public void setCar_front_image(String car_front_image) {
        this.car_front_image = car_front_image;
    }

    public String getCar_back_image() {
        return car_back_image;
    }

    public void setCar_back_image(String car_back_image) {
        this.car_back_image = car_back_image;
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
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
        notifyPropertyChanged(BR.nationality);
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
    public String getCar_date() {
        return car_date;
    }

    public void setCar_date(String car_date) {
        this.car_date = car_date;
        notifyPropertyChanged(BR.car_date);
    }

    @Bindable
    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
        notifyPropertyChanged(BR.identity_card);
    }


}

