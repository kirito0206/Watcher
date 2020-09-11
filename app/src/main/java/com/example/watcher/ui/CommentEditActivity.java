package com.example.watcher.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.watcher.R;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.databinding.ActivityCommentEditBinding;
import com.example.watcher.databinding.AppBarMainBinding;

public class CommentEditActivity extends AppCompatActivity {

    private ActivityCommentEditBinding viewBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCommentEditBinding.inflate(LayoutInflater.from(this));
        initView();
        initStatusColor();
        setContentView(viewBinding.getRoot());
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
        viewBinding.appBar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinding.appBar.toolbar.inflateMenu(R.menu.comment_edit);
        viewBinding.appBar.toolbar.setTitle("发表评论");
        View view = viewBinding.getRoot().findViewById(R.id.action_send);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = viewBinding.commentContent.getText().toString();
                if (!text.equals("")){
                    RetrofitUtil.getInstance().sendMainComment(text);
                    finish();
                }
            }
        });
    }


}
