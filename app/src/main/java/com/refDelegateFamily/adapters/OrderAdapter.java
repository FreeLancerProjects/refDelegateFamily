package com.refDelegateFamily.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_home.HomeActivity;
import com.refDelegateFamily.activities_fragments.activity_home.NewOrderActivity.NewOrderActivity;
import com.refDelegateFamily.activities_fragments.activity_orderdetail.OrderDetailActivity;
import com.refDelegateFamily.databinding.ItemMainOffersBinding;
import com.refDelegateFamily.databinding.ItemOrderBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.OrderModel;
import com.refDelegateFamily.models.UserModel;
import com.refDelegateFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderAdapterVH> {

    private List<OrderModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
//    Preferences preferences;
//    UserModel userModel;
    //private OrderDetailAdapter orderDetailAdapter;


    public OrderAdapter(List<OrderModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public OrderAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order, parent, false);
        return new OrderAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapterVH holder, int position) {
        holder.binding.setLang(Language_Helper.getLanguage(context));
        holder.binding.setModel(orderlist.get(position));
        //    orderDetailAdapter = new OrderDetailAdapter(context);

        holder.itemView.setOnClickListener(view -> {
            if (context instanceof HomeActivity || context instanceof NewOrderActivity) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("DATA", orderlist.get(position));
                context.startActivity(intent);
                //  ((AppCompatActivity) context).finish();}
            }
            });


        }

        @Override
        public int getItemCount () {
            return orderlist.size();
        }

        public class OrderAdapterVH extends RecyclerView.ViewHolder {
            public ItemOrderBinding binding;

            public OrderAdapterVH(@NonNull ItemOrderBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

            }
        }


    }
