package com.example.watcher.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.watcher.R;
import com.example.watcher.databinding.ActivityHomeDetailBinding;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.adapter.ItemDetailAdapter;
import com.example.watcher.adapter.NumberAdapter;
import com.example.watcher.adapter.ProvinceAdapter;
import com.example.watcher.data.Children;
import com.example.watcher.data.ChildrenX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailHomeActivity extends AppCompatActivity {

    private Children province;
    private ActivityHomeDetailBinding viewBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityHomeDetailBinding.inflate(LayoutInflater.from(this));
        String provinceName = (String) getIntent().getSerializableExtra("name");
        province = RetrofitUtil.getInstance().getProvinceDetail(provinceName);
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
        viewBinding.appBar.toolbar.setTitle("省份疫情详情");
    }

    private void initGridView(){
        List<String> titleList = Arrays.asList(new String[]{"累计确诊", "现有确诊", "境外输入确诊", "疑似", "死亡", "治愈"});
        List<Integer> numberList = new ArrayList<>();
        numberList.add(province.getTotal().getConfirm());
        numberList.add(province.getTotal().getConfirm() - province.getTotal().getDead() - province.getTotal().getHeal());
        numberList.add(province.getTotal().getInput());
        numberList.add(province.getTotal().getSuspect());
        numberList.add(province.getTotal().getDead());
        numberList.add(province.getTotal().getHeal());
        List<Integer> todayList = new ArrayList<>();
        todayList.add(province.getToday().getConfirm());
        todayList.add(province.getToday().getConfirm() - province.getToday().getDead() - province.getToday().getHeal());
        todayList.add(province.getToday().getInput());
        todayList.add(null);
        todayList.add(province.getToday().getDead());
        todayList.add(province.getToday().getHeal());
        NumberAdapter adapter = new NumberAdapter(titleList,numberList,todayList);
        viewBinding.gridDetailItem.setAdapter(adapter);
    }

    private void initRecyclerView(){
        if (province.getChildren().size() == 0){
            viewBinding.regionRecyclerView.setVisibility(View.GONE);
            return;
        }
        viewBinding.noListText.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        viewBinding.regionRecyclerView.setLayoutManager(layoutManager);
        ItemDetailAdapter adapter = new ItemDetailAdapter(province.getChildren());
        viewBinding.regionRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        viewBinding.regionRecyclerView.setAdapter(adapter);
    }

    private void initStatusColor(){
        Window window = getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
    }
}
