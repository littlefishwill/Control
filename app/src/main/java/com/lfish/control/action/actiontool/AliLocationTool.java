package com.lfish.control.action.actiontool;


		import android.content.Context;

		import com.amap.api.location.AMapLocationClient;
		import com.amap.api.location.AMapLocationClientOption;
		import com.amap.api.location.AMapLocationListener;

public class AliLocationTool {


	private static AliLocationTool baiduLocationTool;
	public AMapLocationClient mLocationClient = null;
	//声明AMapLocationClientOption对象
	public AMapLocationClientOption mLocationOption = null;
	//声明定位回调监听器
	public AMapLocationListener mLocationListener;

	public static AliLocationTool getInstace() {
		if (baiduLocationTool == null) {
			baiduLocationTool = new AliLocationTool();
		}
		return baiduLocationTool;
	}

	/**
	 * 初始化百度定位
	 */
	public void init(Context context) {
		// TODO Auto-generated method stub
		// 初始化定位
		mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听



		mLocationOption = new AMapLocationClientOption();
		mLocationOption.setOnceLocation(true);
		mLocationOption.setNeedAddress(true);
		mLocationOption.setMockEnable(true);
		mLocationOption.setOnceLocationLatest(true);
		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		mLocationClient.setLocationOption(mLocationOption);
	}



	public void getLocation(AMapLocationListener aMapLocationListener){
		mLocationClient.setLocationListener(aMapLocationListener);
		mLocationClient.setLocationOption(mLocationOption);
		mLocationClient.startLocation();
	}


}

