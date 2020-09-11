package com.example.watcher.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watcher.R;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.data.Subcomment;
import com.example.watcher.data.User;
import com.example.watcher.databinding.CommentItemBinding;
import com.example.watcher.databinding.FooterBinding;
import com.example.watcher.ui.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class SubCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Subcomment> commentsList = new ArrayList<>();
    private static int TYPE_FOOTER = 1;
    private static int TYPE_ITEM = 0;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private CommentItemBinding viewBinding;
        public ViewHolder(View v, CommentItemBinding viewBinding){
            super(v);
            this.viewBinding = viewBinding;
        }
        public void bind(Subcomment comment){
            viewBinding.commentUserName.setText(comment.getUsername());
            viewBinding.commentMain.setText(comment.getText());
            viewBinding.messageText.setText(comment.getUploadtime());
            viewBinding.starNumber.setText(comment.getStar()+"");
            viewBinding.subcommentNumber.setVisibility(View.GONE);
            viewBinding.messageImage.setVisibility(View.GONE);
            viewBinding.starImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewBinding.starImage.setImageResource(R.drawable.star_filled);
                    viewBinding.starNumber.setText(comment.getStar()+1+"");
                    RetrofitUtil.getInstance().starSubComment(comment.getSubcommentid());
                }
            });
            if (User.getInstance().getUserName().equals(comment.getUsername())){
                viewBinding.deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RetrofitUtil.getInstance().deleteSubComment(comment.getSubcommentid());
                    }
                });
            }else {
                viewBinding.deleteImage.setVisibility(View.GONE);
            }
        }
    }

    public SubCommentAdapter(List<Subcomment> pList){
        commentsList = pList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER){
            FooterBinding viewBinding = FooterBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            FooterViewHolder holder = new FooterViewHolder(viewBinding.getRoot(),viewBinding);
            return holder;
        }
        CommentItemBinding viewBinding = CommentItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        final ViewHolder holder = new ViewHolder(viewBinding.getRoot(),viewBinding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SubCommentAdapter.ViewHolder){
            ((SubCommentAdapter.ViewHolder) holder).bind(commentsList.get(position));
        }else if (holder instanceof SubCommentAdapter.FooterViewHolder){
            ((SubCommentAdapter.FooterViewHolder) holder).binding();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == commentsList.size())
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return commentsList.size()+1;
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        private FooterBinding viewBinding;
        public FooterViewHolder(@NonNull View itemView, FooterBinding viewBinding) {
            super(itemView);
            this.viewBinding =viewBinding;
        }
        public void binding(){
            viewBinding.footerText.setText("没有更多了！");
        }
    }
}
