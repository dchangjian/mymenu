package com.menu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	public static final int VERSION = 1;
	public static final String DATABASE_NAME = "menu.sqlite";
	private static DatabaseHelper dbHelper;
	
	private DatabaseHelper(Context c){
		super(c, DATABASE_NAME, null, VERSION);
	}
	
	public static DatabaseHelper getInstance(Context c){
		if(dbHelper == null){
			dbHelper = new DatabaseHelper(c);
		}
		return dbHelper;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建一系列的table
		db.execSQL("create table menu(id text primary key,name text not null,make_time text,face_url text,profile text,face_small_url text)");
		db.execSQL("create table ingredient(id text primary key,name text,use_level text,unit text,type integer, menu_id text,foreign key(menu_id) references menu(id))");
		db.execSQL("create table photo(id text primary key,url text,desc text not null,step_no integer not null,menu_id text,foreign key(menu_id) references menu(id))");
		db.execSQL("create table label(id text primary key,name text ,menu_id,foreign key (menu_id) references menu(id))");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	

}
