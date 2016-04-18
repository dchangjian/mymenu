package com.menu.addmenu.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.activity.R.color;
import com.menu.activity.R.id;
import com.menu.activity.R.layout;
import com.menu.addmenu.adapter.SelectClassifyActivityAdapter;
import com.menu.addmenu.view.SelectClassifyLabelLayout;
import com.menu.model.Classify;

import data.ClassifyMap;

public class SelectClassifyActivity extends Activity{
	
	public static final int COMPLETED = 1;
	public static final int MAX_LABEL_COUNT = 5;
	private ListView mLvClassify;
	private GridView mGvClassify;
	/**
	 * 在最底部显示已选择分类的布局
	 */
	private LinearLayout mShowLabelLayout;
	
	/**
	 * 确定按钮
	 */
	private TextView mTvConfirm;
	private ArrayList<String> selectedClassifyLabelList;
	
	private final HashMap<String,View> classifyLeftLayoutMap = new HashMap<String,View>();
	private HashMap<String,Classify> classifyMap = ClassifyMap.getMap();
	
	private  Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {  
            if (msg.what == COMPLETED) {  
            	mLvClassify.getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
            	TextView tv = (TextView) mLvClassify.getChildAt(0).findViewById(R.id.tv_activity_select_classify_left);
				tv.setTextColor(getResources().getColor(R.color.home_bottom_active));
            	classifyLeftLayoutMap.put("lastActiveView", mLvClassify.getChildAt(0));  
            }  
        }  
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_classify);

		initViews();
	}

	private void initViews(){
		mLvClassify = (ListView) findViewById(R.id.lv_activity_select_classify);
		mGvClassify = (GridView) findViewById(R.id.gv_activity_select_classify);
		//去除gridView点击的时候的黄色边框
		mGvClassify.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		mShowLabelLayout = (LinearLayout) findViewById(R.id.layout_activity_select_classify_show_label);
		
		mTvConfirm = (TextView) findViewById(R.id.tv_activity_select_classify_confirm);
		
		//初始化数据
		ArrayList<String> classifies = new ArrayList<String>();
		classifies.add("主食");
		classifies.add("菜式");
		classifies.add("菜系");
		//默认第一个被选中，clone()处理引用问题
		@SuppressWarnings("unchecked")
		ArrayList<String> items = (ArrayList<String>) classifyMap.get("主食").getClassifyItem().clone();

		mGvClassify.setAdapter(new SelectClassifyActivityAdapter(SelectClassifyActivity.this, R.layout.activity_select_classify_item, items,R.id.tv_activity_select_classify_item));
		//命名不符合，因为重复使用代码
		mLvClassify.setAdapter(new SelectClassifyActivityAdapter(SelectClassifyActivity.this, R.layout.activity_select_classify_left_item, classifies,R.id.tv_activity_select_classify_left));
		mLvClassify.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mLvClassify.setItemChecked(0,true);
		
		Intent intent = getIntent();
		
		//从MenuAddActivity中获取数据
		selectedClassifyLabelList = intent.getStringArrayListExtra("classifyLabel");
		if(selectedClassifyLabelList!= null){
			for(int i = 0;  i< selectedClassifyLabelList.size();i++){
				String text = selectedClassifyLabelList.get(i);
				SelectClassifyLabelLayout v = new SelectClassifyLabelLayout(this, selectedClassifyLabelList, text);
				LayoutParams params = new LayoutParams(Gravity.CENTER_VERTICAL);
				mShowLabelLayout.addView(v,params);
			}
		}
		else{
			selectedClassifyLabelList = new ArrayList<String>();
		}
		
		Timer timer = new Timer();
		//用异步的当时初始化第一条数据
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				while(true){
					if(mLvClassify.getChildAt(0) != null){
						Message msg = new Message();
						msg.what = COMPLETED;
						mHandler.sendMessage(msg);
						this.cancel();
						break;
					}
				}
			}
		}, new Date());
		
		mLvClassify.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(classifyLeftLayoutMap.get("lastActiveView") == null){
					classifyLeftLayoutMap.put("lastActiveView", view);
				}
				else{
					View lastActiveView = classifyLeftLayoutMap.get("lastActiveView");
					lastActiveView.setBackgroundColor(getResources().getColor(R.color.top_bar_background));
					classifyLeftLayoutMap.put("lastActiveView", view);
					TextView tv = (TextView) lastActiveView.findViewById(R.id.tv_activity_select_classify_left);
					tv.setTextColor(Color.parseColor("#000000"));
				}
				
				TextView tv = (TextView) view.findViewById(R.id.tv_activity_select_classify_left);
				tv.setTextColor(getResources().getColor(R.color.home_bottom_active));
				String text = (String) tv.getText();
				
				((SelectClassifyActivityAdapter)mGvClassify.getAdapter()).clear();
				((SelectClassifyActivityAdapter)mGvClassify.getAdapter()).addAll(classifyMap.get(text).getClassifyItem());
				view.setBackgroundColor(Color.parseColor("#ffffff"));
			}
		});
		
		mGvClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
				
				if(selectedClassifyLabelList.size() > MAX_LABEL_COUNT){
					return;
				}
				
				TextView tv = (TextView) view.findViewById(R.id.tv_activity_select_classify_item);
				String text = (String) tv.getText();
				
				for(String s : selectedClassifyLabelList){
					if(s.equals(text)){
						return;
					}
				}
				
				View v =  new SelectClassifyLabelLayout(SelectClassifyActivity.this,selectedClassifyLabelList,text);
				LayoutParams params = new LayoutParams(Gravity.CENTER_VERTICAL);
				selectedClassifyLabelList.add(text);
				mShowLabelLayout.addView(v,params);
				
			}
		});
		
		mTvConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra("classifyLabel", selectedClassifyLabelList);
				setResult(RESULT_OK,i);
				finish();
			}
		});
	}
}
		

