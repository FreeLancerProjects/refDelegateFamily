package com.refDelegateFamily.models;

import java.io.Serializable;
import java.util.List;

public class MainCategoryModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable {
        private int id;
        private String car_model;

        public int getId() {
            return id;
        }

        public String getCar_model() {
            return car_model;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCar_model(String car_model) {
            this.car_model = car_model;
        }
    }


}