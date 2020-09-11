package com.example.watcher.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.watcher.R;
import com.example.watcher.databinding.NumberGridViewItemBinding;
import com.example.watcher.Retrofit.RetrofitUtil;

import java.util.List;

public class NumberAdapter extends BaseAdapter {

    private List<Integer> numberTodayList;
    private List<Integer> numberTotalList;
    private List<String> titleList;

    public NumberAdapter(List<String> titleList,List<Integer> numberTotalList, List<Integer> numberTodayList) {
        this.numberTodayList = numberTodayList;
        this.numberTotalList = numberTotalList;
        this.titleList = titleList;
    }

    @Override
    public int getCount() {
        return numberTotalList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NumberGridViewItemBinding gridBinding = NumberGridViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        if (convertView == null) {
            gridBinding.itemTitle.setText(titleList.get(position));
            gridBinding.itemNumber.setText(numberTotalList.get(position)+"");
            if (numberTodayList.get(position) == null){
                gridBinding.itemToday.setText("较昨日+0");
            } else if (numberTodayList.get(position) >= 0)
                gridBinding.itemToday.setText("较昨日+"+numberTodayList.get(position));
            else
                gridBinding.itemToday.setText("较昨日"+numberTodayList.get(position));
        } else {
            return convertView;
        }

        return gridBinding.getRoot();
    }
}
