package com.lfish.control.action;

public class ActionConfig {
	private String actionName;
	private int drawable;
	private Class clazz;
	public String getActionName() { 
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public int getDrawable() {
		return drawable;
	}
	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}
	
	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	public ActionConfig(String actionName, int drawable, Class clazz) {
		super();
		this.actionName = actionName;
		this.drawable = drawable;
		this.clazz = clazz;
	}
	
	
	
}
