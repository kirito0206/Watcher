package com.example.watcher.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.watcher.R;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.adapter.SubCommentAdapter;
import com.example.watcher.data.Comment;
import com.example.watcher.data.Subcomment;
import com.example.watcher.databinding.ActivitySubCommentBinding;

import java.util.ArrayList;
import java.util.List;

public class SubCommentActivity extends AppCompatActivity {

    private ActivitySubCommentBinding viewBinding;
    private Integer parentId = 0;
    private String parentName = "";
    private String parentText = "";
    private Integer parentStarNumber = 0;
    private String parentDate = "";
    public static Handler mHandler;
    private List<Subcomment> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySubCommentBinding.inflate(LayoutInflater.from(this));
        parentId = getIntent().getIntExtra("parentId",0);
        parentName = getIntent().getStringExtra("parentName");
        parentText = getIntent().getStringExtra("parentText");
        parentStarNumber = getIntent().getIntExtra("parentStarNumber",0);
        parentDate = getIntent().getStringExtra("parentDate");
        initHandler();
        initView();
        initRecyclerView();
        initStatusColor();
        setContentView(viewBinding.getRoot());
    }

    private void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                list.clear();
                list.addAll(RetrofitUtil.getInstance().getSubComments(parentId));
                viewBinding.subcommentRecyclerView.getAdapter().notifyDataSetChanged();
                if (msg.what == 1){
                    Toast.makeText(MyApplication.getContext(),"评论成功！",Toast.LENGTH_SHORT).show();
                }else if (msg.what == 2){
                    Toast.makeText(MyApplication.getContext(),"删除成功！",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void initStatusColor(){
        Window window = getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
    }

    private void initView(){
        viewBinding.commentUserName.setText(parentName);
        viewBinding.commentMain.setText(parentText);
        viewBinding.messageText.setText(parentDate);
        viewBinding.starNumber.setText(parentStarNumber+"");
        viewBinding.starImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBinding.starImage.setImageResource(R.drawable.star_filled);
                viewBinding.starNumber.setText(parentStarNumber+1+"");
                RetrofitUtil.getInstance().starMainComment(parentId);
            }
        });
        viewBinding.appBar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinding.appBar.toolbar.setTitle("详情");

        //下方评论
        viewBinding.sendSubcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = viewBinding.subcommentEdit.getText().toString();
                if (!text.equals("")){
                    RetrofitUtil.getInstance().sendSubComment(text,parentId);
                    viewBinding.subcommentEdit.setText("");
                    viewBinding.subcommentRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    private void initRecyclerView(){
        list = RetrofitUtil.getInstance().getSubComments(parentId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        viewBinding.subcommentRecyclerView.setLayoutManager(layoutManager);
        SubCommentAdapter adapter = new SubCommentAdapter(list);
        viewBinding.subcommentRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        viewBinding.subcommentRecyclerView.setAdapter(adapter);
    }
}
