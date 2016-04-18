package com.menu.fragment;

import android.os.Bundle;
import android.view.View;

import com.menu.model.Menu;
import com.menu.reptile.Reptile;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/30 0030.
 */
public class NetViewFragment extends MenuListFragment{
    private int pageNo = 1;
    private Reptile reptile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reptile = new Reptile();
    }

    @Override
    public ArrayList<Menu> getNextList() {
        return  reptile.getMenuList(MenuListFragment.netSearchTag, pageNo++);
    }

    @Override
    public boolean hasMore() {
        return true;
    }


    public void clearAll(){
        if(super.mLists != null){
            super.mLists.clear();
        }
        pageNo = 1;
        super.mAdapter.clear();
        super.nextList.clear();
        super.mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(true);
            }
        },300);
    }
}
