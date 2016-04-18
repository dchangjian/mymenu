package com.menu.viewmenu.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.addmenu.entity.Step;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

import in.srain.cube.image.CubeImageView;

public class StepAdapter extends ArrayAdapter<Step> {
    public static final String STEP_ADAPTER = "StepAdapter";
    private ArrayList<Step> objects;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public StepAdapter(Context context, ArrayList<Step> objects) {
        super(context, 0, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Step step = objects.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_view_menu_step_item, parent, false);
        }

        TextView tvStepNo = (TextView) convertView.findViewById(R.id.tv_activity_view_menu_item_step_no);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.tv_activity_view_menu_item_desc);
        final CubeImageView imageView = (CubeImageView) convertView.findViewById(R.id.iv_activity_view_menu_step_item);

        tvStepNo.setText(position + 1 + "");
        tvDesc.setText(step.getDesc());

        if (step.getUrl() != null) {
            if (step.getUrl().startsWith("http://")) {
                imageLoader.displayImage(step.getUrl(),imageView);
            } else {
               imageLoader.displayImage("file://" + step.getUrl(),imageView);
            }
        } else {
            imageView.setImageResource(R.drawable.empty_photo);
        }
        return convertView;
    }
}
