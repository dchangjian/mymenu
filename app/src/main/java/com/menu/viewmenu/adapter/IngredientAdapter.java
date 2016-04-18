package com.menu.viewmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/19 0019.
 */
public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> objects;

    public IngredientAdapter(Context context, ArrayList<Ingredient> objects) {
        super(context, 0, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Ingredient ingredient = objects.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_view_menu_ingredient_item,parent,false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_activity_view_menu_ingredient_item_name);
        TextView tvUse = (TextView) convertView.findViewById(R.id.tv_activity_view_menu_ingredient_item_use);

        tvName.setText(ingredient.getIngredientName());
        tvUse.setText(ingredient.getUseLevel() + ingredient.getUnit());

        return convertView;
    }
}
