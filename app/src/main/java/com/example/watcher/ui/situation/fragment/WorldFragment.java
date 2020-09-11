package com.example.watcher.ui.situation.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.watcher.R;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.adapter.CountryAdapter;
import com.example.watcher.adapter.NumberAdapter;
import com.example.watcher.data.AreaTree;
import com.example.watcher.data.XAxisValueFormatter;
import com.example.watcher.databinding.ItemFragmentWorldBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldFragment extends Fragment {

    private ItemFragmentWorldBinding viewBinding;
    public static Handler mHandler;
    private List<AreaTree> countryList = new ArrayList<>();
    private int countryPage = 0;
    private List<Integer> colors = Arrays.asList(new Integer[]{Color.parseColor("#990000"), Color.parseColor("#009933"),Color.parseColor("#696969")});
    private List<String> titleList = Arrays.asList(new String[]{"累计确诊", "现有确诊", "治愈","死亡"});
    private List<Integer> numberList = new ArrayList<>();
    private List<Integer> todayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewBinding = ItemFragmentWorldBinding.inflate(inflater);
        initHandler();
        initData();
        initView();
        return viewBinding.getRoot();
    }

    private void initView(){
        viewBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                // 比如发送文本形式的数据内容
                // 指定发送的内容
                StringBuilder text = new StringBuilder("全世界疫情数据如下\n");
                for (int i = 0;i < numberList.size();i++){
                    text.append(titleList.get(i)).append(":").append(numberList.get(i).toString());
                    if (todayList.get(i) >= 0){
                        text.append(" 较昨日+").append(todayList.get(i)).append("\n");
                    }else {
                        text.append(" 较昨日").append(todayList.get(i)).append("\n");
                    }
                }
                sendIntent.putExtra(Intent.EXTRA_TEXT, (Serializable) text);
                // 指定发送内容的类型
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share to..."));
            }
        });
    }

    private void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
                    initGridView();
                    initBarChart();
                    viewBinding.worldMap.setMapResId(R.raw.world_map);
                    initRecyclerView();
                    viewBinding.progressBar.setVisibility(View.GONE);
                }else if (msg.what == 2){
                    addCountries();
                    viewBinding.countryRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        };
    }

    private void initGridView(){
        numberList = RetrofitUtil.getInstance().getWorldTotalData();
        todayList = RetrofitUtil.getInstance().getWorldTodayData();
        NumberAdapter adapter = new NumberAdapter(titleList,numberList,todayList);
        viewBinding.gridWorld.setAdapter(adapter);
    }

    //初始化表格
    private void initBarChart(){
        XAxis xAxis = viewBinding.barChartWorld.getXAxis();
        XAxisValueFormatter labelFormatter = new XAxisValueFormatter(RetrofitUtil.getInstance().getCountriesName());
        xAxis.setValueFormatter(labelFormatter);

        BarDataSet barDataSetConfirm=new BarDataSet(RetrofitUtil.getInstance().getCountriesConfirm(),"累计确诊");
        barDataSetConfirm.setColor(colors.get(0));
        BarDataSet barDataSetHeal = new BarDataSet(RetrofitUtil.getInstance().getCountriesHeal(),"治愈");
        barDataSetHeal.setColor(colors.get(1));
        BarDataSet barDataSetDead = new BarDataSet(RetrofitUtil.getInstance().getCountriesDead(),"死亡");
        barDataSetDead.setColor(colors.get(2));
        BarData barData=new BarData(barDataSetConfirm);
        barData.addDataSet(barDataSetHeal);
        barData.addDataSet(barDataSetDead);
        //barData.addDataSet(lineDataSetConfirmLeft);
        viewBinding.barChartWorld.setData(barData);
        viewBinding.barChartWorld.animateY(3000);
        Description description = viewBinding.barChartWorld.getDescription();
        description.setText("按现有确诊排序  数据来源：网易");
    }

    private void initRecyclerView(){
        addCountries();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        viewBinding.countryRecyclerView.setLayoutManager(layoutManager);
        CountryAdapter adapter = new CountryAdapter(countryList);
        viewBinding.countryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        viewBinding.countryRecyclerView.setAdapter(adapter);
    }

    private void initData(){
        new Thread(){
            @Override
            public void run() {
                try {
                    while (!RetrofitUtil.getInstance().receiveFlag) {
                        Thread.sleep(500);
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void addCountries(){
        List<AreaTree> list = RetrofitUtil.getInstance().getCountriesData(countryPage);
        if (list.size() == 0)
            return;
        countryList.addAll(list);
        countryPage++;
    }
}