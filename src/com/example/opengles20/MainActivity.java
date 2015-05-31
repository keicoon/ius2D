package com.example.opengles20;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
/* MainActivity Class
 * Function : Android Application�� ���� Ŭ����
 */
public class MainActivity extends Activity {

	private myGLSurfaceView mGLSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		/* openGLES�� ����ϱ� ���ؼ� SurfaceView�� ������ */
		mGLSurfaceView = new myGLSurfaceView(this);
		
		/* openGLES2.0�� ���õ� ���� */
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
	    final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
	   
	    if (!mGLSurfaceView.RenderStart(supportsEs2)) 
		{
	    	// This is where you could create an OpenGL ES 1.x compatible
	    	// renderer if you wanted to support both ES 1 and ES 2.
	    				
	    	return;
		} 
	    
	  //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // ���� ȭ������ ������ ���
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // ���� ȭ������ ������ ���
	    
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
