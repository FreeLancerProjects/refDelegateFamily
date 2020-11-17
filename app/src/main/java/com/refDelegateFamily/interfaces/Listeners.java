package com.refDelegateFamily.interfaces;


public interface Listeners {

    interface SignUpListener {

        void openSheet();

        void closeSheet();

        void checkDataValid();

        void checkReadPermission();

        void checkCameraPermission();

    }

    interface SkipListener {
        void skip();
    }

    interface CreateAccountListener {
        void createNewAccount();
    }

    interface ShowCountryDialogListener {
        void showDialog();
    }

    interface BackListener {
        void back();
    }

    interface LoginListener {
        void validate();

        void showCountryDialog();

        void costumerApp();

        void familyApp();
    }


    interface SettingAction {
        void onSubscriptions();

        void onProfile();

        void onEditProfile();

        void onLanguageSetting();

        void onTerms();

        void onPrivacy();

        void onRate();

        void onTone();

        void about();

        void onFeedback();

        void logout();

        void share();

        void contactus();

    }
}
