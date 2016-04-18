package com.menu.model;

import java.io.Serializable;

/**
 * 材料类
 */
public class Ingredient implements Serializable{

	private static final long serialVersionUID = 1L;
	private String ingredientName;//食材名
	private String useLevel=""; //用量
	private String unit=""; //选择单位
	
	public String getIngredientName() {
		return ingredientName;
	}
	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	public String getUseLevel() {
		return useLevel;
	}
	public void setUseLevel(String useLevel) {
		this.useLevel = useLevel;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
