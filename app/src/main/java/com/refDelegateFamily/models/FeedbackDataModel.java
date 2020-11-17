package com.refDelegateFamily.models;

import java.io.Serializable;
import java.util.List;

public class FeedbackDataModel implements Serializable {
    private int current_page;
    private List<FeedBackModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<FeedBackModel> getData() {
        return data;
    }
}
