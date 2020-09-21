package com.refFamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refFamily.R;
import com.refFamily.databinding.ItemMainOffersBinding;
import com.refFamily.databinding.ItemOffersBinding;
import com.refFamily.models.MarketCatogryModel;
import com.refFamily.models.OfferModel;
import com.refFamily.models.UserModel;
import com.refFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferAdapterVH> {

    private List<OfferModel> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    public OfferAdapter(Context context) {
        this.context = context;
    }

    public OfferAdapter(List<OfferModel> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public OfferAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOffersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_offers, parent, false);
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
        public ItemOffersBinding binding;

        public OfferAdapterVH(@NonNull ItemOffersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
