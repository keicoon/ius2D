package game;

import android.content.Context;
import android.view.MotionEvent;

import com.example.opengles20.GameObject;
import com.example.opengles20.myGLSurfaceView;

import ius.Scene;

public class SceneTITLE extends Scene {
	
	GameObject BG;
	boolean flag;
	public SceneTITLE(Context context, myGLSurfaceView gl20) {
		super(context, gl20);
	}

	@Override
	public void Init() {
		mGL20.soundManager.LoadSound("cheetahmen.mp3",  true);
		mGL20.soundManager.LoadSound("menu_select.mp3", false);
		
		flag = false;
		
		BG = mGL20.objectmanager.newItem(
				mContext, mGL20,
				"spr_bg", 0, 0,
				screenWidth*0.5f, screenHeight*0.5f,
				0f, 1.0f);
		mGL20.soundManager.PlayBGMSound();
		setShader(0);	//default Shader
	}

	@Override
	public void Destroy() {
		mGL20.soundManager.ClearSound();
		mGL20.objectmanager.ClearItem();
	}

	@Override
	protected void Draw() {
		// TODO Auto-generated method stub
		BG.Draw(screenWidth, screenHeight);
	}

	@Override
	protected void Update(float INTERVAL) {
		if(doFadeOut(INTERVAL, 0.3f))
			//TODO change nextScene
			mGL20.ChangeScene(this, new ScenePrologue(mContext, mGL20));
	}

	@Override
	protected void Input() {
		// TODO Auto-generated method stub
		if(TOUCH_EVENT == MotionEvent.ACTION_DOWN){
			flag = true;
		}
		else if(flag && TOUCH_EVENT == MotionEvent.ACTION_UP){
			mGL20.soundManager.PlaySound("menu_select.mp3", false);
			setFadeState(1.0f);
		}

	}
}
