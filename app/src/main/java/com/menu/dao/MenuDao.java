package com.menu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.menu.addmenu.entity.Step;
import com.menu.db.DatabaseHelper;
import com.menu.model.Ingredient;
import com.menu.model.Menu;
import com.menu.util.Util;

import java.util.ArrayList;
import java.util.UUID;

public class MenuDao {
    //主料类型
    public static final int MAIN_INGREDIENT_TYPE = 1;
    public static final int PAGE_SIZE = 5;
    //辅料类型
    public static final int ASSISTANT_INGREDIENT_TYPE = 2;
    private DatabaseHelper mHelper;

    private int currentPage = 0;

    //记录条件查询的标签
    private String tagName;


    public MenuDao(Context c) {
        mHelper = DatabaseHelper.getInstance(c);
    }

    public void save(Menu menu) {

        if (menu.getId() == null || menu.getId().equals("")) {
            addMenu(menu);
        }
        else{
            updateMenu(menu);
        }
    }

    public void addMenu(Menu menu) {

        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.beginTransaction();

        //设置menu_id
        menu.setId(genUUID());
        ContentValues values = new ContentValues();

        //添加menu表数据
        values.put("id", menu.getId());
        values.put("name", menu.getName());
        values.put("make_time", menu.getMakeTime());
        values.put("face_url", menu.getFaceUrl());
        values.put("profile", menu.getProfile());
        db.insert("menu", null, values);

        //保存标签
        if (menu.getLabels() != null) {
           addLabels(menu.getLabels(),db,menu.getId());
        }

        //保存主料
        addIngredient(menu.getMainIngredient(), db, MAIN_INGREDIENT_TYPE, menu.getId());
        //保存辅料
        addIngredient(menu.getAssistantIngredient(), db, ASSISTANT_INGREDIENT_TYPE, menu.getId());

        //保存步骤相关信息
        addSteps(menu.getSteps(),db,menu.getId());

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<Menu> queryMenu(int pageNo,String tagName) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ArrayList<Menu> menus = new ArrayList<Menu>();

        currentPage = pageNo;

//        if(!hasMore()){
//            return menus;
//        }

        String sql = null;
        Cursor cursor = null;

        if(tagName != null){
            this.tagName = tagName;
            sql = "select * from menu where id in(select menu_id from label where name like '"+tagName+"') limit " + String.valueOf(PAGE_SIZE) + " offset " + String.valueOf((currentPage - 1) * PAGE_SIZE);

        }
        else{
            this.tagName = null;
            sql = "select * from menu limit " + String.valueOf(PAGE_SIZE) + " offset " + String.valueOf((currentPage - 1) * PAGE_SIZE);
        }


        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();
                menu.setId(cursor.getString(cursor.getColumnIndex("id")));
                menu.setName(cursor.getString(cursor.getColumnIndex("name")));
                menu.setProfile(cursor.getString(cursor.getColumnIndex("profile")));
                menu.setFaceUrl(cursor.getString(cursor.getColumnIndex("face_url")));
                menu.setMakeTime(cursor.getString(cursor.getColumnIndex("make_time")));

                ArrayList<String> labels = getLabels(menu.getId());
                menu.setLabels(labels);
                menus.add(menu);
            }
            while (cursor.moveToNext());
        }

        return menus;
    }

    public void updateMenu(Menu menu) {

        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.beginTransaction();

        String[] args = {menu.getName(),menu.getProfile(),menu.getFaceUrl(),menu.getMakeTime(),menu.getId()};

        db.execSQL("update menu set name=?,profile=?,face_url=?,make_time=? where id=?",args);

        //先删除
        deleteIngredient(menu.getId(), db);
        deleteLabels(menu.getId(), db);
        deleteSteps(menu.getId(),db);

        //再添加
        addIngredient(menu.getMainIngredient(),db,MAIN_INGREDIENT_TYPE,menu.getId());
        addIngredient(menu.getAssistantIngredient(),db,ASSISTANT_INGREDIENT_TYPE,menu.getId());
        addSteps(menu.getSteps(), db, menu.getId());
        addLabels(menu.getLabels(),db,menu.getId());

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 生成随机的UUID
     *
     * @return
     */
    private String genUUID() {
        return UUID.randomUUID().toString();
    }

    private int getCount() {

        SQLiteDatabase db = mHelper.getWritableDatabase();


        Cursor cursor;

        if(tagName != null){
            cursor = db.rawQuery("select count(*) as num from menu,label where label.name like ? and label.menu_id=menu.id", new String[]{tagName});
        }
        else{
            cursor = db.rawQuery("select count(*) as num from menu", null);
        }

        if(cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex("num"));
        }

        return -1;
    }

    /**
     * 根据menu_id查找label标签
     *
     * @param menuId
     * @return
     */
    private ArrayList<String> getLabels(String menuId) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ArrayList<String> labels = new ArrayList<String>();

        Cursor cursor = db.rawQuery("select name from label where menu_id = ?", new String[]{menuId});

        if (cursor.moveToFirst()) {
            do {
                String label = cursor.getString(cursor.getColumnIndex("name"));
                labels.add(label);
            } while (cursor.moveToNext());
        }
        return labels;
    }

    public ArrayList<Step> getSteps(String menuId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ArrayList<Step> steps = new ArrayList<Step>();

        Cursor cursor = db.rawQuery("select id,url,desc from photo where menu_id = ?", new String[]{menuId});

        if (cursor.moveToFirst()) {
            do {
                Step step = new Step();
                step.setId(cursor.getString(cursor.getColumnIndex("id")));
                step.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                step.setDesc(cursor.getString(cursor.getColumnIndex("desc")));

                steps.add(step);
            } while (cursor.moveToNext());
        }
        return steps;
    }

    /**
     * 查询菜谱用料信息
     * @param menuId
     * @param type 区分主料还是辅料
     * @return
     */
    public ArrayList<Ingredient> getIngredient(String menuId,int type){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        Cursor cursor = db.rawQuery("select name,use_level,unit from ingredient where menu_id = ? and type = ?", new String[]{menuId,String.valueOf(type)});

        if (cursor.moveToFirst()) {
            do {
                Ingredient item = new Ingredient();
                item.setIngredientName(cursor.getString(cursor.getColumnIndex("name")));
                item.setUseLevel(cursor.getString(cursor.getColumnIndex("use_level")));
                item.setUnit(cursor.getString(cursor.getColumnIndex("unit")));

                ingredients.add(item);

            } while (cursor.moveToNext());
        }
        return ingredients;
    }

    private void deleteIngredient(String menuId,SQLiteDatabase db){
        db.execSQL("delete from ingredient where menu_id = ?",new String[]{menuId});
    }

    private void deleteLabels(String menuId,SQLiteDatabase db){
        db.execSQL("delete from label where menu_id = ?",new String[]{menuId});
    }

    private void deleteSteps(String menu_id,SQLiteDatabase db){
        db.execSQL("delete from photo where menu_id=?",new String[]{menu_id});
    }

    private void addIngredient(ArrayList<Ingredient> items,SQLiteDatabase db,int type,String menuId){
        //保存
        ContentValues values = new ContentValues();
        for (int i = 0; i < items.size(); i++) {
            values.clear();
            values.put("id", genUUID());
            values.put("name", items.get(i).getIngredientName());
            values.put("use_level", items.get(i).getUseLevel());
            values.put("unit", items.get(i).getUnit());
            values.put("type", type);
            values.put("menu_id",menuId);

            db.insert("ingredient", null, values);
        }

    }

    private void addLabels(ArrayList<String> labels,SQLiteDatabase db,String menuId){
        ContentValues values = new ContentValues();
        for (int i = 0; i < labels.size(); i++) {
            values.clear();
            values.put("id", genUUID());
            values.put("name", labels.get(i));
            values.put("menu_id", menuId);
            db.insert("label", null, values);
        }
    }

    private void addSteps(ArrayList<Step> steps,SQLiteDatabase db,String menuId){
        ContentValues values = new ContentValues();
        //最后一个步骤无效
        for (int i = 0; i <steps.size() - 1; i++) {
            values.clear();
            Step step =steps.get(i);
            values.put("id", genUUID());
            values.put("url", step.getUrl());
            values.put("desc", step.getDesc());
            values.put("step_no", i + 1);
            values.put("menu_id",menuId);

            db.insert("photo", null, values);
        }
    }

    public boolean hasMore(){
        return (currentPage) * PAGE_SIZE < getCount();
    }
}
