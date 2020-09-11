package com.example.watcher.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.watcher.R;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.databinding.MyMarkerViewBinding;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class MyMarkerView extends MarkerView {

    private TextView title;
    private TextView data;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        title = findViewById(R.id.marker_date);
        data = findViewById(R.id.marker_number);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        try {
            data.setText("数据："+(int)e.getY());
            title.setText(RetrofitUtil.getInstance().getChinaDaysDate().get((int)e.getX()));
        }catch (Exception e1){
            e1.printStackTrace();
        }
        super.refreshContent(e,highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth()/2),-getHeight());
    }

}
