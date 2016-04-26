package com.minfo.quanmei.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

import com.minfo.quanmei.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 图片处理类
 * @author wangrui@min-fo.com
 */
@SuppressLint("SdCardPath")
public class MinfoImg {
	private Context context;
	private int designBaseWidth = 720;
	private int designBaseHeight = 1280;
	private Handler handler;
	private String itemImgPath;
	
	public  MinfoImg(Context context){
		this.context = context;
		//确定网络图片保存目录
    	itemImgPath = "/data/data/" + context.getPackageName() + "/ItemImage/";
    	File destDir = new File(itemImgPath);
    	if (!destDir.exists()){
    		if(destDir.mkdirs() == false){
    			itemImgPath = "/data/data/" + context.getPackageName();
        	}
    	}
	}
	
	/**
	 * 在临时目录中生成压缩后的图片(压缩后最大为100k)
	 * @param filename
	 * @param bmp
	 * @author wangrui@min-fo.com
	 */
	public void createTemImg(String filename, Bitmap bmp){
		Bitmap new_bmp = null;
		if(bmp.getWidth() >= bmp.getHeight()){
			if(bmp.getWidth() > 800 || bmp.getHeight() > 480){
				new_bmp = getZoomBp(800, 480, 3, bmp);
			}
		}else{
			if(bmp.getWidth() > 480 || bmp.getHeight() > 800){
				new_bmp = getZoomBp(800, 480, 3, bmp);
			}
			new_bmp = getZoomBp(480, 800, 3, bmp);
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new_bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);  
		
        try {  
            FileOutputStream fos = new FileOutputStream(filename);  
            fos.write(baos.toByteArray()); 
            fos.flush();  
            fos.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	public String getImgName(String str){
		String[] list = str.split("/");
		return list[(list.length - 1)];
	}
	
	//对Bitmap进行缩放
	public Bitmap getZoomBp(int w, int h, int tag, Bitmap bm){
		Bitmap return_bm = null;
		
		int oldw = bm.getWidth();
		int oldh = bm.getHeight();
		
		Matrix matrix = new Matrix();
		float scale_w;
		float scale_h;
		
		if(tag == 1){
			float scale_base;
			if((w / h) <= (oldw / oldh)){
				scale_base = ((float) w) / oldw;
			}else{
				scale_base = ((float) h) / oldh;
			}
			scale_w = scale_h = scale_base;
			
		}else if(tag == 2){
			scale_w = ((float) w) / oldw;
			scale_h = ((float) h) / oldh;
		}else if(tag == 3){
			scale_w = scale_h = ((float) w) / oldw;
		}else{
			scale_w = scale_h = ((float) h) / oldh;
		}
		
		matrix.postScale(scale_w, scale_h);
		return_bm = Bitmap.createBitmap(bm, 0, 0, oldw, oldh, matrix, true);
		
		return return_bm;
	}
	
	//从缓存目录中获取图片Bitmap对象
	public Bitmap getBpByTempDir(String url){
		String filename = itemImgPath + getFileName(url);
    	File f = new File(filename);
    	Bitmap bmpLogo = null;
    	if(f.exists()){
    		FileInputStream fis = null;
			try {
				fis = new FileInputStream(filename);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
    		bmpLogo = BitmapFactory.decodeStream(fis);
    	}
    	return bmpLogo;
	}
	
	
	
	/*----------异步加载网络图片用的方法集----------*/
	public void loadWebImg(final String url, final ImageView tagIv,
			final int w, final int h, final int zoomTag){
		handler = new Handler();
		new Thread(new Runnable(){
			public void run(){
				if(url.equals("")){
					final Bitmap bitmap = getZoomBp(getBpByRes(R.mipmap.index_default), w, h, zoomTag);
					handler.post(new Runnable() {
						public void run() {
							tagIv.setImageBitmap(bitmap);
						}
					});
				}else{
					String img_filename = itemImgPath + getFileName(url);
					File f = new File(img_filename);
					if(f.exists()){
						//缓存目录有文件，直接读取，进行缩放
						handler.post(new Runnable(){
							public void run(){
								tagIv.setImageBitmap(getZoomBpByPath(url, w, h, zoomTag));
							}
						});
					}else{
						//下载图片到缓存目录，然后读取，缩放
						Bitmap source_bp = getBpByUrl(url);
						if(source_bp == null){
							final Bitmap bitmap = getZoomBp(getBpByRes(R.mipmap.index_default), w,
									h, zoomTag);
							
							handler.post(new Runnable() {
								public void run() {
									tagIv.setImageBitmap(bitmap);
								}
							});
						}else{
							final Bitmap bitmap = getZoomBp(source_bp, w, h, zoomTag);
							
							try {					
								f.createNewFile();
								FileOutputStream fOut = null;
			        			fOut = new FileOutputStream(f);
			        			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			        			fOut.flush();
			        			fOut.close();
			        		} catch (IOException e) {
			        			e.printStackTrace();
			        		}
							
							handler.post(new Runnable() {
								public void run() {
									tagIv.setImageBitmap(bitmap);
								}
							});
						}
					}
				}
			}
		}).start();
	}
	
	//通过url获取Bitmap对象
	public Bitmap getBpByUrl(String url){
		Bitmap bm = null;
		InputStream is;
		try{
			is = getIsByUrl(url);
			bm = BitmapFactory.decodeStream(is);
			is.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return bm;
	}
	
	//根据图片url写入InputStream
	private InputStream getIsByUrl(String uri) throws MalformedURLException{
		URL url = new URL(uri);
		URLConnection conn;
		InputStream is;
		try {
			conn = url.openConnection();
			conn.connect();
			is = conn.getInputStream();
			return is;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public Bitmap getBpFromPath(String path){
		Bitmap return_bp = null;
		return_bp = BitmapFactory.decodeFile(path);
		return return_bp;
	}
	
	//从缓存目录中获取图片Bitmap对象
	public Bitmap getZoomBpByPath(String url, int width, int height, int zoomTag){
		String filename = itemImgPath + getFileName(url);
    	File f = new File(filename);
    	Bitmap bmpLogo = null;
    	if(f.exists()){
    		bmpLogo = BitmapFactory.decodeFile(filename);
    		return getZoomBp(bmpLogo, width, height, zoomTag);
    		/*
    		BitmapFactory.Options opt = new BitmapFactory.Options();
    		opt.inJustDecodeBounds = true;//不加载bitmap到内存中  
            BitmapFactory.decodeFile(filename,opt); 
            
            int outWidth = opt.outWidth;  
            int outHeight = opt.outHeight;  
            opt.inDither = false;  
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;  
            opt.inSampleSize = 1;
            
            if(zoomTag == 3){
            	int sampleSize=outWidth/width;  
                opt.inSampleSize = sampleSize;
            }else{
                int sampleSize=(outWidth/width+outHeight/height)/2;    
                opt.inSampleSize = sampleSize;  
            }
          
            opt.inJustDecodeBounds = false; 
            return BitmapFactory.decodeFile(filename, opt);
            */
    	}
    	return bmpLogo;
	}
	
	private Bitmap getZoomBp(Bitmap old_bp, int new_width, int new_height, int scaleType){
		int old_width = old_bp.getWidth();   //原图宽
		int old_height = old_bp.getHeight(); //原图高
		
		Matrix matrix = new Matrix();
		float scale_w;
		float scale_h;
		
		//其它值表示不失真；2表示按固定宽高(可能会失真)；3表示按宽比例；4表示按高比例
		switch(scaleType){
			case 2:
				scale_w = ((float) new_width) / old_width;
				scale_h = ((float) new_height) / old_height;
				break;
			case 3:
				scale_w = scale_h = ((float) new_width) / old_width;
				break;
			case 4:
				scale_w = scale_h = ((float) new_height) / old_height;
				break;
			default:
				float scale_base;
				if ((new_width / old_width) <= (new_height / old_height)) {
					scale_base = ((float) new_width) / old_width;
				}else{
					scale_base = ((float) new_height) / old_height;
				}
				scale_w = scale_h = scale_base;
				break;
		}
		
		matrix.postScale(scale_w, scale_h);
		
		Bitmap result_bp = Bitmap.createBitmap(old_bp, 0, 0, old_width, old_height, matrix, true);
		if(result_bp != null){
			return result_bp;
		}
		
		return null;
	}
	
	//通过图片的url获取图片名称
	private String getFileName(String imgUrl){
    	int index,start=0;
		while((index = imgUrl.indexOf("/", start))!=-1){
			start = index + 1;
		}
		return imgUrl.substring(start);
    }
	
	/*----------异步加载网络图片用的方法集结束----------*/
	public int getNewSize(int baseValue, double reValue, int tag){
		int return_value = 0;
		if(tag == 1){
			return_value = (int)(reValue * baseValue / 11);
		}else{
			return_value = (int)(reValue * baseValue / 19);
		}
		return return_value;
	}
	
	public int getRealSize(int oldSize, int baseSize, int sizeType){
		if(sizeType == 1){
			return oldSize * baseSize / designBaseWidth;
		}
		return oldSize * baseSize / designBaseHeight;
	}
	
	/*----------缩放资源图片用的方法集(R.drawable.imgname的图片)----------*/
	
	//以最节省内存的形式获取Bitmap对象
	private Bitmap getBpByRes(int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();  
        opt.inPreferredConfig = Bitmap.Config.RGB_565;  
        opt.inPurgeable = true;  
        opt.inInputShareable = true;  
        //获取资源图片  
        InputStream is = context.getResources().openRawResource(resId);  
        return BitmapFactory.decodeStream(is, null, opt);
	}
	/**
	 * 获取屏幕宽
	 *
	 * @return 屏幕高
	 * @author wangrui@min-fo.com
	 * @date 2014-05-24
	 */
	public int getScreenWidth(){
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}
	
	/**
	 * 获取屏幕高
	 *
	 * @return 屏幕高
	 * @author wangrui@min-fo.com
	 * @date 2014-05-24
	 */
	public int getScreenHeight(){
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}
	//缩放图片
	public Bitmap getZoomBp(int resId, int new_width, int new_height, int scaleType){
		Bitmap old_bp = getBpByRes(resId);
		
		int old_width = old_bp.getWidth();   //原图宽
		int old_height = old_bp.getHeight(); //原图高
		
		Matrix matrix = new Matrix();
		float scale_w;
		float scale_h;
		
		//其它值表示不失真；2表示按固定宽高(可能会失真)；3表示按宽比例；4表示按高比例
		switch(scaleType){
			case 2:
				scale_w = ((float) new_width) / old_width;
				scale_h = ((float) new_height) / old_height;
				break;
			case 3:
				scale_w = scale_h = ((float) new_width) / old_width;
				break;
			case 4:
				scale_w = scale_h = ((float) new_height) / old_height;
				break;
			default:
				float scale_base;
				if ((new_width / old_width) <= (new_height / old_height)) {
					scale_base = ((float) new_width) / old_width;
				}else{
					scale_base = ((float) new_height) / old_height;
				}
				scale_w = scale_h = scale_base;
				break;
		}
		
		matrix.postScale(scale_w, scale_h);
		
		Bitmap result_bp = Bitmap.createBitmap(old_bp, 0, 0, old_width, old_height, matrix, true);
		if(result_bp != null){
			return result_bp;
		}
		
		return null;
	}
	/**
	 * 向指定的Handler send massage
	 * author wangrui
	 * date 2015-03-13
	 */
	public void sendMsg(Handler handler, int msgtag){
		Message m = new Message();
		m.what = msgtag;
		handler.sendMessage(m);
	}
}