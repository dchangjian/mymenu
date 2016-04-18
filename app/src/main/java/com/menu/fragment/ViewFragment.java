package com.menu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.menu.activity.R;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class ViewFragment extends Fragment {


    private LocalViewFragment mViewLocalFragment;
    private NetViewFragment mViewNetFragment;
    private RadioButton netButton;
    private RadioButton localButton;
    //带条件的显示
    protected String tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_view, container, false);

        final ViewPager pager = (ViewPager) v.findViewById(R.id.pager);
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        MyAdapter pageAdapter = new MyAdapter(fm);
        pager.setAdapter(pageAdapter);
        //默认显示网络加载
        pager.setCurrentItem(0);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    netButton.setChecked(true);
                } else if (position == 1) {
                    localButton.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        SegmentedGroup group = (SegmentedGroup) v.findViewById(R.id.segmented_group);

        netButton = (RadioButton) v.findViewById(R.id.btn_fragment_view_menu_net);
        localButton = (RadioButton) v.findViewById(R.id.btn_fragment_view_menu_local);

        netButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消平滑移动的效果
                pager.setCurrentItem(0, false);
            }
        });

        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1, false);
            }
        });

        return v;
    }


    class MyAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"网络", "本地"};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if (mViewNetFragment == null) {
                        mViewNetFragment = new NetViewFragment();
                    }

                    return mViewNetFragment;

                case 1:
                    if (mViewLocalFragment == null) {
                        mViewLocalFragment = new LocalViewFragment();
                    }

                    return mViewLocalFragment;
                default:
                    return null;
            }

        }
    }
    public void clearAll(){
        mViewNetFragment.clearAll();
        mViewLocalFragment.clearAll();
    }

}
