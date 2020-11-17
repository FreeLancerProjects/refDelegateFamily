package com.refDelegateFamily.models;

import java.io.Serializable;
import java.util.List;

public class BankModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable {
        private int id;
        private String bank_name;

        public int getId() {
            return id;
        }

        public String getBank_name() {
            return bank_name;
        }
    }
}
