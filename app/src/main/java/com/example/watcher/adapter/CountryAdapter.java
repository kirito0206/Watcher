package com.example.watcher.adapter;

import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watcher.databinding.FooterBinding;
import com.example.watcher.databinding.HeaderBinding;
import com.example.watcher.databinding.RegionItemBinding;
import com.example.watcher.ui.MyApplication;
import com.example.watcher.ui.DetailWorldActivity;
import com.example.watcher.data.AreaTree;
import com.example.watcher.ui.situation.fragment.WorldFragment;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<AreaTree> regionsList = new ArrayList<>();
    private static int TYPE_FOOTER = 1;
    private static int TYPE_ITEM = 0;
    private static int TYPE_Header = -1;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private RegionItemBinding viewBinding;
        public ViewHolder(View v,RegionItemBinding viewBinding){
            super(v);
            this.viewBinding = viewBinding;
        }
        public void bind(AreaTree areaTree){
            viewBinding.eareName.setText(areaTree.getName());
            viewBinding.cureNumber.setText(areaTree.getTotal().getHeal()+"");
            viewBinding.deathNumber.setText(areaTree.getTotal().getDead()+"");
            viewBinding.confirmTotal.setText(areaTree.getTotal().getConfirm()+"");
            viewBinding.confirmLeft.setText(areaTree.getTotal().getConfirm() - areaTree.getTotal().getDead() - areaTree.getTotal().getHeal() +"");
        }
    }

    public CountryAdapter(List<AreaTree> pList){
        regionsList = pList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER){
            FooterBinding viewBinding = FooterBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            FooterViewHolder holder = new FooterViewHolder(viewBinding.getRoot(),viewBinding);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 2;
                    WorldFragment.mHandler.sendMessage(msg);
                }
            });
            return holder;
        }
        if (viewType == TYPE_Header){
            HeaderBinding viewBinding = HeaderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            HeaderViewHolder holder = new HeaderViewHolder(viewBinding.getRoot(),viewBinding);
            return holder;
        }
        RegionItemBinding viewBinding = RegionItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        final ViewHolder holder = new ViewHolder(viewBinding.getRoot(),viewBinding);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取view对应的位置
                int position=holder.getLayoutPosition();
                if (position != 0){
                    Intent intent = new Intent(MyApplication.getContext(), DetailWorldActivity.class);
                    intent.putExtra("name",regionsList.get(position-1).getName());
                    MyApplication.getContext().startActivity(intent);
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ((ViewHolder) holder).bind(regionsList.get(position-1));
        }else if (holder instanceof FooterViewHolder){
            ((FooterViewHolder) holder).binding();
        }else if (holder instanceof HeaderViewHolder){
            ((HeaderViewHolder) holder).binding();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_Header;
        if (position == regionsList.size()+1)
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return regionsList.size()+2;
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        private FooterBinding viewBinding;
        public FooterViewHolder(@NonNull View itemView,FooterBinding viewBinding) {
            super(itemView);
            this.viewBinding =viewBinding;
        }
        public void binding(){
            viewBinding.footerText.setText("点击加载更多！\n           ﹀");
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private HeaderBinding viewBinding;
        public HeaderViewHolder(@NonNull View itemView,HeaderBinding viewBinding) {
            super(itemView);
            this.viewBinding =viewBinding;
        }
        public void binding(){
            viewBinding.eareName.setText("国家地区");
            viewBinding.confirmLeft.setText("现有确诊");
            viewBinding.confirmTotal.setText("累计确诊");
            viewBinding.cureNumber.setText("治愈");
            viewBinding.deathNumber.setText("死亡");
        }
    }
}
