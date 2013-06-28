package com.witmob.bookopen;

import android.content.Context;


/**
 * Created with IntelliJ IDEA. User: marshal Date: 13-3-13 Time: 上午11:48 To
 * change this template use File | Settings | File Templates.
 */
public class BookUtils {
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	/**
	   * Convert x to openGL
	   * 
	   * @param x
	   *            Screen x offset top left
	   * @return Screen x offset top left in OpenGL
	   */
	  public static float toGLX(float x,float ratio,float screenWidth) {
	      return -1.0f * ratio + toGLWidth(x,ratio,screenWidth);
	  }
	 
	  /**
	   * Convert y to openGL y
	   * 
	   * @param y
	   *            Screen y offset top left
	   * @return Screen y offset top left in OpenGL
	   */
	  public static float toGLY(float y,float screenHeight) {
	      return 1.0f - toGLHeight(y,screenHeight);
	  }
	 
	  /**
	   * Convert width to openGL width
	   * 
	   * @param width
	   * @return Width in openGL
	   */
	  public static float toGLWidth(float width,float ratio,float screenWidth) {
	      return 2.0f * (width / screenWidth) * ratio;
	  }
	 
	  /**
	   * Convert height to openGL height
	   * 
	   * @param height
	   * @return Height in openGL
	   */
	  public static float toGLHeight(float height,float screenHeight) {
	      return 2.0f * (height / screenHeight);
	  }
	 
	  /**
	   * Convert x to screen x
	   * 
	   * @param glX
	   *            openGL x
	   * @return screen x
	   */
	  public static float toScreenX(float glX,float ratio,float screenWidth) {
	      return toScreenWidth(glX - (-1 * ratio),ratio,screenWidth);
	  }
	 
	  /**
	   * Convert y to screent y
	   * 
	   * @param glY
	   *            openGL y
	   * @return screen y
	   */
	  public static float toScreenY(float glY,float screenHeight) {
	      return toScreenHeight(1.0f - glY,screenHeight);
	  }
	 
	  /**
	   * Convert glWidth to screen width
	   * 
	   * @param glWidth
	   * @return Width in screen
	   */
	  public static float toScreenWidth(float glWidth,float ratio,float screenWidth) {
	      return (glWidth * screenWidth) / (2.0f * ratio);
	  }
	 
	  /**
	   * Convert height to screen height
	   * 
	   * @param glHeight
	   * @return Height in screen
	   */
	  public static float toScreenHeight(float glHeight,float screenHeight) {
	      return (glHeight * screenHeight) / 2.0f;
	  }
	
}
