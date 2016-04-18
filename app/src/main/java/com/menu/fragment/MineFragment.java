package com.menu.fragment;


import com.menu.activity.R;
import com.menu.addmenu.activity.MenuAddActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MineFragment extends Fragment {
	
	
	private View mAddMenuView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        
		View v = inflater.inflate(R.layout.fragment_main_mine, container,false);
		
		mAddMenuView = v.findViewById(R.id.layout_mine_add_menu);
		
		mAddMenuView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//从Fragment中启动activity
				Intent i = new Intent(getActivity(),MenuAddActivity.class);
				startActivity(i);
			}
		});
		
		return v;
	}
}
