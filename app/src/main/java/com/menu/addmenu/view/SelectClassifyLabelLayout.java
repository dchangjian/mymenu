package com.menu.addmenu.view;

import java.util.ArrayList;

import com.menu.activity.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectClassifyLabelLayout extends LinearLayout{
	private Context c;
	
	private TextView mTv;
	
	private Listener mListener;

	
	private ArrayList<String> list;
	public SelectClassifyLabelLayout(Context context,final ArrayList<String> list,String text) {
		super(context);
		this.list = list;
		LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.activity_select_classify__show_label_item, this,true);
		
		mTv = (TextView) layout.findViewById(R.id.tv_activity_select_classify_item);
	
		mTv.setText(text);
	
		mTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.remove(mTv.getText());
				setVisibility(View.GONE);
				
				if(mListener != null){
					mListener.updateLayout();
				}
			}
		});
	}
	
	public interface Listener{
		void updateLayout();
	}
	
	public void setListener(Listener listener){
		this.mListener = listener;
	}
	
	
	
	

}
