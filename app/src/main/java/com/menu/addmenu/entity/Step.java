package com.menu.addmenu.entity;

import java.io.Serializable;
import java.util.UUID;
/**
 * 添加具体步骤的实体
 * @author Administrator
 *
 */
public class Step implements Serializable{
	private String id = UUID.randomUUID().toString();
	private String url; //拍摄的照片的地址
	private String desc; //描述
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null){
			return false;
		}
		if(o instanceof Step){
			Step step = (Step)o;
			if(step.getId().equals(this.getId())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		
		return super.hashCode();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
