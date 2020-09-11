package com.example.watcher.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.watcher.R;
import com.example.watcher.databinding.FragmentSettingBinding;
import com.example.watcher.ui.LoginActivity;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding viewBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentSettingBinding.inflate(inflater);
        initView();
        return viewBinding.getRoot();
    }

    private void initView(){
        viewBinding.logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }
}
