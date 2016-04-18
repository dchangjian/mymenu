package com.menu.addmenu.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.addmenu.view.AddMenuItemLayout;
import com.menu.addmenu.view.SelectClassifyLabelLayout;
import com.menu.addmenu.view.SelectClassifyLabelLayout.Listener;
import com.menu.addmenu.view.SelectUnitDialog;
import com.menu.model.Ingredient;
import com.menu.model.Menu;
import com.menu.util.Util;

public class MenuAddActivity extends FragmentActivity implements
		OnClickListener {

	public static final String TAG = "MenuAddActivity";
	public static final int SELECT_CLASSIFY_CODE = 1;
	public static final int INGREDIENT_COUNT = 10;
	/**
	 * 界面回退按钮
	 */
	private ImageView mIvBack;

	/**
	 * 添加某一项主料配料的icon
	 */
	private ImageView mIvAddMenuMainItem;
	/**
	 * 添加某一项辅料配料的icon
	 */
	private ImageView mIvaddMenuAssistantItem;
	/**
	 * 添加主料的布局
	 */
	private LinearLayout mLayoutAddMainIngredient;
	/**
	 * 添加辅料的布局
	 */
	private LinearLayout mLayoutAddAssistantIngredient;
	/**
	 * 数量不能超过10个
	 */
	public int mainIngredientCount;

	public int assistantIngredientCount;
	/**
	 * 保存主料每一项的布局
	 */

	/**
	 * 制作时间icon
	 */
	private ImageView mIvMakeTime;
	/**
	 * 制作时间textView
	 */
	private TextView mTvMakeTime;

	/**
	 * 菜谱简介textView
	 */
	private EditText mTxtProfile;
	/**
	 * 下一步按钮
	 */
	private TextView mTvNextStep;

	/**
	 * 菜谱名称EditText
	 */
	private EditText mTxtMenuName;
	private GridLayout mShowClassifyLayout;
	/**
	 * 已经选择好的分类标签
	 */
	private ArrayList<String> selectedClassifyItemList;
	private RelativeLayout mLayoutSelectClassify;

	private ArrayList<AddMenuItemLayout> mAddMenuMainItemLayoutList = new ArrayList<AddMenuItemLayout>();
	private ArrayList<AddMenuItemLayout> mAddMenuAssistantItemLayoutList = new ArrayList<AddMenuItemLayout>();

	// 从MenuAddStepsActivity返回的Menu数据
	private Menu mMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_memu);
		initData();
		initViews();
	}

	private void initData() {
		Intent intent = getIntent();
		mMenu = (Menu) intent.getSerializableExtra("menu");
	}

	private void initViews() {
		mIvBack = (ImageView) findViewById(R.id.iv_activity_add_menu_back);
		mLayoutAddMainIngredient = (LinearLayout) findViewById(R.id.layout_activity_add_menu_add_main_ingredient);
		mLayoutAddAssistantIngredient = (LinearLayout) findViewById(R.id.layout_activity_add_menu_add_assistant_ingredient);
		mIvAddMenuMainItem = (ImageView) findViewById(R.id.iv_activity_add_menu_add_main_item);
		mIvaddMenuAssistantItem = (ImageView) findViewById(R.id.iv_activity_add_menu_add_assistant_item);

		mTxtMenuName = (EditText) findViewById(R.id.txt_activity_add_menu__name);
		mIvMakeTime = (ImageView) findViewById(R.id.iv_activity_add_menu_make_time);
		mTvMakeTime = (TextView) findViewById(R.id.tv_activity_add_menu_make_time);

		mTxtProfile = (EditText) findViewById(R.id.txt_activity_add_menu_profile);

		mTvNextStep = (TextView) findViewById(R.id.tv_activity_add_menu_next_step);
		mLayoutSelectClassify = (RelativeLayout) findViewById(R.id.layout_activity_add_menu_select_classify);

		mShowClassifyLayout = (GridLayout) findViewById(R.id.layout_add_menu_showSelected_classify);
		// 主料布局
		// 默认的第一项，不能删除，必须至少有一个主材料
		AddMenuItemLayout firstMainChild = new AddMenuItemLayout(
				MenuAddActivity.this, mAddMenuMainItemLayoutList);
		// 隐藏删除图标
		firstMainChild.findViewById(R.id.iv_activity_add_menu_del_item)
				.setVisibility(View.GONE);
		// 添加到布局中
		mLayoutAddMainIngredient.addView(firstMainChild);
		// 将第一个(默认)添加到布局集合中
		mAddMenuMainItemLayoutList.add(firstMainChild);

		// 辅料布局
		AddMenuItemLayout firstAssistantChild = new AddMenuItemLayout(
				MenuAddActivity.this, mAddMenuAssistantItemLayoutList);
		firstAssistantChild.findViewById(R.id.iv_activity_add_menu_del_item)
				.setVisibility(View.GONE);
		mLayoutAddAssistantIngredient.addView(firstAssistantChild);
		mAddMenuAssistantItemLayoutList.add(firstAssistantChild);

		mIvAddMenuMainItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 因为本来就有
				if (mAddMenuMainItemLayoutList.size() + 1 > INGREDIENT_COUNT) {
					Util.showToast(MenuAddActivity.this, "主料数量不能超过10");
					return;
				}
				// 将布局存放在list中
				AddMenuItemLayout child = new AddMenuItemLayout(
						MenuAddActivity.this, mAddMenuMainItemLayoutList);
				mAddMenuMainItemLayoutList.add(child);
				mLayoutAddMainIngredient.addView(child);
			}
		});
		mIvaddMenuAssistantItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mAddMenuAssistantItemLayoutList.size() + 1 > INGREDIENT_COUNT) {
					Util.showToast(MenuAddActivity.this, "辅料数量不能超过10");
					return;
				}
				// 将布局放入辅料布局的list中
				AddMenuItemLayout child = new AddMenuItemLayout(
						MenuAddActivity.this, mAddMenuAssistantItemLayoutList);
				mAddMenuAssistantItemLayoutList.add(child);
				mLayoutAddAssistantIngredient.addView(child);
			}
		});

		mIvMakeTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] items = { "请选择", "10分钟内", "10-20分钟", "30分钟-1小时",
						"1-2小时", "2小时以上" };
				SelectUnitDialog dialog = new SelectUnitDialog(
						MenuAddActivity.this, R.style.mydialogstyle, 0.8f,
						0.6f, items);
				dialog.setSelectedListener(new SelectUnitDialog.SelectListener() {
					@Override
					public void itemSelected(String text) {
						mTvMakeTime.setText(text);
					}
				});
				dialog.show();
			}
		});
		mIvBack.setOnClickListener(this);

		mLayoutSelectClassify.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuAddActivity.this,
						SelectClassifyActivity.class);
				i.putStringArrayListExtra("classifyLabel",
						selectedClassifyItemList);
				startActivityForResult(i, SELECT_CLASSIFY_CODE);
			}
		});

		// 下一步按钮
		mTvNextStep.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuAddActivity.this,
						MenuAddStepsActivity.class);
				i.putExtra("menu", saveValue());
				if(mMenu != null){
					setResult(RESULT_OK, i);
				}
				else{
					startActivity(i);
				}
				finish();
			}
		});

		if (mMenu != null) {
			fillData();
		}
	}
	
	@Override
	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
		case R.id.iv_activity_add_menu_back:
			// 可以正确的拿到数据
			for (AddMenuItemLayout l : mAddMenuMainItemLayoutList) {
				EditText txt1 = (EditText) l.findViewById(R.id.txt_ingredient);
				EditText txt2 = (EditText) l
						.findViewById(R.id.txt_add_menu_use_level);
				Util.log(TAG, txt1.getText() + "," + txt2.getText());
			}
			finish();
			break;
		}
	}

	// 回调方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SELECT_CLASSIFY_CODE:
			if (resultCode == RESULT_OK) {
				selectedClassifyItemList = data
						.getStringArrayListExtra("classifyLabel");
			}
			resetShowClassifyLayout();
		}
	}

	/**
	 * 重新设置标签布局
	 */
	private void resetShowClassifyLayout() {
		mShowClassifyLayout.removeAllViews();
		if (selectedClassifyItemList != null) {
			for (int i = 0; i < selectedClassifyItemList.size(); i++) {
				String text = selectedClassifyItemList.get(i);
				SelectClassifyLabelLayout v = new SelectClassifyLabelLayout(
						this, selectedClassifyItemList, text);
				v.setListener(new Listener() {
					@Override
					public void updateLayout() {
						resetShowClassifyLayout();
					}
				});
				mShowClassifyLayout.addView(v);
			}
		}
	}

	/**
	 * 拿取用户填入的值
	 */
	private Menu saveValue() {
		Menu menu = new Menu();
		if(mMenu != null){
			//只有这两个数据不变，其他都重新设置
			menu.setId(mMenu.getId());
			menu.setFaceUrl(mMenu.getFaceUrl());
		}

		menu.setName(mTxtMenuName.getText().toString());
		menu.setLabels(selectedClassifyItemList);
		menu.setProfile(mTxtProfile.getText().toString());
		menu.setMakeTime((String) mTvMakeTime.getText());
		
		ArrayList<Ingredient> mainItem = menu.getMainIngredient();
		ArrayList<Ingredient> AssistantItem = menu.getAssistantIngredient();

		for (AddMenuItemLayout l : mAddMenuMainItemLayoutList) {
			EditText txt1 = (EditText) l.findViewById(R.id.txt_ingredient);
			EditText txt2 = (EditText) l
					.findViewById(R.id.txt_add_menu_use_level);
			TextView tv = (TextView) l.findViewById(R.id.tv_add_menu_unit);

			Ingredient item = new Ingredient();
			item.setIngredientName(txt1.getText().toString());
			item.setUnit((String) tv.getText());
			item.setUseLevel(txt2.getText().toString());
			mainItem.add(item);
			// Util.log(TAG, txt1.getText() + "," + txt2.getText());
		}
		for (AddMenuItemLayout l : mAddMenuAssistantItemLayoutList) {
			EditText txt1 = (EditText) l.findViewById(R.id.txt_ingredient);
			EditText txt2 = (EditText) l
					.findViewById(R.id.txt_add_menu_use_level);
			TextView tv = (TextView) l.findViewById(R.id.tv_add_menu_unit);

			Ingredient item = new Ingredient();
			item.setIngredientName(txt1.getText().toString());
			item.setUnit((String) tv.getText());
			item.setUseLevel(txt2.getText().toString());
			AssistantItem.add(item);
			// Util.log(TAG, txt1.getText() + "," + txt2.getText());
		}
		return menu;
	}

	// 回填数据
	private void fillData() {
		//填充菜谱名
		mTxtMenuName.setText(mMenu.getName());
		//填充简介
		mTxtProfile.setText(mMenu.getProfile());
		//填充制作时间
		mTvMakeTime.setText(mMenu.getMakeTime());
		//填充分类标签
		selectedClassifyItemList = mMenu.getLabels();
		resetShowClassifyLayout();
		
		// 填充主料数据
		ArrayList<Ingredient> mainIngredientList = mMenu.getMainIngredient();
		
		Ingredient item = mainIngredientList.get(0);
		mAddMenuMainItemLayoutList.get(0).setIngredientName(
				item.getIngredientName());
		mAddMenuMainItemLayoutList.get(0).setUseLevel(
				item.getUseLevel());
		mAddMenuMainItemLayoutList.get(0).setUnit(item.getUnit());
		
		for (int i = 1; i < mainIngredientList.size(); i++) {
			 item = mainIngredientList.get(i);
			// 第一个布局是存在的，不用创建，直接填
			if (i == 0) {
				mAddMenuMainItemLayoutList.get(0).setIngredientName(
						item.getIngredientName());
				mAddMenuMainItemLayoutList.get(0).setUseLevel(
						item.getUseLevel());
				mAddMenuMainItemLayoutList.get(0).setUnit(item.getUnit());
			}
			// 需要重新创建布局
			else {
				createMenuItemLayout(mAddMenuMainItemLayoutList,
						mLayoutAddMainIngredient, item);
			}
		}

		// 填充辅料布局
		// 第一个布局是存在的，不用创建，直接填
		ArrayList<Ingredient> assistantIngredientList = mMenu.getAssistantIngredient();
		 item = assistantIngredientList.get(0);
		mAddMenuAssistantItemLayoutList.get(0).setIngredientName(
				item.getIngredientName());
		mAddMenuAssistantItemLayoutList.get(0).setUseLevel(
				item.getUseLevel());
		mAddMenuAssistantItemLayoutList.get(0).setUnit(item.getUnit());
		for (int i = 1; i < assistantIngredientList.size(); i++) {
			 item = assistantIngredientList.get(i);
				createMenuItemLayout(mAddMenuAssistantItemLayoutList,
						mLayoutAddAssistantIngredient, item);
		}
	}

	/*
	 * private void createMenuItemLayout(ArrayList<AddMenuItemLayout>
	 * layoutList,LinearLayout layout){ AddMenuItemLayout child = new
	 * AddMenuItemLayout(MenuAddActivity.this,layoutList);
	 * layoutList.add(child); layout.addView(child); }
	 */

	private void createMenuItemLayout(ArrayList<AddMenuItemLayout> layoutList,
			LinearLayout layout, Ingredient itemData) {
		AddMenuItemLayout child = new AddMenuItemLayout(MenuAddActivity.this,
				layoutList, itemData);
		layoutList.add(child);
		layout.addView(child);
	}

}
