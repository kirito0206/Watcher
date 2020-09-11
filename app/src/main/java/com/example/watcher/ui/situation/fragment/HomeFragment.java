package com.example.watcher.ui.situation.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.watcher.R;
import com.example.watcher.data.MyMarkerView;
import com.example.watcher.databinding.ItemFragmentHomeBinding;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.adapter.NumberAdapter;
import com.example.watcher.adapter.ProvinceAdapter;
import com.example.watcher.data.XAxisValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements OnChartValueSelectedListener{

    private ItemFragmentHomeBinding viewBinding;
    public static Handler mHandler;
    private List<Integer> colors = Arrays.asList(new Integer[]{Color.parseColor("#330000"), Color.parseColor("#009933"),Color.parseColor("#696969"),Color.parseColor("#990000")});
    private List<String> titleList = Arrays.asList(new String[]{"累计确诊", "现有确诊", "境外输入确诊", "疑似", "死亡", "治愈"});
    private List<Integer> numberList = new ArrayList<>();
    private List<Integer> todayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewBinding = ItemFragmentHomeBinding.inflate(inflater);
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
                    viewBinding.chinaMap.setMapResId(R.raw.china_map);
                    initLineChart();
                    initRecyclerView();
                    viewBinding.progressBar.setVisibility(View.GONE);
                }
            }
        };
    }

    private void initGridView(){
        numberList = RetrofitUtil.getInstance().getChinaTotalData();
        todayList = RetrofitUtil.getInstance().getChinaTodayData();
        NumberAdapter adapter = new NumberAdapter(titleList,numberList,todayList);
        viewBinding.gridHome.setAdapter(adapter);
    }

    //初始化表格
    private void initLineChart(){
        XAxis xAxis = viewBinding.lineChartHome.getXAxis();
        viewBinding.lineChartHome.setScaleEnabled(true);
        MyMarkerView myMarkerView = new MyMarkerView(getActivity(),R.layout.my_marker_view);
        myMarkerView.setChartView(viewBinding.lineChartHome);
        viewBinding.lineChartHome.setMarkerView(myMarkerView);
        XAxisValueFormatter labelFormatter = new XAxisValueFormatter(RetrofitUtil.getInstance().getChinaDaysDate());
        xAxis.setLabelRotationAngle(20);
        xAxis.setValueFormatter(labelFormatter);

        LineDataSet lineDataSetConfirm=new LineDataSet(RetrofitUtil.getInstance().getChinaDaysConfirm(),"累计确诊");
        lineDataSetConfirm.setDrawCircles(false);
        lineDataSetConfirm.setColor(colors.get(0));
        lineDataSetConfirm.setLineWidth(3f);
        LineDataSet lineDataSetHeal = new LineDataSet(RetrofitUtil.getInstance().getChinaDaysHeal(),"治愈");
        lineDataSetHeal.setDrawCircles(false);
        lineDataSetHeal.setColor(colors.get(1));
        lineDataSetHeal.setLineWidth(3f);
        LineDataSet lineDataSetDead = new LineDataSet(RetrofitUtil.getInstance().getChinaDaysDead(),"死亡");
        lineDataSetDead.setDrawCircles(false);
        lineDataSetDead.setColor(colors.get(2));
        lineDataSetDead.setLineWidth(3f);
        LineDataSet lineDataSetConfirmLeft = new LineDataSet(RetrofitUtil.getInstance().getChinaDaysConfirmLeft(),"现有确诊");
        lineDataSetConfirmLeft.setDrawCircles(false);
        lineDataSetConfirmLeft.setColor(colors.get(3));
        lineDataSetConfirmLeft.setLineWidth(3f);
        LineData lineData=new LineData(lineDataSetConfirm);
        lineData.addDataSet(lineDataSetHeal);
        lineData.addDataSet(lineDataSetDead);
        lineData.addDataSet(lineDataSetConfirmLeft);
        lineData.setHighlightEnabled(true);

        viewBinding.lineChartHome.setData(lineData);
        viewBinding.lineChartHome.animateX(3000);
        //更改右下角描述
        Description description = viewBinding.lineChartHome.getDescription();
        description.setText("数据来源：网易");
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        viewBinding.provinceRecyclerView.setLayoutManager(layoutManager);
        ProvinceAdapter adapter = new ProvinceAdapter(RetrofitUtil.getInstance().getProvinceList(),true);
        viewBinding.provinceRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        viewBinding.provinceRecyclerView.setAdapter(adapter);
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


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        float touchY = h.getDrawY();//手指接触点在srcChart上的Y坐标，即手势监听器中保存数据
        float y = h.getY();
        Highlight hl = new Highlight(h.getX(), Float.NaN, h.getDataSetIndex());
        hl.setDraw(h.getX(), y);
        viewBinding.lineChartHome.highlightValues(new Highlight[]{hl});
    }

    @Override
    public void onNothingSelected() {

    }
}