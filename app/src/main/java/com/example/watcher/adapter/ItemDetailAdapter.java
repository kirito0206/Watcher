package com.example.watcher.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.watcher.R;
import com.example.watcher.data.ChildrenX;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder>{

    private List<ChildrenX> regionsList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        View regionView;
        TextView eare;
        TextView confirmLeft;
        TextView confirmTotal;
        TextView cure;
        TextView death;

        public ViewHolder(View v){
            super(v);
            regionView = v;
            eare = v.findViewById(R.id.eare_name);
            confirmLeft = v.findViewById(R.id.confirm_left);
            confirmTotal =  v.findViewById(R.id.confirm_total);
            cure =  v.findViewById(R.id.cure_number);
            death = v.findViewById(R.id.death_number);
        }
    }

    public ItemDetailAdapter(List<ChildrenX> pList){
        regionsList = pList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.region_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0){
            holder.eare.setText("地区");
            holder.confirmTotal.setText("累计\n确诊");
            holder.cure.setText("治愈");
            holder.death.setText("死亡");
            holder.confirmLeft.setText("现有\n确诊");
            return;
        }
        ChildrenX region = regionsList.get(position-1);
        holder.eare.setText(region.getName());
        int confirmTotal = region.getTotal().getConfirm();
        int cure = region.getTotal().getHeal();
        int dead = region.getTotal().getDead();
        int confirmLeft = confirmTotal-cure-dead;
        holder.confirmTotal.setText(confirmTotal+"");
        holder.cure.setText(cure+"");
        holder.death.setText(dead+"");
        holder.confirmLeft.setText(confirmLeft+"");
    }

    @Override
    public int getItemCount() {
        return regionsList.size()+1;
    }
}
