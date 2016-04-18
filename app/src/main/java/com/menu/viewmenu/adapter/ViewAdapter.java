package com.menu.viewmenu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.model.Menu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;


import java.util.ArrayList;

import in.srain.cube.image.CubeImageView;


public class ViewAdapter extends ArrayAdapter<Menu> {

    private ArrayList<Menu> menuList;
    private Context context;

    //ImageLoader imageLoader;
    ImageLoader imageLoader = ImageLoader.getInstance();

    DisplayImageOptions options;



    public ViewAdapter(Context context, int resource, ArrayList<Menu> objects) {
        super(context, 0, objects);
        menuList = objects;
        this.context = context;
        //imageLoader = ImageLoaderFactory.create(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_photo) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.empty_photo)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.empty_photo)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new SimpleBitmapDisplayer())
                .build();//构建完成
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_main_view_item, null);


        Menu menu = menuList.get(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_fragment_main_view_face_photo);
        TextView mTv = (TextView) convertView.findViewById(R.id.tv_fragment_main_view_menu_name);

        mTv.setText(menu.getName());
        String faceUrl = menu.getFaceUrl();
        if (faceUrl != null && faceUrl.startsWith("http://")) {
            imageLoader.displayImage(faceUrl, imageView, options);
            //imageView.loadImage(imageLoader,menu.getFaceUrl());
        } else if (faceUrl != null) {
            imageLoader.displayImage("file://" + faceUrl, imageView,options);
            //imageView.loadImage(imageLoader, "file://" + menu.getFaceUrl());
            //imageView.setImageBitmap(BitmapFactory.decodeFile(faceUrl));
        }
        return convertView;
    }


}
