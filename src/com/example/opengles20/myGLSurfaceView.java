package com.example.opengles20;

import game.SceneINGAME;
import ius.AtlasManager;
import ius.CameraManager;
import ius.FontManager;
import ius.ObjectManager;
import ius.Scene;
import ius.SoundManager;
import ius.TimeManager;
import ius.InputManager;
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
	private final Scene currentScene;
	// engine에서 사용하는 Manager 변수
	public ObjectManager objectmanager;
	public final AtlasManager atlasManager;
	public FontManager fontManager;
	public TimeManager timeManager;
	public InputManager inputManager;
	public SoundManager soundManager;
	public CameraManager cameraManager;
	
	public myGLSurfaceView(Context context) {
		
		super(context);
		
		//this option is call one time and need to write 'draw call'
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		//Initialize managers
		objectmanager = new ObjectManager();
		atlasManager = new AtlasManager();
		fontManager = new FontManager("Roboto-Regular.ttf", context);
		timeManager = new TimeManager();
		inputManager = new InputManager();
		soundManager = new SoundManager(context);
		
		//start first Scene
		currentScene = new SceneINGAME(context, this);
		
		//set renderer
		mRenderer = new myGLRenderer(context, this);
		mRenderer.SetCurrentScene(currentScene);
		
		//set cameraManager
		cameraManager = new CameraManager();
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
	public void ChangeScene(Scene prev, Scene next){
		prev.Destroy();
		next.Init();
		
		prev = null;
		mRenderer.SetCurrentScene(next);
	}
	
	public void onResume(){
		super.onResume();
		soundManager.PauseBGMSound(true);
	}
	public void onPause() {
		super.onPause();
		soundManager.PauseBGMSound(false);
	};
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent e) {
	    // MotionEvent reports input details from the touch screen
	    // and other input controls. In this case, you are only
	    // interested in events where the touch position changed.
		synchronized(currentScene.mGL20){
		if(currentScene != null)
			currentScene.onTouchEvent(e);
		}
	    return true;
	}
}
