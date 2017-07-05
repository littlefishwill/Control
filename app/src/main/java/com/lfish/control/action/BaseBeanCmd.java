package com.lfish.control.action;

import java.io.File;
import java.util.Properties;

public abstract class BaseBeanCmd {

	public static final String TABLE_NAME = "cmd_list";

	public static final String COLUMN_NAME_ID = "cmd_id";

	public static final String COLUMN_NAME_CMDNUMBER = "cmd_number";

	public static final String COLUMN_NAME_CMDTIME = "cmd_time";

	public static final String COLUMN_NAME_CMDFROM = "cmd_from";

	/**
	 * 获取简短的描述
	 * @return
	 */
	public abstract String getShortDes();

	/**
	 * 获取详细描述
	 * @return
	 */
	public abstract String getLongDes();

	/**
	 * 获取命令id
	 * @return
	 */
	public abstract int getCmdNumber();

	/**
	 * 获取命令图标
	 * @return
	 */
	public abstract int getDrawable();

	private String menuName;
	private String menuDes;
	private String menuIco;
	private boolean lock;
	private double price;

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuDes() {
		return menuDes;
	}

	public void setMenuDes(String menuDes) {
		this.menuDes = menuDes;
	}


	public String getMenuIco() {
		return menuIco;
	}

	public void setMenuIco(String menuIco) {
		this.menuIco = menuIco;
	}
}
