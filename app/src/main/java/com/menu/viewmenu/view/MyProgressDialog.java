package com.menu.viewmenu.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.menu.activity.R;

/**
 * Created by Administrator on 2016/3/31 0031.
 */
public class MyProgressDialog extends ProgressDialog{


    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutInflater().inflate(R.layout.load_progress_dialog,null,true));


        ImageView image = (ImageView) findViewById(R.id.iv_load_progress);
        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.loading_progress); // 加载动画
        image.startAnimation(animation);
    }
}
