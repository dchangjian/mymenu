package com.menu.addmenu.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.model.Ingredient;

public class AddMenuItemLayout extends LinearLayout {
	private Context mContext;
	/**
	 * 食材名
	 */
	private EditText mTxtIngredientName;
	/**
	 * 用量
	 */
	private EditText mTxtUseLevel;
	/**
	 * 用量单位
	 */
	private TextView mTvUnit;
	
	/**
	 * 回填的构造
	 */
	
	public AddMenuItemLayout(Context c,final ArrayList<AddMenuItemLayout> list,Ingredient ingredientData){
		this(c,list);
		mTxtIngredientName.setText(ingredientData.getIngredientName());
		mTxtUseLevel.setText(ingredientData.getUseLevel());
		mTvUnit.setText(ingredientData.getUnit());
	}
	
	/**
	 * 添加的构造
	 * @param context
	 * @param list
	 */
	public AddMenuItemLayout(Context context,
			final ArrayList<AddMenuItemLayout> list) {
		super(context);
		this.mContext = context;
		// 第三个参数必须传入true
		LayoutInflater.from(context).inflate(R.layout.activity_add_menu_item,
				this, true);

		ImageView ivDel = (ImageView) findViewById(R.id.iv_activity_add_menu_del_item);
		mTxtIngredientName = (EditText) findViewById(R.id.txt_ingredient);
		mTxtUseLevel = (EditText) findViewById(R.id.txt_add_menu_use_level);
		ImageView ivAddMenuArrow = (ImageView) findViewById(R.id.iv_activity_add_menu_arrow);
		 mTvUnit = (TextView) findViewById(R.id.tv_add_menu_unit);
		
		ivDel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				list.remove(AddMenuItemLayout.this);
				setVisibility(View.GONE);
			}
		});
		ivAddMenuArrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//要显示的内容
				String[] items = {"请选择","少量","适量","克","毫升","个","根","盒","条","只","块","段","勺","滴","片","杯","斤"};
				SelectUnitDialog dialog = new SelectUnitDialog(mContext,
						R.style.mydialogstyle,0.8f,1.0f,items);
				
				dialog.setSelectedListener(new SelectUnitDialog.SelectListener() {
					@Override
					public void itemSelected(String text) {
						mTvUnit.setText(text);
					}
				});
				dialog.show();
			}
		});
	}
	public void setIngredientName(String name){
		mTxtIngredientName.setText(name);
	}
	public void setUseLevel(String level){
		mTxtUseLevel.setText(level);
	}
	public void setUnit(String unit){
		mTvUnit.setText(unit);
	}
}
