package com.example.skylink.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.skylink.R;
import com.example.skylink.adapter.MainViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar mainToolbar;
    private ViewPager2 viewPager2;
    private MainViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.main_view_pager);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        if (getSupportActionBar() != null){
            System.out.println("made it here");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        adapter = new MainViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                updateToolbarTitle(position);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_fragment_home){
                    viewPager2.setCurrentItem(0, true);
                } else if (menuItem.getItemId() == R.id.nav_fragment_bookings){
                    viewPager2.setCurrentItem(1, true);
                } else if (menuItem.getItemId() == R.id.nav_fragment_profile){
                    viewPager2.setCurrentItem(2, true);
                } else if (menuItem.getItemId() == R.id.nav_fragment_more){
                    viewPager2.setCurrentItem(3, true);
                }
                return true;
            }
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (viewPager2.getCurrentItem() != 0) {
                    viewPager2.setCurrentItem(0, true);
                } else {
                    finish();
                }
            }
        });
    }

    private void updateToolbarTitle(int position){
        String newTitle;
        switch (position) {
            case 1:
                newTitle = getString(R.string.bottom_nav_bookings);
                break;
            case 2:
                newTitle = getString(R.string.bottom_nav_profile);
                break;
            case 3:
                newTitle = getString(R.string.bottom_nav_more);
                break;
            default:
                newTitle = getString(R.string.app_name);
                break;
        }
        TextView textView = findViewById(R.id.main_toolbar_title);
        if (textView != null){
            textView.setText(newTitle);
        }
    }
}