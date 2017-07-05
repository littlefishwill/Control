package com.lfish.control.action.actiontool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;

public class CLLog {
	public CLLog(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	private Context context;

	// public CallLogTo
	public void saveCLtopath(String path) {
		String str = getCL();
		if (str != null)
			save(str, path);
	}

	private void save(String ss, String path) { // 存储通话记录

		String content = ss;

		File file = new File(path);
		if (file.exists()) {

			file.delete();

		}
		try {
			file.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(content.getBytes());
			outputStream.flush();
			outputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 获取通话记录
	public String getCL() {
		StringBuilder smsBuilder = new StringBuilder();
		ContentResolver cr = context.getContentResolver();

		if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    public void requestPermissions(@NonNull String[] permissions, int requestCode)
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return "";
		}
		Cursor cur = cr.query(CallLog.Calls.CONTENT_URI,
				new String[]{CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
						CallLog.Calls.TYPE, CallLog.Calls.DATE,
						CallLog.Calls.DURATION}, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
		Boolean isHaveBoolean = false;
		if (null != cur && cur.getCount() > 0 && cur.moveToFirst()) {

			do {
				String strNumber = cur.getString(0);
				String strName = cur.getString(1);

				int intType = cur.getInt(2);
				String type = "";
				switch (intType) {
					case 1:
						type = "来电";
						break;
					case 2:
						type = "拨出";
						break;
					case 3:
						type = "未接";
						break;

					default:
						break;
				}
				long duration = cur.getLong(4);
				Date date = new Date(Long.parseLong(cur.getString(3)));
				String time = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss").format(date);

				// smsBuilder.append("[ ");
				smsBuilder.append(strNumber + ", ");
				smsBuilder.append(strName + ", ");
				smsBuilder.append(type + ", ");
				smsBuilder.append(duration + ", ");
				smsBuilder.append(time);
				smsBuilder.append(" \r\n");
			} while (cur.moveToNext());

			if (!cur.isClosed()) {
				cur.close();
				cur = null;
			}
			isHaveBoolean = true;
		} else {
			if (null != cur) {
				cur.close();
				cur = null;
			}
			isHaveBoolean = false;
			smsBuilder.append("no result!");
		} // end if
		if (isHaveBoolean) {
			return smsBuilder.toString();
		} else {
			return null;
		}
	}

	private int checkSelfPermission(String readCallLog) {
		return 0;
	}
}
