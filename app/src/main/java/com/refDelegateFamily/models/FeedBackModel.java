package com.refDelegateFamily.models;

import java.io.Serializable;

public class FeedBackModel implements Serializable {
    private int id;
    private int from_user_id;
    private int to_user_id;
    private int order_id;
    private String rating_comment;
    private double rating;
    private UserModel.User from_user;

    public int getId() {
        return id;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getRating_comment() {
        return rating_comment;
    }

    public double getRating() {
        return rating;
    }

    public UserModel.User getFrom_user() {
        return from_user;
    }
}
