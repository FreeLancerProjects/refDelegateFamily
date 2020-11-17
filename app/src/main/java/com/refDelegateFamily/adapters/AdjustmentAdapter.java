package com.refDelegateFamily.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refDelegateFamily.R;
import com.refDelegateFamily.activities_fragments.activity_adjustment.AdjustmnetActivity;
import com.refDelegateFamily.activities_fragments.activity_feedback.FeedbackActivity;
import com.refDelegateFamily.databinding.BankRowBinding;
import com.refDelegateFamily.databinding.FeedbackRowBinding;
import com.refDelegateFamily.databinding.LoadMoreBinding;
import com.refDelegateFamily.models.BankModel;
import com.refDelegateFamily.models.FeedBackModel;

import java.util.List;

public class AdjustmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;
    private List<BankModel.Data> list;
    private Context context;
    private LayoutInflater inflater;


    public AdjustmentAdapter(List<BankModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            BankRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.bank_row, parent, false);
            return new MyHolder(binding);
        } else {
            LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more, parent, false);
            return new LoadMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setModel(list.get(position));

            ((MyHolder) holder).binding.tvadjust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof AdjustmnetActivity) {
                        AdjustmnetActivity adjustmnetActivity = (AdjustmnetActivity) context;
                        adjustmnetActivity.sendadust(list.get(position));
                    }
                }
            });


        } else if (holder instanceof LoadMoreHolder) {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private BankRowBinding binding;

        public MyHolder(BankRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public static class LoadMoreHolder extends RecyclerView.ViewHolder {
        private LoadMoreBinding binding;

        public LoadMoreHolder(LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {
            return LOAD;
        } else {
            return DATA;
        }
    }
}
