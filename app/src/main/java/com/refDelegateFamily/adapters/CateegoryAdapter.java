package com.refDelegateFamily.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.refDelegateFamily.R;
import com.refDelegateFamily.databinding.SpinnerCategoryRowBinding;
import com.refDelegateFamily.models.MainCategoryModel;

import java.util.List;

import io.paperdb.Paper;

public class CateegoryAdapter extends BaseAdapter {
    private List<MainCategoryModel.Data> data;
    private Context context;
    private String lang;

    public CateegoryAdapter(List<MainCategoryModel.Data> data, Context context) {
        this.data = data;
        this.context = context;
        lang = Paper.book().read("lang","ar");

    }

    @Override
    public int getCount() {
        if (data.size() == 0)
            return 0;
        else
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerCategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_category_row,viewGroup,false);

       binding.setData(data.get(i).getCar_model());

        return binding.getRoot();
    }
}
