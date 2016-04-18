package com.menu.addmenu.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.menu.model.Classify;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 局限于item为文字的ListView
 * @author Administrator
 *
 */
public class SelectClassifyActivityAdapter extends ArrayAdapter<String>{
	public static int selectedId = 0;
	private int resource;
	private int textViewId;
	private ArrayList<String> objects;
	public SelectClassifyActivityAdapter(Context context, int resource,
			ArrayList<String> objects,int textViewId) {
		super(context, resource, objects);
		this.resource = resource;
		this.objects = objects;
		this.textViewId = textViewId;
	}
	
	@Override
	public String getItem(int position) {
		return objects.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(resource,null);
		}
		
		TextView tv = (TextView) convertView.findViewById(textViewId);
		
		tv.setText(objects.get(position));
		
		return convertView;
	}
	
	
}