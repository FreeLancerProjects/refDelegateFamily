package com.refFamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refFamily.R;
import com.refFamily.databinding.ItemCategoryBinding;
import com.refFamily.databinding.ItemMainOffersBinding;
import com.refFamily.models.MarketCatogryModel;
import com.refFamily.models.UserModel;
import com.refFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferAdapterVH> {

    private List<MarketCatogryModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    public OfferAdapter(Context context) {
        this.context = context;
    }

    public OfferAdapter(List<MarketCatogryModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public OfferAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMainOffersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_main_offers, parent, false);
        return new OfferAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapterVH holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class OfferAdapterVH extends RecyclerView.ViewHolder {
        public ItemMainOffersBinding binding;

        public OfferAdapterVH(@NonNull ItemMainOffersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
