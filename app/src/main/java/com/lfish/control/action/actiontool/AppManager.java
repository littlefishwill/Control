package com.lfish.control.action.actiontool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

public class AppManager {
	private Context context = null;

	public AppManager(Context context) {
		this.context = context;
	}

	public String GetInstallLists() {
		StringBuilder stringBuilder = new StringBuilder();
		List<PackageInfo> packageInfosList = context.getPackageManager()
				.getInstalledPackages(0);
		stringBuilder.append("已安装应用列表:\n");
		for (PackageInfo packageInfo : packageInfosList) {
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {

			} else {
				String appName = (String) packageInfo.applicationInfo
						.loadLabel(context.getPackageManager());
				String appPkg = packageInfo.applicationInfo.packageName;
//				stringBuilder.append("应用名:" + appName + "  包名:" + appPkg
//						+ "\n");
				stringBuilder.append("应用名: “" + appName + "” "+ "\n");

			}
		}
		return stringBuilder.toString();

	}

	public void Savetopath(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		try {
			String content = GetInstallLists();
			file.createNewFile();
			FileOutputStream os = new FileOutputStream(file);
			os.write(content.getBytes());
			os.flush();
			os.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
