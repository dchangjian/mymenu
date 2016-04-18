package com.menu.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.menu.db.DatabaseHelper;

/**
 * 菜谱分类的Dao
 * 
 * @author Administrator
 * 
 */
public class ClassifyDao {

	private DatabaseHelper helper;

	public ClassifyDao(Context c) {
		helper = DatabaseHelper.getInstance(c);
	}
	
	/**
	 * 获取左侧分类
	 * @return
	 */
	public ArrayList<String> getClassify() {

		ArrayList<String> items = new ArrayList<String>();
		SQLiteDatabase db = helper.getWritableDatabase();
		
		Cursor cursor = db.query("classify",new String[]{"label"},null,null,null,null,null,null);
		
		if(cursor.moveToFirst()){
			do{
				String label = cursor.getString(cursor.getColumnIndex("label"));
				items.add(label);
			}while(cursor.moveToNext());
		}
		
		return items;
	}
	
	public void addClassify(String ...labels){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		for(int i = 0; i < labels.length;i++){
			db.execSQL("insert into classify(label) values(?)",new String[]{labels[i]});
		}
	
	}
	public void addClassifyItem(String classifyLabel, String... items) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int classifyId = getClassifyIdFormLabel(classifyLabel);
		for (int i = 0; i < items.length; i++) {

			db.execSQL(
					"insert into classify_item(label,classify_id) values(?,?)",
					new Object[] { items[i], classifyId });
		}
	}
	
	public ArrayList<String> getClassifyItems(String tag){
		
		ArrayList<String> items = new ArrayList<String>();
		SQLiteDatabase db = helper.getWritableDatabase();
		
		int classifyId = getClassifyIdFormLabel(tag);
		Cursor cursor = db.query("classify_item",new String[]{"label"},"classify_id=?",new String[]{""+classifyId},null,null,null,null);
		
		if(cursor.moveToFirst()){
			do{
				String label = cursor.getString(cursor.getColumnIndex("label"));
				items.add(label);
			}while(cursor.moveToNext());
		}
		return items;
	}
	
	private int getClassifyIdFormLabel(String label){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("classify",new String[]{"id"},"label=?",new String[]{label},null,null,null);
		int classifyId = 0;
		// 找到相关标签的id
		if(cursor.moveToFirst()){
			 classifyId = cursor.getInt(cursor.getColumnIndex("id"));
		}
		return classifyId;
	}
}
