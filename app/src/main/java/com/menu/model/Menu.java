
package com.menu.model;

import com.menu.addmenu.entity.Step;

import java.io.Serializable;
import java.util.ArrayList;



/**
 * 菜谱类
 * @author Administrator
 *
 */
public class Menu implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String detailSrc; //详细信息页面的src
	private String id;
	private String name; //菜谱名称
	private String faceUrl; //封面照片
	private ArrayList<Ingredient> mainIngredient = new ArrayList<Ingredient>(); //主料
	private ArrayList<Ingredient> assistantIngredient = new ArrayList<Ingredient>(); //辅料
	private String profile; //简介
	private ArrayList<String> labels; //标签
	private String makeTime; //制作时间
	private ArrayList<Step> steps;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Ingredient> getMainIngredient() {
		return mainIngredient;
	}
	public void setMainIngredient(ArrayList<Ingredient> mainIngredient) {
		this.mainIngredient = mainIngredient;
	}
	public ArrayList<Ingredient> getAssistantIngredient() {
		return assistantIngredient;
	}
	public void setAssistantIngredient(ArrayList<Ingredient> assistantIngredient) {
		this.assistantIngredient = assistantIngredient;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public ArrayList<String> getLabels() {
		return labels;
	}
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}
	public String getMakeTime() {
		return makeTime;
	}
	public void setMakeTime(String makeTime) {
		this.makeTime = makeTime;
	}
	public String getFaceUrl() {
		return faceUrl;
	}
	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<Step> getSteps() {
		return steps;
	}
	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}
	public String getDetailSrc()
	{
		return detailSrc;
	}
	public void setDetailSrc(String detailSrc)
	{
		this.detailSrc = detailSrc;
	}
}
