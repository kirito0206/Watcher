package com.example.watcher.ui.discuss;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.adapter.CommentAdapter;
import com.example.watcher.data.Comment;
import com.example.watcher.databinding.FragmentDiscussBinding;
import com.example.watcher.ui.CommentEditActivity;

import java.util.List;

public class DiscussFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentDiscussBinding viewBinding;
    public static Handler mHandler;
    private List<Comment> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentDiscussBinding.inflate(inflater);
        initHandler();
        initRecyclerView();
        initView();
        return viewBinding.getRoot();
    }

    private void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                list.clear();
                list.addAll(RetrofitUtil.getInstance().getComments());
                viewBinding.commentsRecycler.getAdapter().notifyDataSetChanged();
                if (msg.what == 1){
                    Toast.makeText(getActivity(),"发表评论成功！",Toast.LENGTH_SHORT).show();
                }else if (msg.what == 2){
                    Toast.makeText(getActivity(),"删除评论成功！",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void initRecyclerView(){
        list = RetrofitUtil.getInstance().getComments();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        viewBinding.commentsRecycler.setLayoutManager(layoutManager);
        CommentAdapter adapter = new CommentAdapter(list);
        viewBinding.commentsRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        viewBinding.commentsRecycler.setAdapter(adapter);
    }

    private void initView(){
        //下拉刷新
        viewBinding.swiperefreshLayout.setOnRefreshListener(this);

        viewBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommentEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        //关闭下拉刷新进度条
        viewBinding.swiperefreshLayout.setRefreshing(false);
        RetrofitUtil.getInstance().updateComments(5);
    }
}
