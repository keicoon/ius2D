package com.example.opengles20;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
/* MainActivity Class
 * Function : Android Application의 메인 클래스
 */
public class MainActivity extends Activity {

	private myGLSurfaceView mGLSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		/* openGLES을 사용하기 위해서 SurfaceView를 생성함 */
		mGLSurfaceView = new myGLSurfaceView(this);
		
		/* openGLES2.0과 관련된 설정 */
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
	    final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
	   
	    if (!mGLSurfaceView.RenderStart(supportsEs2)) 
		{
	    	// This is where you could create an OpenGL ES 1.x compatible
	    	// renderer if you wanted to support both ES 1 and ES 2.
	    				
	    	return;
		} 
	    
	  //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로 화면으로 고정할 경우
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로 화면으로 고정할 경우
	    
	    setContentView(mGLSurfaceView);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mGLSurfaceView.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mGLSurfaceView.onResume();
	}
}
