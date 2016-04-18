package com.menu.model;

import java.util.ArrayList;

/**
 * 分类
 * @author Administrator
 *.
 */
public class Classify {

	//分类名称
	private String classifyName;
	//分类的具体项
	private ArrayList<String> classifyItem = new ArrayList<String>();
	
	public Classify(String name,String...items){
		classifyName = new String(name);
		setClassifyItem(items);
	}
	
	public String getClassifyName() {
		return classifyName;
	}
	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}
	public ArrayList<String> getClassifyItem() {
		return classifyItem;
	}

	public void setClassifyItem(String ...items){
		for(int i = 0; i < items.length;i++){
			classifyItem.add(items[i]);
		}
	}
}
