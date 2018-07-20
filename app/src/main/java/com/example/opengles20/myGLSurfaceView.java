package com.example.opengles20;

import game.SceneLOADING;
import ius.Scene;
import ius.SoundManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
/* myGLSurfaceView Class
 * Function : SurfaceView를 상속받는 클래스
 */
public class myGLSurfaceView extends GLSurfaceView{

	// openGLES2.0과 관련된 변수
	private final myGLRenderer mRenderer;
	private Scene currentScene;
	// engine에서 사용하는 Manager 변수

	public myGLSurfaceView(Context context) {
		
		super(context);
		
		//this option is call one time and need to write 'draw call'
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		//start first Scene
		currentScene = new SceneLOADING(this);
		
		//set renderer
		mRenderer = new myGLRenderer(context, currentScene);
	}
	public boolean RenderStart(boolean isLevel2)
	{
		if(isLevel2){
			//OpenglES 2.0 Render
			setEGLContextClientVersion(2);
			//save the context and don't call loading again
			setPreserveEGLContextOnPause(true);
			setRenderer(mRenderer);
			return true;
		}
		else{
			//OpenglES 1.0 Render
			return false;
		}
	}
	public void ChangeScene(Scene current, Scene next){
		current.Destroy();
		current = null;
		
		currentScene = next;
		mRenderer.SetCurrentScene(currentScene);
		currentScene.Init();
	}
	
	public void onResume(){
		super.onResume();
		SoundManager.getInstance().PauseBGMSound(true);
	}
	public void onPause() {
		super.onPause();
		SoundManager.getInstance().PauseBGMSound(false);
	};
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent e) {
	    // MotionEvent reports input details from the touch screen
	    // and other input controls. In this case, you are only
	    // interested in events where the touch position changed.
		synchronized(this){
		if(currentScene != null)
			currentScene.onTouchEvent(e);
		}
	    return true;
	}
}
