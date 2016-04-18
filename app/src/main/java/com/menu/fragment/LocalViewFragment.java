package com.menu.fragment;

import android.os.Bundle;

import com.menu.dao.MenuDao;
import com.menu.model.Menu;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/30 0030.
 */
public class LocalViewFragment extends MenuListFragment {
    private static final String TAG = "LocalViewFragment";
    private MenuDao mDao;
    private int pageNo = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDao = new MenuDao(this.getActivity());
    }

    @Override
    public ArrayList<Menu> getNextList() {
        return mDao.queryMenu(pageNo++, MenuListFragment.localSearchTag);
    }

    @Override
    public boolean hasMore() {
        return mDao.hasMore();
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
