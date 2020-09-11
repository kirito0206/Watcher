package com.example.watcher.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watcher.R;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.data.Comment;
import com.example.watcher.data.User;
import com.example.watcher.databinding.CommentItemBinding;
import com.example.watcher.databinding.FooterBinding;
import com.example.watcher.ui.MyApplication;
import com.example.watcher.ui.SubCommentActivity;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Comment> commentsList = new ArrayList<>();
    private static int TYPE_FOOTER = 1;
    private static int TYPE_ITEM = 0;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private CommentItemBinding viewBinding;

        public ViewHolder(View v, CommentItemBinding viewBinding){
            super(v);
            this.viewBinding = viewBinding;
        }
        public void bind(Comment comment){
            viewBinding.commentUserName.setText(comment.getUsername());
            viewBinding.commentMain.setText(comment.getText());
            viewBinding.messageText.setText(comment.getUploadtime());
            viewBinding.starNumber.setText(comment.getStar()+"");
            viewBinding.subcommentNumber.setText(comment.getSubcomments().size()+"");
            viewBinding.starImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewBinding.starImage.setImageResource(R.drawable.star_filled);
                    viewBinding.starNumber.setText(comment.getStar()+1+"");
                    RetrofitUtil.getInstance().starMainComment(comment.getCommentid());
                }
            });
            if (User.getInstance().getUserName().equals(comment.getUsername())){
                viewBinding.deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RetrofitUtil.getInstance().deleteMainComment(comment.getCommentid());
                    }
                });
            }else {
                viewBinding.deleteImage.setVisibility(View.GONE);
            }
        }
    }

    public CommentAdapter(List<Comment> pList){
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取view对应的位置
                int position=holder.getLayoutPosition();
                if (position != commentsList.size()){
                    Intent intent = new Intent(MyApplication.getContext(), SubCommentActivity.class);
                    intent.putExtra("parentId",commentsList.get(position).getCommentid());
                    intent.putExtra("parentName",commentsList.get(position).getUsername());
                    intent.putExtra("parentText",commentsList.get(position).getText());
                    intent.putExtra("parentStarNumber",commentsList.get(position).getStar());
                    intent.putExtra("parentDate",commentsList.get(position).getUploadtime());
                    MyApplication.getContext().startActivity(intent);
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentAdapter.ViewHolder){
            ((CommentAdapter.ViewHolder) holder).bind(commentsList.get(position));
        }else if (holder instanceof CommentAdapter.FooterViewHolder){
            ((CommentAdapter.FooterViewHolder) holder).binding();
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
        public FooterViewHolder(@NonNull View itemView,FooterBinding viewBinding) {
            super(itemView);
            this.viewBinding =viewBinding;
        }
        public void binding(){
            viewBinding.footerText.setText("没有更多了！");
        }
    }
}
