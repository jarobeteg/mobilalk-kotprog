package com.example.skylink.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.skylink.ui.fragment.BookingsFragment;
import com.example.skylink.ui.fragment.HomeFragment;
import com.example.skylink.ui.fragment.MoreFragment;
import com.example.skylink.ui.fragment.ProfileFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new BookingsFragment();
            case 2:
                return new ProfileFragment();
            case 3:
                return new MoreFragment();
            default:
                return new HomeFragment();
        }
    }
}
