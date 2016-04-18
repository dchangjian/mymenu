package com.menu.addmenu.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import com.menu.activity.R;
import com.menu.addmenu.activity.MenuAddActivity;
import com.menu.addmenu.activity.MenuAddStep;
import com.menu.addmenu.activity.MenuAddStepsActivity;
import com.menu.addmenu.entity.Step;
import com.menu.util.Util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddSetpsAdapter extends ArrayAdapter<Step> {
	
	private ArrayList<Step> steps;
	private MenuAddStepsActivity context;
	private Bitmap defaultBitmap;
	
	
	public AddSetpsAdapter(Context context, int resource,
			ArrayList<Step> objects) {
		super(context, 0, objects);
		this.steps = objects;
		this.context = (MenuAddStepsActivity) context;
		// 最后一个添加符号
		Step step = new Step();
		Resources res = context.getResources();
		defaultBitmap  = BitmapFactory.decodeResource(res,
				R.drawable.ico_add_menu_step);
		step.setDesc("添加步骤");
		steps.add(step);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Step step = steps.get(position);
		//如果是最后一个直接解析
		if (position == steps.size() - 1) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.activity_add_menu_step_item_continue_add, null,
					false);
			TextView tvDel = (TextView) convertView
					.findViewById(R.id.tv_activity_add_menu_steps_menu_item_del);
			
			tvDel.setVisibility(View.INVISIBLE);
			
			TextView tvSpec = (TextView) convertView
					.findViewById(R.id.tv_activity_add_menu_steps_desc);
			tvSpec.setText("添加步骤");
			tvSpec.setTextColor(Color.RED);
		} 
		else{
			convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.activity_add_menu_step_item, null, false);
			TextView tvDel = (TextView) convertView
					.findViewById(R.id.tv_activity_add_menu_steps_menu_item_del);
			tvDel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (steps.get(position).getUrl() != null) {
						File file = new File(steps.get(position).getUrl());
						file.delete();
					}
					steps.remove(position);
					Util.showToast(context, "成功删除该步骤");
					AddSetpsAdapter.this.notifyDataSetChanged();
				}
			});
		}
		final ImageView iv = (ImageView) convertView
				.findViewById(R.id.iv_activity_add_menu_steps_item_upload_photo);
		TextView tvSpec = (TextView) convertView
				.findViewById(R.id.tv_activity_add_menu_steps_desc);
		iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, MenuAddStep.class);

				if (position != steps.size() - 1) {
					i.putExtra("step", step);
				}
				context.startActivityForResult(i, context.SAVE_STEP);
			}
		});

		ViewTreeObserver vto = iv.getViewTreeObserver();

		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				// 避免重复监听
				iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				iv.getHeight();
				iv.getWidth();
				if(step.getUrl() != null && !step.getUrl().equals("")){
					
					Bitmap bitmap = BitmapFactory.decodeFile(step.getUrl());
					bitmap = Util.reduce(bitmap, iv.getWidth(),
							iv.getHeight(), true);
					iv.setImageBitmap(bitmap);
				}
//				else{
//					Bitmap bitmap = Util.reduce(defaultBitmap, iv.getWidth(),
//							iv.getHeight(), true);
//					iv.setImageBitmap(bitmap);
//				}
			}
		});
		// 压缩图片尺寸
		Log.d("tag", iv.getWidth() + "--");
		tvSpec.setText(step.getDesc());
		return convertView;
	}

}
