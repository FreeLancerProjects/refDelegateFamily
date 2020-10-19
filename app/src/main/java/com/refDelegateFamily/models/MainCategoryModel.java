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
        private String title;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


}