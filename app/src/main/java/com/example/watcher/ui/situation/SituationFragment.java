package com.example.watcher.ui.situation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.watcher.databinding.FragmentSituationBinding;
import com.example.watcher.ui.situation.fragment.HomeFragment;
import com.example.watcher.ui.situation.fragment.WorldFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class SituationFragment extends Fragment {

    private FragmentSituationBinding viewBinding;
    private Fragment homeFragment;
    private Fragment worldFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentSituationBinding.inflate(inflater);
        homeFragment = new HomeFragment();
        worldFragment = new WorldFragment();
        initViewPager();
        initTabViewPager();
        return viewBinding.getRoot();
    }

    private void initViewPager(){
        viewBinding.viewPager.setAdapter(new FragmentStateAdapter(this) {
             @Override
             public int getItemCount() {
                 return 2;
             }

             @NonNull
             @Override
             public Fragment createFragment(int position) {
                 if (position == 0)
                     return homeFragment;
                 else
                     return worldFragment;
             }
         });
    }

    private void initTabViewPager() {
        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager,
                (tab, position) -> {
                if (position == 0)
                        tab.setText("国内疫情");
                    else
                        tab.setText("世界疫情");
                }
        ).attach();
    }
}
