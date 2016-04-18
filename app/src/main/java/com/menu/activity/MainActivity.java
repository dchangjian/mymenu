package com.menu.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.menu.fragment.ClassifyFragment;
import com.menu.fragment.MineFragment;
import com.menu.fragment.ViewFragment;

public class MainActivity extends FragmentActivity {

	// 3个fragment
	private ViewFragment mViewFragment;
	private Fragment mClassifyFragment;
	private Fragment mMineFragment;
	
	private FragmentManager mFm;

	// tab栏控件
	private View mLayoutView;
	private View mLayoutClassify;
	private View mLayoutMine;
	private TextView mTvView;
	private ImageView mIvView;
	private TextView mTvClassify;
	private ImageView mIvClassify;
	private TextView mTvMine;
	private ImageView mIvMine;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();
	}

	/**
	 * 初始化布局
	 */
	private void initViews() {

		// fragment init
		
		 mFm = getSupportFragmentManager();
		
		//获得事务对象
		FragmentTransaction transaction = mFm.beginTransaction();

		mLayoutView = findViewById(R.id.layout_activity_main_view);
		mLayoutClassify = findViewById(R.id.layout_activity_main_classify);
		mLayoutMine = findViewById(R.id.layout_activity_main_mine);

		mTvView = (TextView) findViewById(R.id.tv_activity_main_view);
		mIvView = (ImageView) findViewById(R.id.iv_activity_main_view);

		mTvClassify = (TextView) findViewById(R.id.tv_activity_main_classify);
		mIvClassify = (ImageView) findViewById(R.id.iv_activity_main_classify);

		mTvMine = (TextView) findViewById(R.id.tv_activity_main_mine);
		mIvMine = (ImageView) findViewById(R.id.iv_activity_main_mine);
		
		//默认viewFragment被选择
		setSelected(0);
		mLayoutView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setSelected(0);
			}
		});

		mLayoutClassify.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setSelected(1);
			}
		});

		mLayoutMine.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setSelected(2);
			}
		});
	}

	// 设置被选中的状态
	public void setSelected(int index) {
		
		FragmentTransaction tr = mFm.beginTransaction();
		clearAll(tr);
		switch (index) {
		case 0:

			if(mViewFragment == null){
				mViewFragment =  new ViewFragment();
				tr.add(R.id.activity_main_container, mViewFragment);
			}
			else{
				tr.show(mViewFragment);
			}
			mIvView.setImageResource(R.drawable.home_menu_ico_view_active);
			mTvView.setTextColor(getResources().getColor(
					R.color.home_bottom_active));
			break;
		case 1:
			
			if(mClassifyFragment == null){
				mClassifyFragment =  new ClassifyFragment();
				tr.add(R.id.activity_main_container, mClassifyFragment);
			}
			
			else{
				tr.show(mClassifyFragment);
			}
			
			mIvClassify
					.setImageResource(R.drawable.home_menu_ico_classify_active);
			mTvClassify.setTextColor(getResources().getColor(
					R.color.home_bottom_active));
			break;
		case 2:
			
			if(mMineFragment == null){
				mMineFragment =  new MineFragment();
				tr.add(R.id.activity_main_container, mMineFragment);
			}
			
			else{
				tr.show(mMineFragment);
			}
			mIvMine.setImageResource(R.drawable.home_menu_ico_me_active);
			mTvMine.setTextColor(getResources().getColor(
					R.color.home_bottom_active));
			break;
		}
		
		tr.commit();
	}

	// 清除所有选中的状态
	private void clearAll(FragmentTransaction tr) {
		
		if(mViewFragment != null){
			tr.hide(mViewFragment);
		}
		if(mClassifyFragment != null){
			tr.hide(mClassifyFragment);
		}
		if(mMineFragment != null){
			tr.hide(mMineFragment);
		}
		
		mIvView.setImageResource(R.drawable.home_menu_ico_view);
		mTvView.setTextColor(getResources().getColor(R.color.home_bottom));
		mIvClassify.setImageResource(R.drawable.home_menu_ico_classify);
		mTvClassify.setTextColor(getResources().getColor(R.color.home_bottom));
		mIvMine.setImageResource(R.drawable.home_menu_ico_me);
		mTvMine.setTextColor(getResources().getColor(R.color.home_bottom));
	}

	public ViewFragment getViewFragment(){
		return mViewFragment;
	}

}
