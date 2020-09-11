package com.example.watcher.data;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class XAxisValueFormatter extends ValueFormatter{

    private final List<String> mLabels;
    public XAxisValueFormatter(List<String> labels) {
        mLabels = labels;
    }

    @Override
    public String getFormattedValue(float value) {
        if ((int)value < mLabels.size())
            return mLabels.get((int)value);
        return "";
    }
}