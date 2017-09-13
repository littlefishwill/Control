/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.hyphenate.easeui.R;

public class EaseBaiduMapActivity extends EaseBaseActivity {

	private final static String TAG = "map";
	static MapView mMapView = null;
	FrameLayout mMapViewContainer = null;
//	private LocationClient mLocClient;
//	public MyLocationListenner myListener = new MyLocationListenner();
//	public NotifyLister mNotifyer = null;

	Button sendButton = null;

	EditText indexText = null;
	int index = 0;
	public static EaseBaiduMapActivity instance = null;
	private AMap map;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		//initialize SDK with context, should call this before setContentView
//        SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.ease_activity_baidumap);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.onCreate(savedInstanceState);
		map = mMapView.getMap();
		Intent intent = getIntent();
		double latitude = intent.getDoubleExtra("latitude", 0);

		if (latitude == 0) {
//			mMapView = new MapView(this, new BaiduMapOptions());
//			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
//							mCurrentMode, true, null));
//			showMapWithLocationClient();
		} else {
			double longtitude = intent.getDoubleExtra("longitude", 0);
			String address = intent.getStringExtra("address");

			showMap(latitude, longtitude, address);
		}

	}

	public void showMap(double latitude,double longtitude,String address){
		LatLng latLng = new LatLng(latitude, longtitude);

		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.ico_ali_posation_ico));


//		Bitmap bitMap = bitmapDescriptor.getBitmap();
//		int width = bitMap.getWidth();
//		int height = bitMap.getHeight();
//		// 设置想要的大小
//		int newWidth = 70;
//		int newHeight = 70;
//		// 计算缩放比例
//		float scaleWidth = ((float) newWidth) / width;
//		float scaleHeight = ((float) newHeight) / height;
//		// 取得想要缩放的matrix参数
//		Matrix matrix = new Matrix();
//		matrix.postScale(scaleWidth, scaleHeight);
//		// 得到新的图片
//		bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix,
//				true);



		MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address).snippet("对方所在位置").icon(bitmapDescriptor);


		Marker marker = map.addMarker(markerOptions);
		Animation animation = new RotateAnimation(marker.getRotateAngle(),marker.getRotateAngle()+180,0,0,0);
		long duration = 1000L;
		animation.setDuration(duration);
		animation.setInterpolator(new LinearInterpolator());

		marker.startAnimation();

//		CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(17);
		CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,18,30,0));
		map.animateCamera(mCameraUpdate);
		UiSettings uiSettings = map.getUiSettings();
		uiSettings.setCompassEnabled(true);
		uiSettings.setScaleControlsEnabled(true);


	}

//	private void showMap(double latitude, double longtitude, String address) {
//		sendButton.setVisibility(View.GONE);
//		LatLng llA = new LatLng(latitude, longtitude);
//		CoordinateConverter converter= new CoordinateConverter();
//		converter.coord(llA);
//		converter.from(CoordinateConverter.CoordType.COMMON);
//		LatLng convertLatLng = converter.convert();
//		OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory
//				.fromResource(R.drawable.ease_icon_marka))
//				.zIndex(4).draggable(true);
//		mBaiduMap.addOverlay(ooA);
//		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
//		mBaiduMap.animateMapStatus(u);
//	}
//
//	private void showMapWithLocationClient() {
//		String str1 = getResources().getString(R.string.Making_sure_your_location);
//		progressDialog = new ProgressDialog(this);
//		progressDialog.setCanceledOnTouchOutside(false);
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		progressDialog.setMessage(str1);
//
//		progressDialog.setOnCancelListener(new OnCancelListener() {
//
//			public void onCancel(DialogInterface arg0) {
//				if (progressDialog.isShowing()) {
//					progressDialog.dismiss();
//				}
//				Log.d("map", "cancel retrieve location");
//				finish();
//			}
//		});
//
//		progressDialog.show();
//
//		mLocClient = new LocationClient(this);
//		mLocClient.registerLocationListener(myListener);
//
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);// open gps
//		// option.setCoorType("bd09ll");
//		// Johnson change to use gcj02 coordination. chinese national standard
//		// so need to conver to bd09 everytime when draw on baidu map
//		option.setCoorType("gcj02");
//		option.setScanSpan(30000);
//		option.setAddrType("all");
//		mLocClient.setLocOption(option);
//	}
//

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
		mMapView.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
		mMapView.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
		mMapView.onPause();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
		mMapView.onSaveInstanceState(outState);
	}

//	@Override
//	protected void onPause() {
//		mMapView.onPause();
//		if (mLocClient != null) {
//			mLocClient.stop();
//		}
//		super.onPause();
//		lastLocation = null;
//	}
//
//	@Override
//	protected void onResume() {
//		mMapView.onResume();
//		if (mLocClient != null) {
//			mLocClient.start();
//		}
//		super.onResume();
//	}
//
//	@Override
//	protected void onDestroy() {
//		if (mLocClient != null)
//			mLocClient.stop();
//		mMapView.onDestroy();
//		unregisterReceiver(mBaiduReceiver);
//		super.onDestroy();
//	}
//	private void initMapView() {
//		mMapView.setLongClickable(true);
//	}
//
//	/**
//	 * format new location to string and show on screen
//	 */
//	public class MyLocationListenner implements BDLocationListener {
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			if (location == null) {
//				return;
//			}
//			Log.d("map", "On location change received:" + location);
//			Log.d("map", "addr:" + location.getAddrStr());
//			sendButton.setEnabled(true);
//			if (progressDialog != null) {
//				progressDialog.dismiss();
//			}
//
//			if (lastLocation != null) {
//				if (lastLocation.getLatitude() == location.getLatitude() && lastLocation.getLongitude() == location.getLongitude()) {
//					Log.d("map", "same location, skip refresh");
//					// mMapView.refresh(); //need this refresh?
//					return;
//				}
//			}
//			lastLocation = location;
//			mBaiduMap.clear();
//			LatLng llA = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//			CoordinateConverter converter= new CoordinateConverter();
//			converter.coord(llA);
//			converter.from(CoordinateConverter.CoordType.COMMON);
//			LatLng convertLatLng = converter.convert();
//			OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory
//					.fromResource(R.drawable.ease_icon_marka))
//					.zIndex(4).draggable(true);
//			mBaiduMap.addOverlay(ooA);
//			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
//			mBaiduMap.animateMapStatus(u);
//		}
//
//		public void onReceivePoi(BDLocation poiLocation) {
//			if (poiLocation == null) {
//				return;
//			}
//		}
//	}
//
//	public class NotifyLister extends BDNotifyListener {
//		public void onNotify(BDLocation mlocation, float distance) {
//		}
//	}
//
//	public void back(View v) {
//		finish();
//	}
//
//	public void sendLocation(View view) {
//		Intent intent = this.getIntent();
//		intent.putExtra("latitude", lastLocation.getLatitude());
//		intent.putExtra("longitude", lastLocation.getLongitude());
//		intent.putExtra("address", lastLocation.getAddrStr());
//		this.setResult(RESULT_OK, intent);
//		finish();
//		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
//	}

}
