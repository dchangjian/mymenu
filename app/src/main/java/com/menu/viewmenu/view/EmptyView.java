package com.menu.viewmenu.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.menu.activity.R;


public class EmptyView extends RelativeLayout{
    public EmptyView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.empyt_content,
                this, true);
    }
}
