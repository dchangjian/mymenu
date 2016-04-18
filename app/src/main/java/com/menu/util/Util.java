package com.menu.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Toast;

/**
 * 辅助类
 * @author Administrator
 *
 */
public class Util {

	public static final int SAVE_FACE_IMAGE = 1;
	public static final int SAVE_STEP_IMAGE = 2;

	public static void showToast(Context c,String content){
		Toast.makeText(c, content, Toast.LENGTH_SHORT).show();
	}
	
	public static void log(String tag,String msg){
		Log.d(tag, msg);
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
    
    /** 
     * 压缩图片 
     * @param bitmap 源图片 
     * @param width 想要的宽度 
     * @param height 想要的高度 
     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩 
     * @return Bitmap 
     */  
	public static Bitmap reduce(Bitmap bitmap, int width, int height, boolean isAdjust) {
		// 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图
		if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
			return bitmap;
		}
		// 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor,
		// int scale, int roundingMode);
		// scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
		float sx = new BigDecimal(width).divide(
				new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN)
				.floatValue();
		float sy = new BigDecimal(height).divide(
				new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN)
				.floatValue();
		if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
			sx = (sx < sy ? sx : sy);
			sy = sx;// 哪个比例小一点，就用哪个比例
		}
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}
	
	public static byte[] bitmap2Bytes(Bitmap bmp){
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		bmp.compress(CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        
        bmp.recycle();//自由选择是否进行回收
        
        byte[] result = output.toByteArray();//转换成功了
        
        try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return result;
	}
	
	public static Bitmap Bytes2Bimap(byte[] b) {
		if(b == null){
			return null;
		}
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	
	public static void bitmap2File(Bitmap bmp, File file){
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			bmp.compress(CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
			output.close();
		} 
		catch(Exception e){
			e.printStackTrace();
		}
	}


}
