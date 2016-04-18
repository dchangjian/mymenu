package com.menu.addmenu.view;

import com.menu.activity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CustomImageView  extends ImageView{
	
	private Context context;
	private String[] items = {"拍照","从相册获取"};
	public CustomImageView(Context context) {
		this(context,null);
	}
	
	public CustomImageView(Context context,AttributeSet attrs) {
		this(context,attrs,0);
	}
	
	public CustomImageView(final Context context, AttributeSet attrs, int defStyle){
		super(context,attrs,defStyle);
		this.context = context;
		
		
	}
	
	
	
	
}
