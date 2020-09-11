package com.example.watcher.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.watcher.R;
import com.example.watcher.databinding.ActivityWorldDetailBinding;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.adapter.NumberAdapter;
import com.example.watcher.adapter.ProvinceAdapter;
import com.example.watcher.data.AreaTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailWorldActivity extends AppCompatActivity {

    private ActivityWorldDetailBinding viewBinding;
    private AreaTree areaTree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = (String) getIntent().getSerializableExtra("name");
        areaTree = RetrofitUtil.getInstance().getCountryDetail(name);
        viewBinding = ActivityWorldDetailBinding.inflate(LayoutInflater.from(this));
        initGridView();
        initRecyclerView();
        initView();
        initStatusColor();
        setContentView(viewBinding.getRoot());
    }

    private void initView(){
        viewBinding.appBar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinding.appBar.toolbar.setTitle("国家及地区疫情详情");
    }

    private void initStatusColor(){
        Window window = getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
    }

    private void initGridView(){
        List<String> titleList = Arrays.asList(new String[]{"累计确诊", "现有确诊","死亡", "治愈"});
        List<Integer> numberList = new ArrayList<>();
        numberList.add(areaTree.getTotal().getConfirm());
        numberList.add(areaTree.getTotal().getConfirm() - areaTree.getTotal().getDead() - areaTree.getTotal().getHeal());
        numberList.add(areaTree.getTotal().getDead());
        numberList.add(areaTree.getTotal().getHeal());
        List<Integer> todayList = new ArrayList<>();
        todayList.add(areaTree.getToday().getConfirm());
        todayList.add(areaTree.getToday().getConfirm() - areaTree.getToday().getDead() - areaTree.getToday().getHeal());
        todayList.add(areaTree.getToday().getDead());
        todayList.add(areaTree.getToday().getHeal());
        NumberAdapter adapter = new NumberAdapter(titleList,numberList,todayList);
        viewBinding.gridDetailItem.setAdapter(adapter);
    }

    private void initRecyclerView(){
        if (areaTree.getChildren().size() == 0){
            viewBinding.regionRecyclerView.setVisibility(View.GONE);
            return;
        }
        viewBinding.noListText.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        viewBinding.regionRecyclerView.setLayoutManager(layoutManager);
        ProvinceAdapter adapter = new ProvinceAdapter(areaTree.getChildren(),false);
        viewBinding.regionRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        viewBinding.regionRecyclerView.setAdapter(adapter);
    }
}
