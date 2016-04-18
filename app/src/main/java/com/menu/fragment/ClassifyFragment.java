package com.menu.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.menu.activity.MainActivity;
import com.menu.activity.R;
import com.menu.addmenu.adapter.SelectClassifyActivityAdapter;
import com.menu.model.Classify;
import com.menu.util.Util;
import com.menu.viewmenu.activity.SearchActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import data.ClassifyMap;

public class ClassifyFragment extends Fragment {

	public static final int COMPLETED = 1;
	private ListView mLv;
	private GridView mGv;
	private EditText mTxtSearch;

	private ArrayList<String> selectedClassifyLabelList;
	private final HashMap<String,View> classifyLeftLayoutMap = new HashMap<String,View>();

	private HashMap<String,Classify> classifyMap = ClassifyMap.getMap();

	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if (msg.what == COMPLETED) {
				mLv.getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
				TextView tv = (TextView) mLv.getChildAt(0).findViewById(R.id.tv_activity_select_classify_left);
				tv.setTextColor(getResources().getColor(R.color.home_bottom_active));
				classifyLeftLayoutMap.put("lastActiveView", mLv.getChildAt(0));
			}
		}
	};

	public ClassifyFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main_classify, container,false);

		initViews(v);
		return v;
	}

	private void initViews(View v ){
		mLv = (ListView) v.findViewById(R.id.lv_fragment_select_classify);
		mGv = (GridView) v.findViewById(R.id.gv_fragment_select_classify);

		mTxtSearch = (EditText) v.findViewById(R.id.tv_fragment_main_classify_search_menu);
		//去除gridView点击的时候的黄色边框
		mGv.setSelector(new ColorDrawable(Color.TRANSPARENT));


		//初始化数据
		ArrayList<String> classifies = new ArrayList<String>();
		classifies.add("主食");
		classifies.add("菜式");
		classifies.add("菜系");
		//默认第一个被选中，clone()处理引用问题
		@SuppressWarnings("unchecked")
		ArrayList<String> items = (ArrayList<String>) classifyMap.get("主食").getClassifyItem().clone();

		mGv.setAdapter(new SelectClassifyActivityAdapter(ClassifyFragment.this.getActivity(), R.layout.activity_select_classify_item, items,R.id.tv_activity_select_classify_item));
		//命名不符合，因为重复使用代码
		mLv.setAdapter(new SelectClassifyActivityAdapter(ClassifyFragment.this.getActivity(), R.layout.activity_select_classify_left_item, classifies,R.id.tv_activity_select_classify_left));
		mLv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mLv.setItemChecked(0,true);


		Timer timer = new Timer();
		//用异步的当时初始化第一条数据
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				while(true){
					if(mLv.getChildAt(0) != null){
						Message msg = new Message();
						msg.what = COMPLETED;
						mHandler.sendMessage(msg);
						this.cancel();
						break;
					}
				}
			}
		}, new Date());

		mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				if (classifyLeftLayoutMap.get("lastActiveView") == null) {
					classifyLeftLayoutMap.put("lastActiveView", view);
				} else {
					View lastActiveView = classifyLeftLayoutMap.get("lastActiveView");
					lastActiveView.setBackgroundColor(Color.parseColor("#cccccc"));
					classifyLeftLayoutMap.put("lastActiveView", view);
					TextView tv = (TextView) lastActiveView.findViewById(R.id.tv_activity_select_classify_left);
					tv.setTextColor(Color.parseColor("#000000"));
				}

				TextView tv = (TextView) view.findViewById(R.id.tv_activity_select_classify_left);
				tv.setTextColor(getResources().getColor(R.color.home_bottom_active));
				String text = (String) tv.getText();

				((SelectClassifyActivityAdapter) mGv.getAdapter()).clear();
				((SelectClassifyActivityAdapter) mGv.getAdapter()).addAll(classifyMap.get(text).getClassifyItem());
				view.setBackgroundColor(Color.parseColor("#ffffff"));
			}
		});

		mTxtSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ClassifyFragment.this.getActivity(), SearchActivity.class);
				startActivity(i);
			}
		});

		mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {

				String keyWord = (String) mGv.getAdapter().getItem(position);
				Util.showToast(ClassifyFragment.this.getActivity(),keyWord);
				//处理选中逻辑
				try {
					MenuListFragment.localSearchTag = keyWord;
					MenuListFragment.netSearchTag = URLEncoder.encode(keyWord,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				MainActivity mainActivity = (MainActivity) getActivity();

				mainActivity.getViewFragment().clearAll();
				mainActivity.setSelected(0);

//				Intent i = new Intent(ClassifyFragment.this.getActivity(),MainActivity.class);
//				startActivity(i);
//				ClassifyFragment.this.getActivity().finish();

			}
		});
	}
}
