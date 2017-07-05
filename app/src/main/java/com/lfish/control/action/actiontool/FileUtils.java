package com.lfish.control.action.actiontool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.os.Environment;
/**
 * A utility class for file io operaton
 */
public class FileUtils {
	/**
	 * Is SDCard exist or not
	 */
	public static boolean isHasSDCard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * Format SDCard
	 */
	public static void formatSDCard() {
		delFolder(Environment.getExternalStorageDirectory() + "/");
	}

	/**
	 * Delete file or folder
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath);
			new File(folderPath).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete file in the folder
	 */
	private static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		if (tempList == null) {
			return;
		}
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delFolder(temp.getAbsolutePath());
			}
		}
	}

	/**
	 * Write File. If the file exists, it is overwritten.
	 * 
	 * @param content
	 *            the buffer to be written.
	 * @param fileName
	 *            the name of the file to which this stream writes
	 */
	public static void writeFile(byte[] content, String fileName) {
		writeFile(content, fileName, false);
	}

	/**
	 * Write File. If the file exists, it is overwritten.
	 * 
	 * @param content
	 *            the string to be written.
	 * @param fileName
	 *            the name of the file to which this stream writes
	 */
	public static void writeFile(String content, String fileName) {
		writeFile(content, fileName, false);
	}

	/**
	 * Write File
	 * 
	 * @param content
	 *            the buffer to be written.
	 * @param fileName
	 *            the name of the file to which this stream writes
	 * @param append
	 *            indicates whether or not to append to an existing file.
	 */
	public static void writeFile(byte[] content, String fileName, boolean append) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileName, append);
			outputStream.write(content);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Write File
	 * 
	 * @param content
	 *            the string to be written.
	 * @param fileName
	 *            the name of the file to which this stream writes
	 * @param append
	 *            indicates whether or not to append to an existing file.
	 */
	public static void writeFile(String content, String fileName, boolean append) {
		if(content==null){
		return;
		}
		writeFile(content.getBytes(), fileName, append);
	}

	/**
	 * Copy file or directory
	 * 
	 * @param srcFile
	 *            the source file to copy the content
	 * @param desFile
	 *            the destination file to copy the data into.
	 */
	public static void copyFile(File srcFile, File desFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (srcFile.isFile()) {
				fis = new FileInputStream(srcFile);
				fos = new FileOutputStream(desFile);

				byte[] buf = new byte[1024 * 8];
				int len = 0;

				while ((len = fis.read(buf)) != -1) {
					fos.write(buf, 0, len);
				}
			} else if (srcFile.isDirectory()) {
				File[] f = srcFile.listFiles();
				desFile.mkdir();
				for (int i = 0; i < f.length; i++) {
					copyFile(f[i].getAbsolutePath(), desFile.getAbsolutePath() + File.separator
							+ f[i].getName());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Copy file or directory
	 * 
	 * @param srcPath
	 *            the source filepath to copy the content
	 * @param desPath
	 *            the destination filepath to copy the data into.
	 */
	public static void copyFile(String srcPath, String desPath) {
		File srcFile = new File(srcPath);
		File desFile = new File(desPath);
		copyFile(srcFile, desFile);
	}

	/**
	 * Cut file
	 * 
	 * @param srcPath
	 *            the source filepath to cut the content
	 * @param desPath
	 *            the destination filepath to cut the data into.
	 */
	public static void cutFile(String srcPath, String desPath) {
		copyFile(srcPath, desPath);
		File scrFile = new File(srcPath);
		if (scrFile.exists()) {
			delFolder(scrFile.getAbsolutePath());
		}
	}

	/**
	 * Read file to string
	 * 
	 * @param filePath
	 *            an absolute or relative path specifying the file to read
	 * @return the result string to read
	 */
	public static String readFile(String filePath) {
		String result = null;
		InputStreamReader inputStreamReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			inputStreamReader = new FileReader(filePath);
			int ch = 0;
			while (-1 != (ch = inputStreamReader.read())) {
				sb.append((char) ch);
			}
			result = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStreamReader != null)
					inputStreamReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Read Stream to string
	 * 
	 * @param filePath
	 *            an absolute or relative path specifying the file to read
	 * @return the result string to read
	 */
	public static String readStream2String(InputStream inputStream) {
		String result = null;
		InputStreamReader inputStreamReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			inputStreamReader = new InputStreamReader(inputStream);
			int ch = 0;
			while (-1 != (ch = inputStreamReader.read())) {
				sb.append((char) ch);
			}
			result = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (inputStreamReader != null)
					inputStreamReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Read file to bytes
	 * 
	 * @param filePath
	 *            an absolute or relative path specifying the file to read
	 * @return the result bytes to read
	 */
	public static byte[] readFile2bytes(String filePath) {
		byte[] result = null;
		InputStream inputStream = null;
		ByteArrayOutputStream byteOutputStram = null;
		try {
			byteOutputStram = new ByteArrayOutputStream();
			inputStream = new FileInputStream(filePath);
			byte[] tempByte = new byte[1024 * 8];
			int len = 0;
			while (-1 != (len = inputStream.read(tempByte))) {
				byteOutputStram.write(tempByte, 0, len);
			}
			result = byteOutputStram.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (byteOutputStram != null)
					byteOutputStram.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Read Stream to bytes
	 * 
	 * @return the result bytes to read
	 */
	public static byte[] readStream2bytes(InputStream inputStream) {
		byte[] result = null;
		ByteArrayOutputStream byteOutputStram = null;
		try {
			byteOutputStram = new ByteArrayOutputStream();
			byte[] tempByte = new byte[1024 * 8];
			int len = 0;
			while (-1 != (len = inputStream.read(tempByte))) {
				byteOutputStram.write(tempByte, 0, len);
			}
			result = byteOutputStram.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (byteOutputStram != null)
					byteOutputStram.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void writeFilesInSandbox(Context c, String fileName, String content, int mode) {
		if(content==null){
			return;
		}
		writeFilesInSandbox(c, fileName, content.getBytes(), mode);
	}
	

	public static void writeFilesInSandbox(Context c, String fileName, byte[] content, int mode) {
		FileOutputStream fos = null;
		try {
			fos = c.openFileOutput(fileName, mode);
			fos.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 

	public static String readFilesFromSandbox(Context c, String fileName) {
		ByteArrayOutputStream baos = null;
		FileInputStream fis = null;
		String content = null;
		try {
			baos = new ByteArrayOutputStream();
			fis = c.openFileInput(fileName);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			content = baos.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
			}
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
			}
		}
		return content;
	}
	public static LinkedList<File> listLinkedFiles(String strPath) {
		LinkedList<File> list = new LinkedList<File>();
		File dir = new File(strPath);
		File file[] = dir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory())
				list.add(file[i]);
			else
				System.out.println(file[i].getAbsolutePath());
		}
		File tmp;
		while (!list.isEmpty()) {
			tmp = (File) list.removeFirst();
			if (tmp.isDirectory()) {
				file = tmp.listFiles();
				if (file == null)
					continue;
				for (int i = 0; i < file.length; i++) {
					if (file[i].isDirectory())
						list.add(file[i]);
					else
						System.out.println(file[i].getAbsolutePath());
				}
			} else {
				System.out.println(tmp.getAbsolutePath());
			}
		}
		return list;
	}

	// recursion
	public static ArrayList<File> listFiles(String strPath,String endName) {
		ArrayList<File> arrayList = new ArrayList<File>();
		getFileList(arrayList, strPath,endName);
		return arrayList;
	}

	private static void getFileList(ArrayList<File> arrayList, String path,String endName) {
		File file = new File(path);
		if (file.isDirectory()) {
			File fileList[] = file.listFiles();
			for (File file2 : fileList) {
				getFileList(arrayList, file2.getAbsolutePath(),endName);
			}
		} else {
			String namString = file.getAbsolutePath();
			if (namString.endsWith(endName/*".amr"*/)) {
				arrayList.add(file);
			}
		}
	}

//	private static ArrayList<File> refreshFileList(String strPath) {
//		ArrayList<File> filelist = new ArrayList<File>();
//		File dir = new File(strPath);
//		File[] files = dir.listFiles();
//
//		if (files == null)
//			return null;
//		for (int i = 0; i < files.length; i++) {
//			if (files[i].isDirectory()) {
//				refreshFileList(files[i].getAbsolutePath());
//			} else {
//				if (files[i].getName().toLowerCase().endsWith(".amr"))
//					filelist.add(files[i]);
//			}
//		}
//		return filelist;
//	}
}
