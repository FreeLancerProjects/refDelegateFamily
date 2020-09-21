package com.refFamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refFamily.R;
import com.refFamily.activities_fragments.activity_orderdetail.OrderDetailActivity;
import com.refFamily.databinding.ItemOrderBinding;
import com.refFamily.databinding.ItemOrderDetailBinding;
import com.refFamily.language.Language_Helper;
import com.refFamily.models.OrderModel;
import com.refFamily.models.UserModel;
import com.refFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailAdapterVH> {

    private List<OrderModel> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    public OrderDetailAdapter(Context context) {
        this.context = context;
    }

    public OrderDetailAdapter(List<OrderModel> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public OrderDetailAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_detail, parent, false);
        return new OrderDetailAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapterVH holder, int position) {
        holder.binding.setLang(Language_Helper.getLanguage(context));



    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class OrderDetailAdapterVH extends RecyclerView.ViewHolder {
        public ItemOrderDetailBinding binding;

        public OrderDetailAdapterVH(@NonNull ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
