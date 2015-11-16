package com.example.lunbotu;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 
 *     图片缓存类，封装了网络获取图片，本地缓存，内存缓存
 * 
 **/

public class ImageUtil {
	private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	public static final String IMAGE_PATH = "/sdcard/images/";
	private Context mContext;
	public ImageUtil(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * JPG图片缓存
	 * 
	 * @param imagePath
	 * @param bitmap
	 * @throws IOException
	 */
	public static void saveImageJpeg(String imagePath, Bitmap bitmap)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		byte[] b = bos.toByteArray();
		saveImage(imagePath, b);
		bos.flush();
		bos.close();
	}

	/**
	 * PNG图片缓存
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */

	public static void saveImagePng(String imagePath, Bitmap bitmap)
			throws IOException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] b = bos.toByteArray();
		saveImage(imagePath, b);
		bos.flush();
		bos.close();
	}

	/**
	 * 缓存图片
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */
	public static void saveImage(String imagePath, byte[] buffer)
			throws IOException {
		File f = new File(imagePath);
		if (f.exists()) {
			return;
		} else {
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(imagePath);
			fos.write(buffer);
			fos.flush();
			fos.close();
		}
	}

	/**
	 * 从本地获取图片
	 * 
	 * @param imagePath
	 * @return Bitmap
	 */
	public static Bitmap getImageFromLocal(String imagePath) {
		File file = new File(imagePath);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			file.setLastModified(System.currentTimeMillis());
			return bitmap;
		}
		return null;
	}

	/**
	 * 从网络获取图片并缓存
	 * 
	 * @return Bitmap
	 * @throws IOException
	 */
	Bitmap mbitmap = null;
	String url = null;
	public Bitmap loadImage(final String imageName, final String imgUrl,
			final boolean isbusy, final ImageCallback callback) {
		
		Bitmap bitmap = null;
		if (imageCache.containsKey(imgUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imgUrl);
			bitmap = softReference.get();
			if (bitmap != null) {
				Log.i(TuTu.TAG, "从内存获得图片成功。。");
				callback.loadImage(bitmap, imgUrl);
				return bitmap;
			}
		}
		// if(!isbusy){
		// 从本地获取图片
		bitmap = getBitmapFromData(imageName, mContext);
		// System.out.println("从本地获取图片");
		// }
		if (bitmap != null) {
			imageCache.put(imgUrl, new SoftReference<Bitmap>(bitmap));
			Log.i(TuTu.TAG, "从本地获得图片成功。。");
			callback.loadImage(bitmap, imgUrl);
			return bitmap;
		} else {// 从网络获取图片
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.obj != null) {
						synchronized (imageCache) {
							Map<String, Bitmap> bitmapMap = (Map) msg.obj;
							for(String str:bitmapMap.keySet()){
								mbitmap = bitmapMap.get(str);
								url = str;
								imageCache.put(url,new SoftReference<Bitmap>(mbitmap));
							}
							
							if (android.os.Environment.getExternalStorageState()
									.equals(android.os.Environment.MEDIA_MOUNTED)) {
								try {
									String imageName = url.substring(url.lastIndexOf("/")+1, url.length());
									if(!imageName.endsWith(".jpg") && !imageName.endsWith(".png")){
										imageName += ".png";
									}
									String mImagePath = ImageUtil.IMAGE_PATH+imageName;
									/*if (url.endsWith(".png")) {
										saveImagePng(mImagePath, mbitmap);
									} else if (url.endsWith(".jpg")) {
										saveImageJpeg(mImagePath, mbitmap);
									}*/
									saveBitmapToData(imageName, mbitmap, mContext);
									// System.out.println("向SD卡获取图片");
		
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							callback.loadImage(mbitmap, url);
						}
						

					}
				}
			};

			getImageRunnable run = new getImageRunnable(imgUrl,handler,callback);
			ThreadPoolManager.getInstance().addTask(run);
		}
		return bitmap;
	}
	
	class getImageRunnable implements Runnable{
		
		private String url;
		private Handler mHandler;
		private ImageCallback mCallback;
		public getImageRunnable(String imgUrl, Handler handler,ImageCallback callback) {
			url = imgUrl;
			mHandler = handler;
			mCallback = callback;
		}

		@Override
		public void run() {
			try {
				synchronized (imageCache) {
					mCallback.onStart(url);
				}
				
				Bitmap bitmap = null;
				if (url != null && !"".equals(url)) {
					byte[] b = getUrlBytes(url);
					/*
					 * BitmapFactory.Options opts = new
					 * BitmapFactory.Options(); opts.inJustDecodeBounds
					 * = true;
					 * BitmapFactory.decodeStream(getUrlInputStream
					 * (imgUrl), null, opts);
					 * 
					 * opts.inSampleSize = computeSampleSize(opts, -1,
					 * 128*128); opts.inJustDecodeBounds = false; try {
					 * bitmap =
					 * BitmapFactory.decodeStream(getUrlInputStream
					 * (imgUrl), null, opts); } catch (OutOfMemoryError
					 * err) { }
					 */
					bitmap = BitmapFactory.decodeByteArray(b, 0,
							b.length);
					// Bitmap bitmap = BitmapFactory.decodeStream(bis);
				}
				Message msg = mHandler.obtainMessage();
				Map<String, Bitmap> bitmapMap = new HashMap<String, Bitmap>();
				bitmapMap.put(url, bitmap);
				msg.obj = bitmapMap;
				mHandler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				synchronized (imageCache) {
					mCallback.onFailed(url);
				}
				
			}
		}
		
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 获取指定路径的Byte[]数据-通用
	 * 
	 * @param urlpath
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getUrlBytes(String urlpath) throws Exception {
		InputStream in_s = getUrlInputStream(urlpath);
		return readStream(in_s);
	}

	/**
	 * 获取指定路径的InputStream数据-通用
	 * 
	 * @param urlpath
	 * @return byte[]
	 * @throws Exception
	 */
	public static InputStream getUrlInputStream(String urlpath)
			throws Exception {
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestMethod("GET");
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(15*1000);// 10秒超时
		int responseCode = conn.getResponseCode();
		Log.i("image", responseCode + "");
		System.out.println(responseCode + "");
		if (responseCode == HttpURLConnection.HTTP_OK) {// 返回码200等于返回成功
			InputStream inputStream = conn.getInputStream();
			return inputStream;
		}
		return null;
	}

	/**
	 * 从InputStream中读取数据-通用
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[128];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		inStream.close();
		return outstream.toByteArray();
	}

	/**
	 * 获取缓存路径
	 * 
	 * @return sd卡路径
	 */
	public static String getCacheImgPath() {
		return Environment.getExternalStorageDirectory().getPath()
				+ "/ambow/cache/";
	}
	/**
	 * 将图片保存到data/data目录下
	 * 
	 * @param name
	 * @param bitmap
	 * @param context
	 */
	public void saveBitmapToData(String name, Bitmap bitmap, Context context) {
		try {
			FileOutputStream localFileOutputStream1 = context.openFileOutput(
					name, 0);

			Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.PNG;
			bitmap.compress(localCompressFormat, 100, localFileOutputStream1);

			localFileOutputStream1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取data/data下的图片文件
	 * @param name
	 * @param context
	 * @return
	 */
	public Bitmap getBitmapFromData(String name, Context context) {
		FileInputStream localStream;
		Bitmap bitmap = null;
		try {
			localStream = context.openFileInput(name);
			bitmap = BitmapFactory.decodeStream(localStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return bitmap;
	}
	

	/**
	 * 类功能描述：给网络上获取的图片设置的回调
	 * 
	 * @version 1.0 </p> 修改时间：</br> 修改备注：</br>
	 */
	public interface ImageCallback {
		public void onStart(String imageUrl);
		public void loadImage(Bitmap bitmap, String imageUrl);
		public void onFailed(String imageUrl);
	}

}
