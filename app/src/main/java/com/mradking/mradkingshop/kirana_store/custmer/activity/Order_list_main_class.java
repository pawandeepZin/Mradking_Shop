package com.mradking.mradkingshop.kirana_store.custmer.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mradking.mradkingshop.R;
import com.mradking.mradkingshop.kirana_store.custmer.Fragment.Delivered_list_fragment;
import com.mradking.mradkingshop.kirana_store.custmer.Fragment.Orderlist_frament;

import java.util.ArrayList;
import java.util.List;

public class Order_list_main_class  extends AppCompatActivity {
    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;

    private ViewPager viewPager;
    String key;
    public String myString = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kirana_order_main_list);



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    public String getMyData() {
        return myString;
    }

    private void setupViewPager(ViewPager viewPager) {




            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


            adapter.addFragment(new Orderlist_frament(),  "Ordered");
            adapter.addFragment(new Delivered_list_fragment(),  "Delivered");


            viewPager.setOffscreenPageLimit(1);
            viewPager.setAdapter(adapter);








    }
}




class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private final List<String> mFragmentTitleList = new ArrayList<String>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {


        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {



        return mFragmentTitleList.get(position);
    }




}


