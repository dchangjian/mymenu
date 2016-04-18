package com.menu.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.model.Menu;
import com.menu.util.Util;
import com.menu.viewmenu.activity.ViewMenuActivity;
import com.menu.viewmenu.adapter.ViewAdapter;
import com.menu.viewmenu.view.EmptyView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * 主界面查看的Fragment
 *
 * @author Administrator
 */
public abstract class MenuListFragment extends Fragment {

    public static final int LOAD_MORE_FINISH = 1;
    private ListView mListView;
    protected ViewAdapter mAdapter;
    protected ArrayList<Menu> mLists = new ArrayList<Menu>();
    protected ArrayList<Menu> nextList = new ArrayList<>();
    public static String localSearchTag = "土豆丝";
    public static String netSearchTag = "%e5%9c%9f%e8%b1%86%e4%b8%9d";
    protected PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;

    private View contentView;
    private View emptyView;

    private BaseHandler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //千万不能直接初始化
        mLists = new ArrayList<Menu>();
        mHandler = new BaseHandler(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_show_menu_list, container, false);

        emptyView = inflater.inflate(R.layout.empyt_content, container, false);

        //设置ListView
        mListView = (ListView) v.findViewById(R.id.lv_fragment_view_menu);
        //静止第一条和最后一条分割线的显示
        mListView.setHeaderDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);
        mListView.setDividerHeight(Util.dip2px(this.getActivity(), 1));
        // 为listview的创建一个headerview,注意，如果不加会影响到加载的footview的显示！
        View headerMarginView = new View(this.getActivity());
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(20)));
        mListView.addHeaderView(headerMarginView);
        mAdapter = new ViewAdapter(this.getActivity(), 0, mLists);
        mListView.setAdapter(mAdapter);

        contentView = mListView;

        //设置下拉刷新组件和事件监听
        mPtrFrameLayout = (PtrFrameLayout) v.findViewById(R.id.load_more_list_view_ptr_frame);


        //header
//        final MaterialHeader header = new MaterialHeader(this);
//        int[] colors = getResources().getIntArray(R.array.google_colors);
//        header.setColorSchemeColors(colors);
//        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-2, -3));
//        header.setPadding(0, px2dip(this,30), 0, px2dip(this,30));
//        header.setPtrFrameLayout(mPtrFrameLayout);

        // set header
        final StoreHouseHeader header = new StoreHouseHeader(this.getActivity());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-2, -3));
        header.setPadding(0, Util.dip2px(this.getActivity(), 15), 0, Util.dip2px(this.getActivity(), 15));
        header.initWithString("MY-MENU");

        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.setLoadingMinTime(1000);

        //set loadmore container
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) v.findViewById(R.id.load_more_list_view_container);
        mLoadMoreListViewContainer.setAutoLoadMore(true);//设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // here check list view, not content.
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, contentView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //实现下拉刷新的功能
                Log.i("test", "-----onRefreshBegin-----");
                mPtrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        new Thread(task).start();
                        /*

                         nextList= reptile.getMenuList("%E5%9C%9F%E8%B1%86%E4%B8%9D", pageNo++);
                        nextList = mDao.queryMenu(++pageNo);
                         mLists.addAll(nextList);
                         mPtrFrameLayout.refreshComplete();//第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        mLoadMoreListViewContainer.loadMoreFinish(false, true);
                        mAdapter.notifyDataSetChanged();
                        */
                    }
                }, 0);
            }
        });

        //设置延时自动刷新数据
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(false);
            }
        }, 0);


        //4.加载更多的组件

        //5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                //模拟加载更多的业务处理
                mLoadMoreListViewContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(task).start();
                        /*
                        nextList= reptile.getMenuList("%E5%9C%9F%E8%B1%86%E4%B8%9D", pageNo++);
                        //nextList = mDao.queryMenu(++pageNo);
                        mLists.addAll(nextList);
                        mLoadMoreListViewContainer.loadMoreFinish(false, true);

                        mAdapter.notifyDataSetChanged();*/
                    }
                }, 0);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解决索引问题
                Menu menu = (Menu) parent.getAdapter().getItem(position);
                //Menu menu = mLists.get(position);
                Intent i = new Intent(MenuListFragment.this.getActivity(), ViewMenuActivity.class);
                int difference = 0;
                //如果是本地查看
                if (MenuListFragment.this instanceof LocalViewFragment) {
                    difference = ViewMenuActivity.VIEW_LOCAL;
                } else {
                    difference = ViewMenuActivity.VIEW_NET;
                }
                i.putExtra("menu", menu);
                i.putExtra("difference", difference);

                startActivity(i);
            }
        });

        return v;
    }

    Runnable task = new Runnable() {

        @Override
        public void run() {
            // 在这里进行 http request.网络请求相关操作
            Message msg = new Message();

            nextList = getNextList();
            mHandler.sendMessage(msg);
        }
    };

    class BaseHandler extends Handler{

        protected WeakReference<Activity> activityReference;
        public BaseHandler(Activity activity) {
            activityReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            final Activity activity = activityReference.get();

            if (activity == null) {
                return;
            }
            if(mPtrFrameLayout.isRefreshing()){
                mPtrFrameLayout.refreshComplete();
            }

            if (mLists != null) {
                mLists.addAll(nextList);
                mAdapter.notifyDataSetChanged();
                mLoadMoreListViewContainer.loadMoreFinish(false, hasMore());
            }

            //空数据
            if(mLists.size() == 0) {
                ViewGroup parentView = (ViewGroup) mListView.getParent();
                parentView.removeView(emptyView);
                parentView.addView(emptyView, 1); // 你需要在这儿设置正确的位置，以达到你需要的效果。
                mListView.setEmptyView(emptyView);
            }

            if(hasMore() && nextList.size() == 0) {
                mLoadMoreListViewContainer.loadMoreError(0, "加载失败");
            }
        }
    }

    /**
     * 获取下一条数据，因为实现方法不同，所以抽象
     *
     * @return
     */
    protected abstract ArrayList<Menu> getNextList();

    /**
     * 判断是否有更多
     *
     * @return
     */
    protected abstract boolean hasMore();

}
